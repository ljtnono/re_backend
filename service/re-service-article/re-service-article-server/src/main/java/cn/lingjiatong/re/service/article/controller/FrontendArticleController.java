package cn.lingjiatong.re.service.article.controller;

import cn.lingjiatong.re.common.EsPage;
import cn.lingjiatong.re.common.ResultVO;
import cn.lingjiatong.re.service.article.api.client.FrontendArticleFeignClient;
import cn.lingjiatong.re.service.article.api.dto.*;
import cn.lingjiatong.re.service.article.api.vo.*;
import cn.lingjiatong.re.service.article.service.FrontendArticleService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * 前端文章模块controller层
 *
 * @author Ling, Jiatong
 * Date: 2022/10/20 21:38
 */
@RestController
public class FrontendArticleController implements FrontendArticleFeignClient {

    @Autowired
    private FrontendArticleService frontendArticleService;

    // ********************************新增类接口********************************
    // ********************************删除类接口********************************
    // ********************************修改类接口********************************
    // ********************************查询类接口********************************

    @Override
    @GetMapping("/frontend/api/v1/article/scroll")
    public ResultVO<Page<FrontendArticleScrollVO>> findArticleScroll(FrontendArticleScrollDTO dto) {
        return ResultVO.success(frontendArticleService.findArticleScroll(dto));
    }

    @Override
    @GetMapping("/frontend/api/v1/article/{articleId}")
    public ResultVO<FrontendArticleVO> findArticleById(@PathVariable("articleId") Long articleId) {
        return ResultVO.success(frontendArticleService.findArticle(articleId));
    }

    @Override
    @GetMapping("/frontend/api/v1/article/topList")
    public ResultVO<Page<FrontendArticleTopListVO>> findArticleTopList(FrontendArticleTopListDTO dto) {
        return ResultVO.success(frontendArticleService.findArticleTopList(dto));
    }

    @Override
    @GetMapping("/frontend/api/v1/article/recommendList")
    public ResultVO<Page<FrontendArticleRecommendListVO>> findArticleRecommendList(FrontendArticleRecommendListDTO dto) {
        return ResultVO.success(frontendArticleService.findArticleRecommendList(dto));
    }

    @Override
    @GetMapping("/frontend/api/v1/article/search")
    public ResultVO<EsPage<FrontendArticleSearchListVO>> search(FrontendArticleSearchDTO dto) {
        return ResultVO.success(frontendArticleService.search(dto));
    }

    @Override
    @GetMapping("/frontend/api/v1/article/list")
    public ResultVO<IPage<FrontendArticleListVO>> findArticleList(FrontendArticleListDTO dto) {
        return ResultVO.success(frontendArticleService.findArticleList(dto));
    }

}
