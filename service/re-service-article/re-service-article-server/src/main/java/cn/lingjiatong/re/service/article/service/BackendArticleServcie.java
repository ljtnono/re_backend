package cn.lingjiatong.re.service.article.service;

import cn.lingjiatong.re.common.constant.CommonConstant;
import cn.lingjiatong.re.common.constant.MinioConstant;
import cn.lingjiatong.re.common.constant.RedisCacheKeyEnum;
import cn.lingjiatong.re.common.constant.UserConstant;
import cn.lingjiatong.re.common.exception.ErrorEnum;
import cn.lingjiatong.re.common.exception.ParamErrorException;
import cn.lingjiatong.re.common.exception.ServerException;
import cn.lingjiatong.re.common.util.*;
import cn.lingjiatong.re.service.article.api.dto.BackendArticleSaveDTO;
import cn.lingjiatong.re.service.article.constant.BackendArticleErrorMessageConstant;
import cn.lingjiatong.re.service.article.entity.Article;
import cn.lingjiatong.re.service.article.mapper.ArticleMapper;
import io.minio.ObjectWriteResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.stereotype.Service;
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

    // ********************************新增类接口********************************

    /**
     * 后端保存文章接口
     *
     * @param dto 后台保存文章接口DTO对象
     */
    public void saveArticle(BackendArticleSaveDTO dto) {
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
        // 校验文章参数
        checkSaveArticleDTO(dto);

        Article article = new Article();
        article.setCategoryId(categoryId);
        article.setDeleted(CommonConstant.ENTITY_NORMAL);
        article.setFavorite(0L);
        article.setCoverUrl(coverUrl);
        article.setCreationType(creationType.byteValue());
        article.setMarkdownContent(markdownContent);
        article.setHtmlContent(htmlContent);
        // TODO 这里先写死
        article.setOptUser(UserConstant.SUPER_ADMIN_USER);
        article.setUserId(UserConstant.SUPER_ADMIN_USER_ID);
        article.setCreateTime(DateUtil.getLocalDateTimeNow());
        article.setModifyTime(DateUtil.getLocalDateTimeNow());
        article.setQuoteInfo(quoteInfo);
        article.setTransportInfo(dto.getTransportInfo());

        try {
            articleMapper.insert(article);
            elasticsearchRestTemplate.save(article);
        } catch (Exception e) {

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

    }

}
