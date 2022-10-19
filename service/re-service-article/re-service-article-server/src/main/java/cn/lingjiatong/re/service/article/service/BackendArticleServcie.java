package cn.lingjiatong.re.service.article.service;

import cn.lingjiatong.re.common.constant.CommonConstant;
import cn.lingjiatong.re.common.constant.MinioConstant;
import cn.lingjiatong.re.common.constant.RedisCacheKeyEnum;
import cn.lingjiatong.re.common.constant.UserConstant;
import cn.lingjiatong.re.common.exception.BusinessException;
import cn.lingjiatong.re.common.exception.ErrorEnum;
import cn.lingjiatong.re.common.exception.ParamErrorException;
import cn.lingjiatong.re.common.exception.ServerException;
import cn.lingjiatong.re.common.util.*;
import cn.lingjiatong.re.service.article.api.dto.BackendArticleSaveDTO;
import cn.lingjiatong.re.service.article.constant.BackendArticleConstant;
import cn.lingjiatong.re.service.article.constant.BackendArticleErrorMessageConstant;
import cn.lingjiatong.re.service.article.entity.Article;
import cn.lingjiatong.re.service.article.entity.Category;
import cn.lingjiatong.re.service.article.mapper.ArticleMapper;
import io.minio.ObjectWriteResponse;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Optional;
import java.util.Set;

/**
 * 后台文章模块service层
 *
 * @author Ling, Jiatong
 * Date: 2022/10/17 21:58
 */
@Slf4j
@Service
public class BackendArticleServcie {

    @Resource
    private ArticleMapper articleMapper;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private MinioUtil minioUtil;
    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;
    @Autowired
    private BackendCategoryService backendCategoryService;

    // ********************************新增类接口********************************

    /**
     * 后端保存文章接口
     *
     * @param dto 后台保存文章接口DTO对象
     */
    @Transactional(rollbackFor = Exception.class)
    public void saveArticle(BackendArticleSaveDTO dto) {
        // 校验文章参数
        checkSaveArticleDTO(dto);
        String title = dto.getTitle();
        String summary = dto.getSummary();
        Integer categoryId = dto.getCategoryId();
        String coverUrl = dto.getCoverUrl();
        Integer recommend = dto.getRecommend();
        Integer creationType = dto.getCreationType();
        Integer top = dto.getTop();
        String markdownContent = dto.getMarkdownContent();
        String htmlContent = dto.getHtmlContent();
        String quoteInfo = dto.getQuoteInfo();

        Article article = new Article();
        article.setTitle(title);
        article.setSummary(summary);
        article.setCategoryId(categoryId);
        article.setDeleted(CommonConstant.ENTITY_NORMAL);
        article.setFavorite(0L);
        article.setView(0L);
        article.setCoverUrl(coverUrl);
        article.setCreationType(creationType.byteValue());
        article.setMarkdownContent(markdownContent);
        article.setHtmlContent(htmlContent);
        article.setTop(top.byteValue());
        article.setRecommend(recommend.byteValue());
        // TODO 这里先写死，后期加入权限系统之后改为获取当前登录用户
        article.setOptUser(UserConstant.SUPER_ADMIN_USER);
        article.setUserId(UserConstant.SUPER_ADMIN_USER_ID);
        article.setCreateTime(DateUtil.getLocalDateTimeNow());
        article.setModifyTime(DateUtil.getLocalDateTimeNow());
        article.setQuoteInfo(quoteInfo);
        article.setTransportInfo(dto.getTransportInfo());

        try {
            articleMapper.insert(article);
//            elasticsearchRestTemplate.save(article);

            // TODO 对文章标签的处理
        } catch (Exception e) {
            log.error("==========保存文章失败，异常：{}", e.getMessage());
            throw new BusinessException(ErrorEnum.SAVE_ARTICLE_ERROR);
        }

    }


