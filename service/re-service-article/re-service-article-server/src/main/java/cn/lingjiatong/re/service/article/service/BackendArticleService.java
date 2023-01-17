package cn.lingjiatong.re.service.article.service;

import cn.lingjiatong.re.common.ResultVO;
import cn.lingjiatong.re.common.constant.ArticleConstant;
import cn.lingjiatong.re.common.constant.CommonConstant;
import cn.lingjiatong.re.common.constant.RedisCacheKeyEnum;
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
import cn.lingjiatong.re.service.article.api.dto.*;
import cn.lingjiatong.re.service.article.api.vo.BackendArticleListVO;
import cn.lingjiatong.re.service.article.api.vo.BackendDraftDetailVO;
import cn.lingjiatong.re.service.article.api.vo.BackendDraftListVO;
import cn.lingjiatong.re.service.article.constant.BackendArticleErrorMessageConstant;
import cn.lingjiatong.re.service.article.entity.Article;
import cn.lingjiatong.re.common.entity.es.ESArticle;
import cn.lingjiatong.re.service.article.mapper.ArticleMapper;
import cn.lingjiatong.re.service.sys.api.client.BackendUserFeignClient;
import cn.lingjiatong.re.service.sys.api.vo.BackendUserListVO;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.document.Document;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.UpdateQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
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
    @Autowired
    @Qualifier("commonThreadPool")
    private ExecutorService commonThreadPool;
    @Autowired
    private BackendUserFeignClient backendUserFeignClient;

    // ********************************新增类接口********************************

    /**
     * 后端文章发布接口
     *
     * @param dto 后台文章发布接口DTO对象
     * @param currentUser 当前用户
     */
    @Transactional(rollbackFor = Exception.class)
    public void publishArticle(BackendArticlePublishDTO dto, User currentUser) {
        // 校验文章参数
        checkArticlePublishDTO(dto);
        String title = dto.getTitle();
        String draftId = dto.getDraftId();
        String summary = dto.getSummary();
        Long categoryId = dto.getCategoryId();
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
        article.setOptUser(currentUser.getUsername());
        article.setUserId(currentUser.getId());
        article.setCreateTime(DateUtil.getLocalDateTimeNow());
        article.setModifyTime(DateUtil.getLocalDateTimeNow());
        article.setQuoteInfo(quoteInfo);
        article.setTransportInfo(dto.getTransportInfo());
        article.setDeleted(CommonConstant.ENTITY_NORMAL);

        try {
            articleMapper.insert(article);
            // 插入标签列表
            backendTagService.saveTagBatch(tagList);
            // 插入文章和标签的关联关系
            backendTagService.saveTrArticleTag(article.getId(), tagList);
        } catch (Exception e) {
            log.error(e.toString(), e);
            throw new BusinessException(ErrorEnum.SAVE_ARTICLE_ERROR);
        }

        try {
            // 插入es
            ESArticle esArticle = new ESArticle();
            BeanUtils.copyProperties(article, esArticle);
            esArticle.setTagList(tagList);
            elasticsearchRestTemplate.save(esArticle);
            // 删除草稿
            redisUtil.deleteObject(RedisCacheKeyEnum.ARTICLE_DRAFT.getValue()
                    .replaceAll("username", currentUser.getUsername())
                    .replaceAll("draftId", draftId));
        } catch (Exception e) {
            // 出现异常，需要删除之前插入的文章标签列表和文章标签关系信息
            log.error(e.toString(), e);
            backendTagService.deleteTagBatch(tagList);
            backendTagService.deleteTrArticleTagBatch(List.of(article.getId()));
            throw new BusinessException(ErrorEnum.SAVE_ARTICLE_ERROR);
        }

        // TODO 重新统计标签的总浏览量和总喜欢数
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

    /**
     * 后端批量删除文章
     *
     * @param dto 后端批量删除文章DTO对象
     * @param currentUser 当前用户
     * @return 通用消息返回对象
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteArticleBatch(BackendArticleDeleteBatchDTO dto, User currentUser) {
        List<Long> articleIdList = dto.getArticleIdList();
        if (CollectionUtils.isEmpty(articleIdList)) {
            return;
        }
        List<String> articleIdStrList = articleIdList
                .stream()
                .map(String::valueOf)
                .collect(Collectors.toList());
        // 物理删除，删除文章所有相关信息
        try {
            // 删除文章
            articleMapper.deleteBatchIds(articleIdList);
            // 删除文章标签
            backendTagService.deleteTrArticleTagBatch(articleIdList);
            // 删除es数据
            String[] articleIds = articleIdStrList.toArray(String[]::new);
            NativeSearchQuery nativeSearchQuery = new NativeSearchQuery(QueryBuilders.idsQuery().addIds(articleIds));
            elasticsearchRestTemplate.delete(nativeSearchQuery, ESArticle.class, IndexCoordinates.of("article"));
        } catch (Exception e) {
            log.error(e.toString(), e);
            throw new BusinessException(ErrorEnum.COMMON_SERVER_ERROR);
        }
        // TODO 重新计算分类的总浏览量和总喜欢数，这个可以异步执行（消息队列方式）
    }


    // ********************************修改类接口********************************

    /**
     * 后端批量更新文章推荐状态
     *
     * @param dto 后端批量更新文章推荐状态DTO对象
     * @param currentUser 当前用户
     * @return 通用消息返回对象
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateArticleRecommendBatch(BackendArticleUpdateRecommendBatchDTO dto, User currentUser) {
        List<Long> articleIdList = dto.getArticleIdList();
        if (CollectionUtils.isEmpty(articleIdList)) {
            return;
        }
        if (!ArticleConstant.recommendValues().contains(dto.getRecommend())) {
            throw new ParamErrorException(ErrorEnum.ILLEGAL_PARAM_ERROR);
        }
        try {
            articleMapper.update(null, new LambdaUpdateWrapper<Article>()
                    .set(Article::getRecommend, dto.getRecommend())
                    .set(Article::getOptUser, currentUser.getUsername())
                    .set(Article::getModifyTime, LocalDateTime.now(ZoneId.of("Asia/Shanghai")))
                    .in(Article::getId, articleIdList));
        } catch (Exception e) {
            log.error(e.toString(), e);
            throw new BusinessException(ErrorEnum.COMMON_SERVER_ERROR);
        }
    }

    /**
     * 后端批量更新文章置顶状态
     *
     * @param dto 后端批量更新文章置顶状态DTO对象
     * @param currentUser 当前用户
     * @return 通用消息返回对象
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateArticleTopBatch(BackendArticleUpdateTopBatchDTO dto, User currentUser) {
        List<Long> articleIdList = dto.getArticleIdList();
        if (CollectionUtils.isEmpty(articleIdList)) {
            return;
        }
        if (!ArticleConstant.topValues().contains(dto.getTop())) {
            throw new ParamErrorException(ErrorEnum.ILLEGAL_PARAM_ERROR);
        }
        try {
            articleMapper.update(null, new LambdaUpdateWrapper<Article>()
                    .set(Article::getTop, dto.getTop())
                    .set(Article::getOptUser, currentUser.getUsername())
                    .set(Article::getModifyTime, LocalDateTime.now(ZoneId.of("Asia/Shanghai")))
                    .in(Article::getId, articleIdList));
        } catch (Exception e) {
            log.error(e.toString(), e);
            throw new BusinessException(ErrorEnum.COMMON_SERVER_ERROR);
        }
    }

    /**
     * 后端批量更新文章删除状态
     *
     * @param dto 后端批量更新文章删除状态DTO对象
     * @param currentUser 当前用户
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateArticleDeleteBatch(BackendArticleUpdateDeleteBatchDTO dto, User currentUser) {
        List<Long> articleIdList = dto.getArticleIdList();
        Byte delete = dto.getDelete();
        if (CollectionUtils.isEmpty(articleIdList)) {
            return;
        }
        if (!CommonConstant.deleteValues().contains(delete)) {
            throw new ParamErrorException(ErrorEnum.ILLEGAL_PARAM_ERROR);
        }

        try {
            articleMapper.update(null, new LambdaUpdateWrapper<Article>()
                    .set(Article::getDeleted, delete)
                    .set(Article::getOptUser, currentUser.getUsername())
                    .set(Article::getModifyTime, LocalDateTime.now(ZoneId.of("Asia/Shanghai")))
                    .in(Article::getId, articleIdList));
            List<UpdateQuery> updateQueryList = Lists.newArrayList();
            articleIdList.forEach(articleId -> {
                Document document = Document.create();
                document.put("deleted", delete);
                UpdateQuery updateQuery = UpdateQuery
                        .builder(String.valueOf(articleId))
                        .withDocument(document)
                        .build();
                updateQueryList.add(updateQuery);
            });
            //  更新es
            elasticsearchRestTemplate.bulkUpdate(updateQueryList, IndexCoordinates.of("article"));

            // TODO 重新计算分类的总浏览量和总喜欢数，这个可以异步执行（消息队列方式）
        } catch (Exception e) {
            log.error(e.toString(), e);
            throw new BusinessException(ErrorEnum.COMMON_SERVER_ERROR);
        }
    }


    // ********************************查询类接口********************************

    /**
     * 分页获取文章列表
     *
     * @param dto 后端获取文章列表DTO对象
     * @return 后端获取文章列表VO对象分页对象
     */
    public Page<BackendArticleListVO> findArticleList(BackendArticleListDTO dto) {
        // 生成排序条件
        dto.generateOrderCondition();
        Page page = new Page<>(dto.getPageNum(), dto.getPageSize());
        // 不查询总数
        page.setSearchCount(false);
        Page<BackendArticleListVO> articleList = articleMapper.findBackendArticleList(page, dto);
        long total = articleMapper.findBackendArticleListTotal(dto);
        page.setTotal(total);
        // 查询文章作者，查询文章标签
        List<BackendArticleListVO> records = articleList.getRecords();
        if (!CollectionUtils.isEmpty(records)) {
            List<Long> articleIdList = records
                    .stream()
                    .map(vo -> Long.valueOf(vo.getId()))
                    .collect(Collectors.toList());
            List<Long> userIdList = records.stream()
                    .map(BackendArticleListVO::getUserId)
                    .collect(Collectors.toList());
            Future<Map<Long, List<String>>> articleTagListFuture = commonThreadPool.submit(() -> backendTagService.findTagListByArticleIdList(articleIdList));
            Map<Long, String> articleAuthorMap = Maps.newHashMap();
            Map<Long, String> userMap = Maps.newHashMap();

            ResultVO<List<BackendUserListVO>> resultVO = backendUserFeignClient.findUserListByUserIdList(userIdList);
            if (!ResultVO.CODE_SUCCESS.equals(resultVO.getCode()) || !ResultVO.MESSAGE_SUCCESS.equals(resultVO.getMessage())) {
                throw new BusinessException(ErrorEnum.COMMON_SERVER_ERROR);
            }

            List<BackendUserListVO> voList = resultVO.getData();
            voList.forEach(vo -> {
                userMap.put(Long.valueOf(vo.getId()), vo.getUsername());
            });
            records.forEach(record -> {
                articleAuthorMap.put(Long.valueOf(record.getId()), userMap.get(record.getUserId()));
            });

            //防止fegin获取不到当前请求
//            RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
//            Future<Map<Long, String>> articleAuthorFuture = commonThreadPool.submit(() -> {
//                RequestContextHolder.setRequestAttributes(attributes);
//            });
            try {
                Map<Long, List<String>> articleTagListMap = articleTagListFuture.get();
//                Map<Long, String> articleAuthorMap = articleAuthorFuture.get();
                records.forEach(record -> {
                    Long id = Long.valueOf(record.getId());
                    List<String> tagList = articleTagListMap.get(id);
                    String author = articleAuthorMap.get(id);
                    record.setTagList(tagList);
                    record.setAuthor(author);
                });
            } catch (Exception e) {
                log.error(e.toString(), e);
                throw new BusinessException(ErrorEnum.COMMON_SERVER_ERROR);
            }
        }
        return articleList;
    }

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
     * 校验BackendArticlePublishDTO的参数
     *
     * @param dto 后台文章发布接口DTO对象
     */
    private void checkArticlePublishDTO(BackendArticlePublishDTO dto) {
        String title = dto.getTitle();
        String summary = dto.getSummary();
        Long categoryId = dto.getCategoryId();
        String coverUrl = dto.getCoverUrl();
        Integer recommend = dto.getRecommend();
        Integer creationType = dto.getCreationType();
        Integer top = dto.getTop();
        String markdownContent = dto.getMarkdownContent();
        String transportInfo = dto.getTransportInfo();
        List<String> tagList = dto.getTagList();

        // 空值校验
        if (!StringUtils.hasLength(title)) {
            // 标题不能为空
            throw new ParamErrorException(ErrorEnum.REQUEST_PARAM_ERROR.getCode(), BackendArticleErrorMessageConstant.ARTICLE_TITLE_EMPTY_ERROR_MESSAGE);
        }
        if (!StringUtils.hasLength(markdownContent)) {
            // 文章内容不能为空
            throw new ParamErrorException(ErrorEnum.REQUEST_PARAM_ERROR.getCode(), BackendArticleErrorMessageConstant.ARTICLE_MARKDOWN_CONTENT_EMPTY_ERROR_MESSAGE);
        }
        if (!StringUtils.hasLength(summary)) {
            // 如果简介为空，默认获取文章的前200个字符作为文章简介
            summary = getSummaryFromMarkdownContent(markdownContent);
            dto.setSummary(summary);
        }

        if (categoryId == null) {
            throw new ParamErrorException(ErrorEnum.REQUEST_PARAM_ERROR.getCode(), BackendArticleErrorMessageConstant.ARTICLE_CATEGORY_NULL_ERROR_MESSAGE);
        } else {
            if (categoryId < 0) {
                // 文章分类不能为null 或者小于0
                throw new ParamErrorException(ErrorEnum.REQUEST_PARAM_ERROR.getCode(), BackendArticleErrorMessageConstant.ARTICLE_CATEGORY_NULL_ERROR_MESSAGE);
            }
        }

        if (!StringUtils.hasLength(coverUrl)) {
            // 封面图片url如果为空，使用默认封面
            coverUrl = ArticleConstant.DEFAULT_COVER_URL;
            dto.setCoverUrl(coverUrl);
        }
        if (recommend == null) {
            // 如果没有设置推荐，默认设置为不推荐
            recommend = ArticleConstant.ARTICLE_NOT_RECOMMEND.intValue();
            dto.setRecommend(recommend);
        }
        if (creationType == null) {
            // 如果没有设置是原创还是转载，那么默认是原创
            creationType = ArticleConstant.ARTICLE_CREATION_YC.intValue();
            dto.setCreationType(creationType);
        }
        if (top == null) {
            // 如果没有设置是否置顶，那么不置顶
            top = ArticleConstant.ARTICLE_NOT_TOP.intValue();
            dto.setTop(top);
        }

        // 校验规则校验
        // 标题不能超过100个字符
        if (!ArticleConstant.TITLE_REGEX.matcher(title).matches()) {
            throw new ParamErrorException(ErrorEnum.REQUEST_PARAM_ERROR.getCode(), BackendArticleErrorMessageConstant.ARTICLE_TITLE_FORMAT_ERROR_MESSAGE);
        }
        // 简介不能超过200个字符
        if (!ArticleConstant.SUMMARY_REGEX.matcher(summary).matches()) {
            throw new ParamErrorException(ErrorEnum.REQUEST_PARAM_ERROR.getCode(), BackendArticleErrorMessageConstant.ARTICLE_SUMMARY_FORMAT_ERROR_MESSAGE);
        }
        // 校验文章分类是否存在
        if (!backendCategoryService.isExistById(categoryId)) {
            throw new ParamErrorException(ErrorEnum.REQUEST_PARAM_ERROR.getCode(), BackendArticleErrorMessageConstant.ARTICLE_CATEGORY_NOT_EXIST_ERROR_MESSAGE);
        }
        // 校验推荐值是否存在
        if (!ArticleConstant.recommendValues().contains(recommend.byteValue())) {
            throw new ParamErrorException(ErrorEnum.ILLEGAL_PARAM_ERROR);
        }
        // 校验创作类型值是否存在
        if (!ArticleConstant.creationTypeValues().contains(creationType.byteValue())) {
            throw new ParamErrorException(ErrorEnum.ILLEGAL_PARAM_ERROR);
        }
        // 校验置顶值是否存在
        if (!ArticleConstant.topValues().contains(top.byteValue())) {
            throw new ParamErrorException(ErrorEnum.ILLEGAL_PARAM_ERROR);
        }
        // 当文章是转载类型时，必须有转载说明
        if (ArticleConstant.ARTICLE_CREATION_ZZ.intValue() == creationType && StringUtils.isEmpty(transportInfo)) {
            throw new ParamErrorException(ErrorEnum.REQUEST_PARAM_ERROR.getCode(), BackendArticleErrorMessageConstant.NO_TRANSPORT_INFO_ERROR_MESSAGE);
        }
        // 标签格式校验
        if (!CollectionUtils.isEmpty(tagList)) {
            tagList.forEach(tag -> {
                if (!ArticleConstant.TAG_NAME_REGEX.matcher(tag).matches()) {
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
    private String getSummaryFromMarkdownContent(String markdownContent) {
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
