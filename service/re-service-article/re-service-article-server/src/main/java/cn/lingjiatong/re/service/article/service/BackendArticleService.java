package cn.lingjiatong.re.service.article.service;

import cn.lingjiatong.re.common.constant.CommonConstant;
import cn.lingjiatong.re.common.constant.RedisCacheKeyEnum;
import cn.lingjiatong.re.common.constant.UserConstant;
import cn.lingjiatong.re.common.entity.User;
import cn.lingjiatong.re.common.entity.cache.DraftCache;
import cn.lingjiatong.re.common.exception.BusinessException;
import cn.lingjiatong.re.common.exception.ErrorEnum;
import cn.lingjiatong.re.common.exception.ParamErrorException;
import cn.lingjiatong.re.common.exception.ResourceNotExistException;
import cn.lingjiatong.re.common.util.DateUtil;
import cn.lingjiatong.re.common.util.RandomUtil;
import cn.lingjiatong.re.common.util.RedisUtil;
import cn.lingjiatong.re.common.util.SnowflakeIdWorkerUtil;
import cn.lingjiatong.re.service.article.api.dto.BackendArticleSaveDTO;
import cn.lingjiatong.re.service.article.api.dto.BackendDraftSaveOrUpdateDTO;
import cn.lingjiatong.re.service.article.api.vo.BackendDraftDetailVO;
import cn.lingjiatong.re.service.article.api.vo.BackendDraftListVO;
import cn.lingjiatong.re.service.article.constant.BackendArticleConstant;
import cn.lingjiatong.re.service.article.constant.BackendArticleErrorMessageConstant;
import cn.lingjiatong.re.service.article.entity.Article;
import cn.lingjiatong.re.service.article.entity.ArticleEs;
import cn.lingjiatong.re.service.article.mapper.ArticleMapper;
import com.google.common.collect.Lists;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 后台文章模块service层
 *
 * @author Ling, Jiatong
 * Date: 2022/10/17 21:58
 */
@Slf4j
@Service
public class BackendArticleService {

    @Resource
    private ArticleMapper articleMapper;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;
    @Autowired
    private BackendCategoryService backendCategoryService;
    @Autowired
    private BackendTagService backendTagService;
    @Autowired
    private SnowflakeIdWorkerUtil snowflakeIdWorkerUtil;

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
        Long categoryId = Long.valueOf(dto.getCategoryId());
        String coverUrl = dto.getCoverUrl();
        Integer recommend = dto.getRecommend();
        Integer creationType = dto.getCreationType();
        Integer top = dto.getTop();
        String markdownContent = dto.getMarkdownContent();
        String htmlContent = dto.getHtmlContent();
        String quoteInfo = dto.getQuoteInfo();
        List<String> tagList = dto.getTagList();

        Article article = new Article();
        article.setId(snowflakeIdWorkerUtil.nextId());
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
        article.setDeleted(CommonConstant.ENTITY_NORMAL);

