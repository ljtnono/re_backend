package cn.lingjiatong.re.api.frontend.controller;

import cn.lingjiatong.re.common.ResultVO;
import cn.lingjiatong.re.service.article.api.client.FrontendArticleFeignClient;
import cn.lingjiatong.re.service.article.api.vo.FrontendArticleVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

/**
 * 文章页面controller层
 *
 * @author Ling, Jiatong
 * Date: 2022/10/20 21:53
 */
@Slf4j
@RestController
@RequestMapping("/article")
public class ArticleController {

    @Autowired
    private FrontendArticleFeignClient frontendArticleFeignClient;


    /**
     * 获取单个文章信息
     *
     * @param articleId 文章id
     * @return 单个文章信息VO对象
     */
    @NonNull
    @GetMapping("/{articleId:\\d+}")
    public ResultVO<FrontendArticleVO> findArticleById(@PathVariable("articleId") @NonNull Long articleId) {
        log.info("==========获取单个文章信息，参数：{}", articleId);
        return frontendArticleFeignClient.findArticleById(articleId);
    }

}
