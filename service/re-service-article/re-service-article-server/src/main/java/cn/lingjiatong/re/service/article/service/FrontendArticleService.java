package cn.lingjiatong.re.service.article.service;

import cn.lingjiatong.re.common.EsPage;
import cn.lingjiatong.re.common.ResultVO;
import cn.lingjiatong.re.common.constant.CommonConstant;
import cn.lingjiatong.re.common.constant.UserConstant;
import cn.lingjiatong.re.common.entity.es.ESArticle;
import cn.lingjiatong.re.common.exception.BusinessException;
import cn.lingjiatong.re.common.exception.ErrorEnum;
import cn.lingjiatong.re.common.exception.ParamErrorException;
import cn.lingjiatong.re.common.exception.ResourceNotExistException;
import cn.lingjiatong.re.service.article.api.dto.*;
import cn.lingjiatong.re.service.article.api.vo.*;
import cn.lingjiatong.re.service.article.constant.FrontendArticleErrorMessageConstant;
import cn.lingjiatong.re.service.article.entity.Article;
import cn.lingjiatong.re.service.article.entity.Category;
import cn.lingjiatong.re.service.article.mapper.ArticleMapper;
import cn.lingjiatong.re.service.article.mapper.CategoryMapper;
import cn.lingjiatong.re.service.article.mapper.TagMapper;
import cn.lingjiatong.re.service.sys.api.client.FrontendUserFeignClient;
import cn.lingjiatong.re.service.sys.api.vo.FrontendUserListVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.ScoreSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 前端article模块service层
 *
 * @author Ling, Jiatong
 * Date: 2022/10/20 21:39
 */
@Slf4j
@Service
public class FrontendArticleService {

    @Resource
    private ArticleMapper articleMapper;
    @Resource
    private CategoryMapper categoryMapper;
    @Resource
    private TagMapper tagMapper;
    @Autowired
    private FrontendUserFeignClient frontendUserFeignClient;
    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;


    // ********************************新增类接口********************************

    // ********************************删除类接口********************************

    // ********************************修改类接口********************************

    // ********************************查询类接口********************************

    /**
     * 获取单个文章信息
     *
     * @param articleId 文章id
     * @return 单个文章信息VO对象
     */
    @Transactional(readOnly = true)
    public FrontendArticleVO findArticle(Long articleId) {
        if (articleId < 0) {
            throw new BusinessException(ErrorEnum.ILLEGAL_PARAM_ERROR.getCode(), FrontendArticleErrorMessageConstant.ARTICLE_NOT_EXIST);
        }
        Article article = articleMapper.selectOne(new LambdaQueryWrapper<Article>()
                .eq(Article::getId, articleId)
                .eq(Article::getDeleted, CommonConstant.ENTITY_NORMAL));
        Optional.ofNullable(article)
                .orElseThrow(() -> new ResourceNotExistException(ErrorEnum.RESOURCE_NOT_EXIST_ERROR));

        // 获取其标签列表和分类名
        FrontendArticleVO result = new FrontendArticleVO();
        BeanUtils.copyProperties(article, result);
        result.setId(String.valueOf(articleId));
        result.setFinalUpdateTime(article.getModifyTime());
        Category category = categoryMapper.selectOne(new LambdaQueryWrapper<Category>()
                .select(Category::getName)
                .eq(Category::getId, article.getCategoryId())
                .eq(Category::getDeleted, CommonConstant.ENTITY_NORMAL));
        result.setCategory(category.getName());

        // 获取文章的作者信息
        ResultVO<List<FrontendUserListVO>> userList = frontendUserFeignClient.findUserListByUserIdList(List.of(article.getUserId()));
        if (!ResultVO.CODE_SUCCESS.equals(userList.getCode()) || !ResultVO.MESSAGE_SUCCESS.equals(userList.getMessage())) {
            throw new BusinessException(ErrorEnum.COMMON_SERVER_ERROR);
        }
        List<FrontendUserListVO> voList = userList.getData();
        if (CollectionUtils.isEmpty(voList)) {
            result.setAuthor(UserConstant.USER_DELETE_SHOW_NAME);
        } else {
            result.setAuthor(voList.get(0).getUsername());
        }

        // 获取标签列表
        List<String> tagList = tagMapper.findFrontendTagListByArticleId(articleId);
        result.setTagList(tagList);
        return result;
    }