    /**
     * 保存后端文章封面图片
     * 当文章没保存时，先将封面图保存在minio中
     *
     * @param multipartFile 图片
     * @param title 文章标题
     * @return 图片的minio访问地址
     */
    public String saveArticleCoverImage(String title, MultipartFile multipartFile) {
        if (StringUtils.isEmpty(title)) {
            throw new ParamErrorException(ErrorEnum.REQUEST_PARAM_ERROR.getCode(), BackendArticleErrorMessageConstant.ARTICLE_TITLE_EMPTY_ERROR_MESSAGE);
        }
        Optional.ofNullable(multipartFile)
                .orElseThrow(() -> new ParamErrorException(ErrorEnum.REQUEST_PARAM_ERROR));

        try {
            String objectName =  MinioConstant.ARTICLE_COVER_FOLDER + "/" + multipartFile.getOriginalFilename();
            minioUtil.uploadFile(MinioConstant.MINIO_BUCKET_NAME, multipartFile, objectName, multipartFile.getContentType());
            String urlWithParam = minioUtil.getPresignedObjectUrl(MinioConstant.MINIO_BUCKET_NAME, objectName);
            return UrlUtil.removeUrlParamter(urlWithParam);
        } catch (Exception e) {
            log.error(e.toString(), e);
            throw new ServerException(ErrorEnum.MINIO_SERVER_ERROR);
        }
    }


    /**
     * 后端保存文章草稿
     * 默认保存到redis中
     *
     * @param markdownContent 文章草稿markdown内容
     */
    public void saveDraftArticle(String draftTitle, String markdownContent) {
        if (StringUtils.isEmpty(draftTitle)) {
            throw new ParamErrorException(ErrorEnum.REQUEST_PARAM_ERROR.getCode(), BackendArticleErrorMessageConstant.DRAFT_TITLE_EMPTY_ERROR_MESSAGE);
        }
        Optional.ofNullable(markdownContent)
                .orElseThrow(() -> new ParamErrorException(ErrorEnum.REQUEST_PARAM_ERROR));

        // 先获取上次已保存的值，如果不存在则设置，存在则删除再设置
        Set<String> keys = redisUtil.keys("daft:" + draftTitle + "*");
        if (!CollectionUtils.isEmpty(keys)) {
            keys.forEach(key -> {
                redisUtil.deleteObject(key);
            });
        }

        String redisKey = RedisCacheKeyEnum.ARTICLE_DRAFT.getValue()
                .replace("draftTitle", draftTitle)
                .replace("timestamp", String.valueOf(System.currentTimeMillis()))
                .replace("uuid", RandomUtil.getInstance().generateUUID());

        // 不设置过期时间
        redisUtil.setCacheObject(redisKey, markdownContent);
    }

    // ********************************删除类接口********************************

    // ********************************修改类接口********************************

    // ********************************查询类接口********************************