        try {
            articleMapper.insert(article);
            // 插入标签
            backendTagService.saveBatch(article.getId(), tagList);
            // 插入es
            ArticleEs articleEs = new ArticleEs();
            BeanUtils.copyProperties(article, articleEs);
            articleEs.setTagList(tagList);
            elasticsearchRestTemplate.save(articleEs);
        } catch (Exception e) {
            log.error("==========保存文章失败，异常：{}", e.getMessage());
            throw new BusinessException(ErrorEnum.SAVE_ARTICLE_ERROR);
        }
    }


    /**
     * 后端保存或更新文章草稿
     * 默认保存到redis中
     *
     * @param dto 草稿保存或更新DTO对象
     * @param currentUser 当前用户
     */
    public void saveOrUpdateDraft(BackendDraftSaveOrUpdateDTO dto, User currentUser) {
        String title = dto.getTitle();
        String markdownContent = dto.getMarkdownContent();
        String draftId = dto.getDraftId();
        if (StringUtils.isEmpty(title)) {
            throw new ParamErrorException(ErrorEnum.REQUEST_PARAM_ERROR.getCode(), BackendArticleErrorMessageConstant.DRAFT_TITLE_EMPTY_ERROR_MESSAGE);
        }
        Optional.ofNullable(markdownContent)
                .orElseThrow(() -> new ParamErrorException(ErrorEnum.REQUEST_PARAM_ERROR));


        if (!StringUtils.hasLength(draftId)) {
            draftId = RandomUtil.getInstance().generateUUID();
        }
        String redisKey = RedisCacheKeyEnum.ARTICLE_DRAFT.getValue()
                .replace("username", currentUser.getUsername())
                .replace("draftId", draftId);
        DraftCache draftCache = new DraftCache();
        draftCache.setTitle(title);
        draftCache.setMarkdownContent(markdownContent);
        draftCache.setDraftId(draftId);
        draftCache.setSaveTime(LocalDateTime.now(ZoneId.of("Asia/Shanghai")));

        // 不设置过期时间
        redisUtil.setCacheObject(redisKey, draftCache);
    }


    // ********************************删除类接口********************************

    /**
     * 删除用户的草稿
     *
     * @param draftId 草稿id
     * @param currentUser 当前用户
     */
    public void deleteDraft(String draftId, User currentUser) {
        if (!StringUtils.hasLength(draftId)) {
            throw new ParamErrorException(ErrorEnum.REQUEST_PARAM_ERROR.getCode(), BackendArticleErrorMessageConstant.DRAFT_ID_EMPTY_ERROR_MESSAGE);
        }

        String redisKey = RedisCacheKeyEnum.ARTICLE_DRAFT.getValue()
                .replace("username", currentUser.getUsername())
                .replace("draftId", draftId);
        Set<String> keys = redisUtil.keys(redisKey);
        if (CollectionUtils.isEmpty(keys)) {
            throw new ResourceNotExistException(ErrorEnum.RESOURCE_NOT_EXIST_ERROR);
        }
        redisUtil.deleteObjects(keys);
    }

    // ********************************修改类接口********************************

    // ********************************查询类接口********************************

    /**
     * 后端获取草稿详情
     *
     * @param currentUser 当前用户
     * @param draftId 草稿id
     * @return 文章草稿详情VO对象
     */
    public BackendDraftDetailVO getDraftDetail(String draftId, User currentUser) {
        if (!StringUtils.hasLength(draftId)) {
            throw new ParamErrorException(ErrorEnum.REQUEST_PARAM_ERROR);
        }
        String key = RedisCacheKeyEnum.ARTICLE_DRAFT.getValue()
                .replace("username", currentUser.getUsername())
                .replace("draftId", draftId);
        DraftCache draftCache = (DraftCache) redisUtil.getCacheObject(key);
        Optional.ofNullable(draftCache)
                .orElseThrow(() -> new ResourceNotExistException(ErrorEnum.RESOURCE_NOT_EXIST_ERROR));
        BackendDraftDetailVO result = new BackendDraftDetailVO();
        BeanUtils.copyProperties(draftCache, result);
        return result;
    }

    /**
     * 获取当前用户的所有草稿列表
     *
     * @param currentUser 当前用户实体
     * @return 该用户的所有草稿列表
     */
    public List<BackendDraftListVO> getDraftList(User currentUser) {
        String keyPattern = RedisCacheKeyEnum.ARTICLE_DRAFT.getValue()
                .replace("username", currentUser.getUsername())
                .replace("draftId", "*");
        Set<String> keys = redisUtil.keys(keyPattern);
        if (CollectionUtils.isEmpty(keys)) {
            return Lists.newArrayList();
        }

        List<BackendDraftListVO> result = Lists.newArrayList();
        //  如果使用获取单个会非常耗时间，当超过30条时会导致失败，因此使用redis获取多个
        List<Object> multiValues = redisUtil.getMultiValues(keys);
        for (Object value : multiValues) {
            DraftCache draftCache = (DraftCache) value;
            BackendDraftListVO backendDraftListVO = new BackendDraftListVO();
            BeanUtils.copyProperties(draftCache, backendDraftListVO);
            result.add(backendDraftListVO);
        }
        return result.stream().sorted(Comparator.comparing(BackendDraftListVO::getSaveTime).reversed()).collect(Collectors.toList());
    }

    // ********************************私有函数********************************

    /**
     * 校验BackendArticleSaveDTO的参数
     *
     * @param dto 后台保存文章接口DTO对象
     */
    private void checkSaveArticleDTO(BackendArticleSaveDTO dto) {
        String title = dto.getTitle();
        String summary = dto.getSummary();
        String categoryIdStr = dto.getCategoryId();
        Long categoryId;
        String coverUrl = dto.getCoverUrl();
        Integer recommend = dto.getRecommend();
        Integer creationType = dto.getCreationType();
        Integer top = dto.getTop();
        String markdownContent = dto.getMarkdownContent();
        String transportInfo = dto.getTransportInfo();
        List<String> tagList = dto.getTagList();

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

        if (!StringUtils.hasLength(categoryIdStr)) {
            throw new ParamErrorException(ErrorEnum.REQUEST_PARAM_ERROR.getCode(), BackendArticleErrorMessageConstant.ARTICLE_CATEGORY_NULL_ERROR_MESSAGE);
        } else {
            categoryId = Long.valueOf(categoryIdStr);
            if (categoryId < 0) {
                // 文章分类不能为null 或者小于0
                throw new ParamErrorException(ErrorEnum.REQUEST_PARAM_ERROR.getCode(), BackendArticleErrorMessageConstant.ARTICLE_CATEGORY_NULL_ERROR_MESSAGE);
            }
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
        // 标签格式校验
        if (!CollectionUtils.isEmpty(tagList)) {
            tagList.forEach(tag -> {
                if (!BackendArticleConstant.TAG_NAME_REGEX.matcher(tag).matches()) {
                    throw new ParamErrorException(ErrorEnum.REQUEST_PARAM_ERROR.getCode(), BackendArticleErrorMessageConstant.TAG_NAME_FORMAT_ERROR_MESSAGE);
                }
            });
        }
    }

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
        if (result.length() > 200) {
            return result.substring(0, 200);
        } else {
            return result.toString();
        }
    }


}