    /**
     * 前端滚动获取文章列表
     *
     * 逻辑：使用分页查询逻辑，排序规则：按照创建时间 DESC、浏览量 DESC排序
     * @param dto 前端文章无限滚动列表DTO对象
     * @return 前端文章无限滚动列表VO对象分页对象
     */
    @Transactional(readOnly = true)
    public Page<FrontendArticleScrollVO> findArticleScroll(FrontendArticleScrollDTO dto) {
        // 按照创建时间 DESC、浏览量 DESC排序
        List<String> orderFieldList = dto.getOrderFieldList();
        List<Byte> orderFlagList = dto.getOrderFlagList();
        orderFieldList.add("modify_time");
        orderFieldList.add("view");
        orderFlagList.add(CommonConstant.ORDER_BY_DESC);
        orderFlagList.add(CommonConstant.ORDER_BY_DESC);

        dto.generateOrderCondition();
        Page<?> page = new Page<>(dto.getPageNum(), dto.getPageSize());
        // 不查询总数
        page.setSearchCount(false);
        Page<FrontendArticleScrollVO> articleScroll = articleMapper.findFrontendArticleScroll(page, dto);
        long total = articleMapper.findFrontendArticleScrollTotal();
        page.setTotal(total);

        // 查询文章作者
        List<FrontendArticleScrollVO> records = articleScroll.getRecords();
        if (!CollectionUtils.isEmpty(records)) {
            List<Long> userIdList = records.stream()
                    .map(FrontendArticleScrollVO::getUserId)
                    .collect(Collectors.toList());
            Map<Long, String> articleAuthorMap = Maps.newHashMap();
            Map<Long, String> userMap = Maps.newHashMap();

            ResultVO<List<FrontendUserListVO>> resultVO = frontendUserFeignClient.findUserListByUserIdList(userIdList);
            if (!ResultVO.CODE_SUCCESS.equals(resultVO.getCode()) || !ResultVO.MESSAGE_SUCCESS.equals(resultVO.getMessage())) {
                throw new BusinessException(ErrorEnum.COMMON_SERVER_ERROR);
            }

            List<FrontendUserListVO> voList = resultVO.getData();
            voList.forEach(vo -> {
                userMap.put(Long.valueOf(vo.getId()), vo.getUsername());
            });
            records.forEach(record -> {
                articleAuthorMap.put(Long.valueOf(record.getId()), userMap.get(record.getUserId()));
            });

            try {
                records.forEach(record -> {
                    Long id = Long.valueOf(record.getId());
                    String author = articleAuthorMap.get(id);
                    record.setAuthor(author);
                });
            } catch (Exception e) {
                log.error(e.toString(), e);
                throw new BusinessException(ErrorEnum.COMMON_SERVER_ERROR);
            }
        }
        return articleScroll;
    }

    /**
     * 前端分页获取文章置顶列表
     *
     * @param dto 前端分页获取文章置顶列表DTO对象
     * @return 前端分页获取文章置顶列表VO对象分页对象
     */
    @Transactional(readOnly = true)
    public Page<FrontendArticleTopListVO> findArticleTopList(FrontendArticleTopListDTO dto) {
        List<String> orderFieldList = dto.getOrderFieldList();
        List<Byte> orderFlagList = dto.getOrderFlagList();
        orderFieldList.add("modify_time");
        orderFlagList.add(CommonConstant.ORDER_BY_DESC);

        dto.generateOrderCondition();
        Page<?> page = new Page<>(dto.getPageNum(), dto.getPageSize());
        // 不查询总数
        page.setSearchCount(false);

        Page<FrontendArticleTopListVO> articleTopList = articleMapper.findFrontendArticleTopList(page, dto);
        long total = articleMapper.findFrontendArticleTopListTotal();
        page.setTotal(total);
        return articleTopList;
    }

