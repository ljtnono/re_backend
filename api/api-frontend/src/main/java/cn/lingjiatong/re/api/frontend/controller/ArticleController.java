package cn.lingjiatong.re.api.frontend.controller;

import cn.lingjiatong.re.common.ResultVO;
import cn.lingjiatong.re.service.article.api.client.FrontendArticleFeignClient;
import cn.lingjiatong.re.service.article.api.vo.FrontendArticleVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 文章页面controller层
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

}
