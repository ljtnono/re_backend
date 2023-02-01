package cn.lingjiatong.re.api.frontend.controller;

import cn.lingjiatong.re.common.EsPage;
import cn.lingjiatong.re.common.ResultVO;
import cn.lingjiatong.re.service.article.api.client.FrontendArticleFeignClient;
import cn.lingjiatong.re.service.article.api.dto.*;
import cn.lingjiatong.re.service.article.api.vo.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 前端博客文章模块接口
 *
 * @author Ling, Jiatong
 * Date: 2022/10/20 21:53
 */
@Slf4j
@RestController
@RequestMapping("/article")
@Tag(name = "前端博客文章模块接口")
public class ArticleController {

    @Autowired
    private FrontendArticleFeignClient frontendArticleFeignClient;

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
    @GetMapping("/{articleId:\\d+}")
    @Operation(summary = "获取单个文章信息", method = "GET")
    public ResultVO<FrontendArticleVO> findArticleById(@PathVariable("articleId") Long articleId) {
        log.info("==========获取单个文章信息，参数：{}", articleId);
        return frontendArticleFeignClient.findArticleById(articleId);
    }

    /**
     * 前端分页获取文章置顶列表
     *
     * @param dto 前端分页获取文章置顶列表DTO对象
     * @return 前端分页获取文章置顶列表VO对象分页对象
     */
    @GetMapping("/topList")
    @Operation(summary = "前端分页获取文章置顶列表", method = "GET")
    public ResultVO<Page<FrontendArticleTopListVO>> findArticleTopList(FrontendArticleTopListDTO dto) {
        log.info("==========前端分页获取文章置顶列表，参数：{}", dto);
        return frontendArticleFeignClient.findArticleTopList(dto);
    }

    /**
     * 前端滚动获取文章列表
     *
     * @param dto 前端文章无限滚动列表DTO对象
     * @return 前端文章无限滚动列表VO对象分页对象
     */
    @GetMapping("/scroll")
    @Operation(summary = "前端滚动获取文章列表", method = "GET")
    public ResultVO<Page<FrontendArticleScrollVO>> findArticleScroll(FrontendArticleScrollDTO dto) {
        log.info("==========前端滚动获取文章列表，参数：{}", dto);
        return frontendArticleFeignClient.findArticleScroll(dto);
    }

    /**
     * 前端分页获取推荐文章列表
     *
     * @param dto 前端推荐文章列表DTO对象
     * @return 前端推荐文章列表VO对象分页对象
     */
    @GetMapping("/recommendList")
    @Operation(summary = "前端分页获取推荐文章列表", method = "GET")
    public ResultVO<Page<FrontendArticleRecommendListVO>> findArticleRecommendList(FrontendArticleRecommendListDTO dto) {
        log.info("==========前端分页获取推荐文章列表，参数：{}", dto);
        return frontendArticleFeignClient.findArticleRecommendList(dto);
    }

    /**
     * 前端搜索文章列表
     *
     * @param dto 前端搜索文章列表DTO对象
     * @return 前端搜索文章列表VO对象分页对象
     */
    @GetMapping("/search")
    @Operation(summary = "前端搜索文章列表", method = "GET")
    public ResultVO<EsPage<FrontendArticleSearchListVO>> searchArticle(FrontendArticleSearchDTO dto) {
        log.info("==========前端搜索文章列表，参数：{}", dto);
        return frontendArticleFeignClient.search(dto);
    }

    /**
     * 前端分页获取文章列表
     *
     * @param dto 前端分页获取文章列表DTO对象
     * @return 前端分页获取文章列表VO对象
     */
    @GetMapping("/list")
    @Operation(summary = "前端分页获取文章列表", method = "GET")
    public ResultVO<IPage<FrontendArticleListVO>> findArticleList(FrontendArticleListDTO dto) {
        log.info("==========前端分页获取文章列表，参数：{}", dto);
        return frontendArticleFeignClient.findArticleList(dto);
    }
}