    /**
     * 前端分页获取推荐文章列表
     *
     * @param dto 前端推荐文章列表DTO对象
     * @return 前端推荐文章列表VO对象分页对象
     */
    @Transactional(readOnly = true)
    public Page<FrontendArticleRecommendListVO> findArticleRecommendList(FrontendArticleRecommendListDTO dto) {
        List<String> orderFieldList = dto.getOrderFieldList();
        List<Byte> orderFlagList = dto.getOrderFlagList();
        orderFieldList.add("modify_time");
        orderFieldList.add("favorite");
        orderFlagList.add(CommonConstant.ORDER_BY_DESC);
        orderFlagList.add(CommonConstant.ORDER_BY_DESC);

        dto.generateOrderCondition();
        Page<?> page = new Page<>(dto.getPageNum(), dto.getPageSize());
        // 不查询总数
        page.setSearchCount(false);

        Page<FrontendArticleRecommendListVO> frontendArticleRecommendList = articleMapper.findFrontendArticleRecommendList(page, dto);
        long total = articleMapper.findFrontendArticleRecommendListTotal();
        page.setTotal(total);
        return frontendArticleRecommendList;
    }

    /**
     * 前端搜索文章列表
     *
     * @param dto 前端搜索文章列表DTO对象
     * @return 前端搜索文章列表VO对象分页对象
     */
    public EsPage<FrontendArticleSearchListVO> search(FrontendArticleSearchDTO dto) {
        // 从文章标签列表、文章简介、文章markdown内容中分词查询
        EsPage<FrontendArticleSearchListVO> esPage = new EsPage<>();
        esPage.setCurrent(dto.getPageNum());
        esPage.setSize(dto.getPageSize());

        ScoreSortBuilder scoreSortBuilder = SortBuilders
                .scoreSort()
                .order(SortOrder.DESC);
        FieldSortBuilder fieldSortBuilder = SortBuilders.fieldSort("modifyTime")
                .order(SortOrder.DESC);
        PageRequest pageRequest = PageRequest.of(dto.getPageNum() - 1, dto.getPageSize());
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery()
                .should(QueryBuilders.matchQuery("title", dto.getSearchCondition()))
                .should(QueryBuilders.matchQuery("summary", dto.getSearchCondition()))
                .should(QueryBuilders.matchQuery("markdownContent", dto.getSearchCondition()))
                // 最少满足一个条件
                .minimumShouldMatch(1)
                // 查询非隐藏的数据
                .must(QueryBuilders.matchQuery("deleted", CommonConstant.ENTITY_NORMAL));

        NativeSearchQuery query = new NativeSearchQueryBuilder()
                .withQuery(queryBuilder)
                // 分页查询
                .withPageable(pageRequest)
                // 根据匹配评分倒序
                .withSort(scoreSortBuilder)
                // 根据最后修改时间倒序
                .withSort(fieldSortBuilder)
                // 高亮
                .withHighlightFields(new HighlightBuilder.Field("*").fragmentSize(15).preTags("<font color=\"#ff55ae\">").postTags("</font>"))
                // 只获取部分字段
                .withSourceFilter(new FetchSourceFilter(new String[]{"id", "summary", "title", "categoryId", "author", "userId", "coverUrl", "view", "favorite", "modifyTime"}, null))
                .build();
        // 设置追踪总数
        query.setTrackTotalHits(true);
        // 获取高亮数据并返回
        SearchHits<ESArticle> searchHits = elasticsearchRestTemplate.search(query, ESArticle.class);
        List<SearchHit<ESArticle>> searchHitList = searchHits.getSearchHits();
        List<FrontendArticleSearchListVO> records = Lists.newArrayList();
        // 取出高亮字段并返回
        searchHitList.forEach(searchHit -> {
            ESArticle esArticle = searchHit.getContent();
            FrontendArticleSearchListVO vo = new FrontendArticleSearchListVO();
            Map<String, List<String>> highlightFields = searchHit.getHighlightFields();
            List<String> titleHighlightList = highlightFields.get("title");
            List<String> summaryHighlightList = highlightFields.get("summary");
            List<String> markdownContentHighlightList = highlightFields.get("markdownContent");
            if (!CollectionUtils.isEmpty(titleHighlightList)) {
                esArticle.setTitle(titleHighlightList.get(0));
                vo.setTitle(esArticle.getTitle());
            }
            if (!CollectionUtils.isEmpty(summaryHighlightList)) {
                esArticle.setSummary(summaryHighlightList.get(0));
                vo.setSummary(esArticle.getSummary());
            }
            if (!CollectionUtils.isEmpty(markdownContentHighlightList)) {
                esArticle.setMarkdownContent(markdownContentHighlightList.get(0));
                vo.setSummary(esArticle.getMarkdownContent());
            }

            BeanUtils.copyProperties(esArticle, vo);
            vo.setId(String.valueOf(esArticle.getId()));
            records.add(vo);
        });
        // TODO 查询文章的作者、文章的分类名
        esPage.setTotal(searchHits.getTotalHits());
        esPage.setRecords(records);
        // 设置高亮字段
        return esPage;
    }

