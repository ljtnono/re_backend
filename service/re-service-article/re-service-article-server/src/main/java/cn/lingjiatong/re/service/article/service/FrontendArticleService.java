package cn.lingjiatong.re.service.article.service;

import cn.lingjiatong.re.common.ResultVO;
import cn.lingjiatong.re.common.constant.CommonConstant;
import cn.lingjiatong.re.common.constant.UserConstant;
import cn.lingjiatong.re.common.exception.BusinessException;
import cn.lingjiatong.re.common.exception.ErrorEnum;
import cn.lingjiatong.re.common.exception.ResourceNotExistException;
import cn.lingjiatong.re.service.article.api.dto.FrontendArticleRecommendListDTO;
import cn.lingjiatong.re.service.article.api.dto.FrontendArticleScrollDTO;
import cn.lingjiatong.re.service.article.api.dto.FrontendArticleTopListDTO;
import cn.lingjiatong.re.service.article.api.vo.FrontendArticleRecommendListVO;
import cn.lingjiatong.re.service.article.api.vo.FrontendArticleScrollVO;
import cn.lingjiatong.re.service.article.api.vo.FrontendArticleTopListVO;
import cn.lingjiatong.re.service.article.api.vo.FrontendArticleVO;
import cn.lingjiatong.re.service.article.constant.FrontendArticleErrorMessageConstant;
import cn.lingjiatong.re.service.article.entity.Article;
import cn.lingjiatong.re.service.article.entity.Category;
import cn.lingjiatong.re.service.article.mapper.ArticleMapper;
import cn.lingjiatong.re.service.article.mapper.CategoryMapper;
import cn.lingjiatong.re.service.article.mapper.TagMapper;
import cn.lingjiatong.re.service.sys.api.client.FrontendUserFeignClient;
import cn.lingjiatong.re.service.sys.api.vo.FrontendUserListVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
}