    /**
     * 校验BackendArticleSaveDTO的参数
     *
     * @param dto 后台保存文章接口DTO对象
     */
    private void checkSaveArticleDTO(BackendArticleSaveDTO dto) {
        String title = dto.getTitle();
        String summary = dto.getSummary();
        Integer categoryId = dto.getCategoryId();
        String coverUrl = dto.getCoverUrl();
        Integer recommend = dto.getRecommend();
        Integer creationType = dto.getCreationType();
        Integer top = dto.getTop();
        String markdownContent = dto.getMarkdownContent();
        String transportInfo = dto.getTransportInfo();

        // 空值校验
        if (StringUtils.isEmpty(title)) {
            // 标题不能为空
            throw new ParamErrorException(ErrorEnum.REQUEST_PARAM_ERROR.getCode(), BackendArticleErrorMessageConstant.ARTICLE_TITLE_EMPTY_ERROR_MESSAGE);
        }
        if (StringUtils.isEmpty(markdownContent)) {
            // 文章内容不能为空
            throw new ParamErrorException(ErrorEnum.REQUEST_PARAM_ERROR.getCode(), BackendArticleErrorMessageConstant.ARTICLE_MARKDOWN_CONTENT_EMPTY_ERROR_MESSAGE);
        }
        if (StringUtils.isEmpty(summary)) {
            // 如果简介为空，默认获取文章的前200个字符作为文章简介
            summary = getSummaryFromMarkdownContent(markdownContent);
            dto.setSummary(summary);
        }
        if (categoryId == null || categoryId < 0) {
            // 文章分类不能为null 或者小于0
            throw new ParamErrorException(ErrorEnum.REQUEST_PARAM_ERROR.getCode(), BackendArticleErrorMessageConstant.ARTICLE_CATEGORY_NULL_ERROR_MESSAGE);
        }
        if (StringUtils.isEmpty(coverUrl)) {
            // 封面图片url如果为空，使用默认封面
            coverUrl = BackendArticleConstant.DEFAULT_COVER_URL;
            dto.setCoverUrl(coverUrl);
        }
        if (recommend == null) {
            // 如果没有设置推荐，默认设置为不推荐
            recommend = BackendArticleConstant.ARTICLE_NOT_RECOMMEND.intValue();
            dto.setRecommend(recommend);
        }
        if (creationType == null) {
            // 如果没有设置是原创还是转载，那么默认是原创
            creationType = BackendArticleConstant.ARTICLE_CREATION_YC.intValue();
            dto.setCreationType(creationType);
        }
        if (top == null) {
            // 如果没有设置是否置顶，那么不置顶
            top = BackendArticleConstant.ARTICLE_NOT_TOP.intValue();
            dto.setTop(top);
        }

        // 校验规则校验
        // 标题不能超过100个字符
        if (!BackendArticleConstant.TITLE_REGEX.matcher(title).matches()) {
            throw new ParamErrorException(ErrorEnum.REQUEST_PARAM_ERROR.getCode(), BackendArticleErrorMessageConstant.ARTICLE_TITLE_FORMAT_ERROR_MESSAGE);
        }
        // 简介不能超过200个字符
        if (!BackendArticleConstant.SUMMARY_REGEX.matcher(summary).matches()) {
            throw new ParamErrorException(ErrorEnum.REQUEST_PARAM_ERROR.getCode(), BackendArticleErrorMessageConstant.ARTICLE_SUMMARY_FORMAT_ERROR_MESSAGE);
        }
        // 校验文章分类是否存在
        if (!backendCategoryService.isExistById(categoryId)) {
            throw new ParamErrorException(ErrorEnum.REQUEST_PARAM_ERROR.getCode(), BackendArticleErrorMessageConstant.ARTICLE_CATEGORY_NOT_EXIST_ERROR_MESSAGE);
        }
        // 校验推荐值是否存在
        if (!BackendArticleConstant.recommendValues().contains(recommend.byteValue())) {
            throw new ParamErrorException(ErrorEnum.ILLEGAL_PARAM_ERROR);
        }
        // 校验创作类型值是否存在
        if (!BackendArticleConstant.creationTypeValues().contains(creationType.byteValue())) {
            throw new ParamErrorException(ErrorEnum.ILLEGAL_PARAM_ERROR);
        }
        // 校验置顶值是否存在
        if (!BackendArticleConstant.topValues().contains(top.byteValue())) {
            throw new ParamErrorException(ErrorEnum.ILLEGAL_PARAM_ERROR);
        }
        // 当文章是转载类型时，必须有转载说明
        if (BackendArticleConstant.ARTICLE_CREATION_ZZ.intValue() == creationType && StringUtils.isEmpty(transportInfo)) {
            throw new ParamErrorException(ErrorEnum.REQUEST_PARAM_ERROR.getCode(), BackendArticleErrorMessageConstant.NO_TRANSPORT_INFO_ERROR_MESSAGE);
        }

        // TODO 标签格式校验
    }

    // ********************************私有函数********************************

    /**
     * 从文章内容中获取简介内容
     *
     * @param markdownContent markdown内容
     * @return 文章简介
     */
    @NonNull
    private String getSummaryFromMarkdownContent(@NonNull String markdownContent) {
        StringBuilder result = new StringBuilder();
        String[] lines = markdownContent.split("\\r?\\n");
        if (lines.length > 0) {
            // 如果以 # 开头则为标题，跳过该行
            for (String line : lines) {
                if (line.startsWith("#") || "".equals(line)) {
                    continue;
                }
                result.append(line).append("\n");
                if (result.length() > 200) {
                    break;
                }
            }
        }
        return result.toString();
    }


}