    /**
     * 前端分页获取文章列表
     *
     * @param dto 前端分页获取文章列表DTO对象
     * @return 前端分页获取文章列表VO对象分页对象
     */
    @Transactional(readOnly = true)
    public IPage<FrontendArticleListVO> findArticleList(FrontendArticleListDTO dto) {
        Long categoryId = dto.getCategoryId();
        Long tagId = dto.getTagId();
        // 请求参数有误
        if (categoryId == null && tagId == null) {
            throw new ParamErrorException(ErrorEnum.REQUEST_PARAM_ERROR);
        }

        List<String> orderFieldList = dto.getOrderFieldList();
        List<Byte> orderFlagList = dto.getOrderFlagList();
        orderFieldList.add("modify_time");
        orderFlagList.add(CommonConstant.ORDER_BY_DESC);
        dto.generateOrderCondition();
        Page<?> page = new Page<>(dto.getPageNum(), dto.getPageSize());
        // 不查询总数
        page.setSearchCount(false);

        Page<FrontendArticleListVO> articleList = articleMapper.findArticleList(page, dto);
        long total = articleMapper.findArticleListTotal(dto);
        page.setTotal(total);

        // 查询文章作者
        List<FrontendArticleListVO> records = articleList.getRecords();
        if (!CollectionUtils.isEmpty(records)) {
            List<Long> userIdList = records.stream()
                    .map(FrontendArticleListVO::getUserId)
                    .collect(Collectors.toList());
            Map<Long, String> articleAuthorMap = Maps.newHashMap();
            Map<Long, String> userMap = Maps.newHashMap();

            ResultVO<List<FrontendUserListVO>> resultVO = frontendUserFeignClient.findUserListByUserIdList(userIdList);
            if (!ResultVO.CODE_SUCCESS.equals(resultVO.getCode()) || !ResultVO.MESSAGE_SUCCESS.equals(resultVO.getMessage())) {
                throw new BusinessException(ErrorEnum.COMMON_SERVER_ERROR);
            }

            List<FrontendUserListVO> voList = resultVO.getData();
            voList.forEach(vo -> {
                userMap.put(Long.valueOf(vo.getId()), vo.getUsername());
            });
            records.forEach(record -> {
                articleAuthorMap.put(Long.valueOf(record.getId()), userMap.get(record.getUserId()));
            });

            try {
                records.forEach(record -> {
                    Long id = Long.valueOf(record.getId());
                    String author = articleAuthorMap.get(id);
                    record.setAuthor(author);
                });
            } catch (Exception e) {
                log.error(e.toString(), e);
                throw new BusinessException(ErrorEnum.COMMON_SERVER_ERROR);
            }
        }

        return articleList;
    }
}
