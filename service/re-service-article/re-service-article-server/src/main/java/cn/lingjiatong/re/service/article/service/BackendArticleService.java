package cn.lingjiatong.re.service.article.service;

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
import cn.lingjiatong.re.service.article.api.dto.BackendArticleListDTO;
import cn.lingjiatong.re.service.article.api.dto.BackendArticlePublishDTO;
import cn.lingjiatong.re.service.article.api.dto.BackendDraftSaveOrUpdateDTO;
import cn.lingjiatong.re.service.article.api.vo.BackendArticleListVO;
import cn.lingjiatong.re.service.article.api.vo.BackendDraftDetailVO;
import cn.lingjiatong.re.service.article.api.vo.BackendDraftListVO;
import cn.lingjiatong.re.service.article.constant.BackendArticleErrorMessageConstant;
import cn.lingjiatong.re.service.article.entity.Article;
import cn.lingjiatong.re.service.article.entity.ArticleEs;
import cn.lingjiatong.re.service.article.mapper.ArticleMapper;
import cn.lingjiatong.re.service.sys.api.client.BackendUserFeignClient;
import cn.lingjiatong.re.service.sys.api.vo.BackendUserListVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
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
 * ??????????????????service???
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

    // ********************************???????????????********************************

    /**
     * ????????????????????????
     *
     * @param dto ????????????????????????DTO??????
     * @param currentUser ????????????
     */
    @Transactional(rollbackFor = Exception.class)
    public void publishArticle(BackendArticlePublishDTO dto, User currentUser) {
        // ??????????????????
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
            // ????????????
            backendTagService.saveBatch(article.getId(), tagList);
            // ??????es
            ArticleEs articleEs = new ArticleEs();
            BeanUtils.copyProperties(article, articleEs);
            articleEs.setTagList(tagList);
            elasticsearchRestTemplate.save(articleEs);
            // ????????????
            redisUtil.deleteObject(RedisCacheKeyEnum.ARTICLE_DRAFT.getValue().replaceAll("username", currentUser.getUsername()).replaceAll("draftId", draftId));
        } catch (Exception e) {
            log.error("==========??????????????????????????????{}", e.getMessage());
            throw new BusinessException(ErrorEnum.SAVE_ARTICLE_ERROR);
        }
    }


    /**
     * ?????????????????????????????????
     * ???????????????redis???
     *
     * @param dto ?????????????????????DTO??????
     * @param currentUser ????????????
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

        // ?????????????????????
        redisUtil.setCacheObject(redisKey, draftCache);
    }


    // ********************************???????????????********************************

    /**
     * ?????????????????????
     *
     * @param draftId ??????id
     * @param currentUser ????????????
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

    // ********************************???????????????********************************

    // ********************************???????????????********************************

    /**
     * ????????????????????????
     *
     * @param dto ????????????????????????DTO??????
     * @return ????????????????????????VO??????????????????
     */
    public Page<BackendArticleListVO> findArticleList(BackendArticleListDTO dto) {
        // ??????????????????
        dto.generateOrderCondition();
        Page page = new Page<>(dto.getPageNum(), dto.getPageSize());
        // ???????????????
        page.setSearchCount(false);
        Page<BackendArticleListVO> articleList = articleMapper.findArticleList(page, dto);
        long total = articleMapper.findArticleListTotal(dto);
        page.setTotal(total);
        // ???????????????????????????????????????
        List<BackendArticleListVO> records = articleList.getRecords();
        if (!CollectionUtils.isEmpty(records)) {
            List<Long> articleIdList = records
                    .stream()
                    .map(BackendArticleListVO::getId)
                    .collect(Collectors.toList());
            List<Long> userIdList = records.stream()
                    .map(BackendArticleListVO::getUserId)
                    .collect(Collectors.toList());
            Future<Map<Long, List<String>>> articleTagListFuture = commonThreadPool.submit(() -> backendTagService.findTagListByArticleIdList(articleIdList));
            Map<Long, String> articleAuthorMap = Maps.newHashMap();
            Map<Long, String> userMap = Maps.newHashMap();
            List<BackendUserListVO> voList = backendUserFeignClient.findUserListByUserIdList(userIdList).getData();
            voList.forEach(vo -> {
                userMap.put(Long.valueOf(vo.getId()), vo.getUsername());
            });
            records.forEach(record -> {
                articleAuthorMap.put(record.getId(), userMap.get(record.getUserId()));
            });

            //??????fegin????????????????????????
//            RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
//            Future<Map<Long, String>> articleAuthorFuture = commonThreadPool.submit(() -> {
//                RequestContextHolder.setRequestAttributes(attributes);
//            });
            try {
                Map<Long, List<String>> articleTagListMap = articleTagListFuture.get();
//                Map<Long, String> articleAuthorMap = articleAuthorFuture.get();
                records.forEach(record -> {
                    Long id = record.getId();
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
     * ????????????????????????
     *
     * @param currentUser ????????????
     * @param draftId ??????id
     * @return ??????????????????VO??????
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
     * ???????????????????????????????????????
     *
     * @param currentUser ??????????????????
     * @return ??????????????????????????????
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
        //  ??????????????????????????????????????????????????????30????????????????????????????????????redis????????????
        List<Object> multiValues = redisUtil.getMultiValues(keys);
        for (Object value : multiValues) {
            DraftCache draftCache = (DraftCache) value;
            BackendDraftListVO backendDraftListVO = new BackendDraftListVO();
            BeanUtils.copyProperties(draftCache, backendDraftListVO);
            result.add(backendDraftListVO);
        }
        return result.stream().sorted(Comparator.comparing(BackendDraftListVO::getSaveTime).reversed()).collect(Collectors.toList());
    }

    // ********************************????????????********************************

    /**
     * ??????BackendArticlePublishDTO?????????
     *
     * @param dto ????????????????????????DTO??????
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

        // ????????????
        if (!StringUtils.hasLength(title)) {
            // ??????????????????
            throw new ParamErrorException(ErrorEnum.REQUEST_PARAM_ERROR.getCode(), BackendArticleErrorMessageConstant.ARTICLE_TITLE_EMPTY_ERROR_MESSAGE);
        }
        if (!StringUtils.hasLength(markdownContent)) {
            // ????????????????????????
            throw new ParamErrorException(ErrorEnum.REQUEST_PARAM_ERROR.getCode(), BackendArticleErrorMessageConstant.ARTICLE_MARKDOWN_CONTENT_EMPTY_ERROR_MESSAGE);
        }
        if (!StringUtils.hasLength(summary)) {
            // ?????????????????????????????????????????????200???????????????????????????
            summary = getSummaryFromMarkdownContent(markdownContent);
            dto.setSummary(summary);
        }

        if (categoryId == null) {
            throw new ParamErrorException(ErrorEnum.REQUEST_PARAM_ERROR.getCode(), BackendArticleErrorMessageConstant.ARTICLE_CATEGORY_NULL_ERROR_MESSAGE);
        } else {
            if (categoryId < 0) {
                // ?????????????????????null ????????????0
                throw new ParamErrorException(ErrorEnum.REQUEST_PARAM_ERROR.getCode(), BackendArticleErrorMessageConstant.ARTICLE_CATEGORY_NULL_ERROR_MESSAGE);
            }
        }

        if (!StringUtils.hasLength(coverUrl)) {
            // ????????????url?????????????????????????????????
            coverUrl = ArticleConstant.DEFAULT_COVER_URL;
            dto.setCoverUrl(coverUrl);
        }
        if (recommend == null) {
            // ???????????????????????????????????????????????????
            recommend = ArticleConstant.ARTICLE_NOT_RECOMMEND.intValue();
            dto.setRecommend(recommend);
        }
        if (creationType == null) {
            // ???????????????????????????????????????????????????????????????
            creationType = ArticleConstant.ARTICLE_CREATION_YC.intValue();
            dto.setCreationType(creationType);
        }
        if (top == null) {
            // ????????????????????????????????????????????????
            top = ArticleConstant.ARTICLE_NOT_TOP.intValue();
            dto.setTop(top);
        }

        // ??????????????????
        // ??????????????????100?????????
        if (!ArticleConstant.TITLE_REGEX.matcher(title).matches()) {
            throw new ParamErrorException(ErrorEnum.REQUEST_PARAM_ERROR.getCode(), BackendArticleErrorMessageConstant.ARTICLE_TITLE_FORMAT_ERROR_MESSAGE);
        }
        // ??????????????????200?????????
        if (!ArticleConstant.SUMMARY_REGEX.matcher(summary).matches()) {
            throw new ParamErrorException(ErrorEnum.REQUEST_PARAM_ERROR.getCode(), BackendArticleErrorMessageConstant.ARTICLE_SUMMARY_FORMAT_ERROR_MESSAGE);
        }
        // ??????????????????????????????
        if (!backendCategoryService.isExistById(categoryId)) {
            throw new ParamErrorException(ErrorEnum.REQUEST_PARAM_ERROR.getCode(), BackendArticleErrorMessageConstant.ARTICLE_CATEGORY_NOT_EXIST_ERROR_MESSAGE);
        }
        // ???????????????????????????
        if (!ArticleConstant.recommendValues().contains(recommend.byteValue())) {
            throw new ParamErrorException(ErrorEnum.ILLEGAL_PARAM_ERROR);
        }
        // ?????????????????????????????????
        if (!ArticleConstant.creationTypeValues().contains(creationType.byteValue())) {
            throw new ParamErrorException(ErrorEnum.ILLEGAL_PARAM_ERROR);
        }
        // ???????????????????????????
        if (!ArticleConstant.topValues().contains(top.byteValue())) {
            throw new ParamErrorException(ErrorEnum.ILLEGAL_PARAM_ERROR);
        }
        // ???????????????????????????????????????????????????
        if (ArticleConstant.ARTICLE_CREATION_ZZ.intValue() == creationType && StringUtils.isEmpty(transportInfo)) {
            throw new ParamErrorException(ErrorEnum.REQUEST_PARAM_ERROR.getCode(), BackendArticleErrorMessageConstant.NO_TRANSPORT_INFO_ERROR_MESSAGE);
        }
        // ??????????????????
        if (!CollectionUtils.isEmpty(tagList)) {
            tagList.forEach(tag -> {
                if (!ArticleConstant.TAG_NAME_REGEX.matcher(tag).matches()) {
                    throw new ParamErrorException(ErrorEnum.REQUEST_PARAM_ERROR.getCode(), BackendArticleErrorMessageConstant.TAG_NAME_FORMAT_ERROR_MESSAGE);
                }
            });
        }
    }

    /**
     * ????????????????????????????????????
     *
     * @param markdownContent markdown??????
     * @return ????????????
     */
    @NonNull
    private String getSummaryFromMarkdownContent(@NonNull String markdownContent) {
        StringBuilder result = new StringBuilder();
        String[] lines = markdownContent.split("\\r?\\n");
        if (lines.length > 0) {
            // ????????? # ?????????????????????????????????
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
