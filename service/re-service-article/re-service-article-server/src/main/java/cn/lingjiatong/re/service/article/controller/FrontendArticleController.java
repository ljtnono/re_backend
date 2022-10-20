package cn.lingjiatong.re.service.article.controller;

import cn.lingjiatong.re.service.article.api.client.FrontendArticleFeignClient;
import cn.lingjiatong.re.service.article.api.vo.FrontendArticleVO;
import cn.lingjiatong.re.service.article.service.FrontendArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
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

    @Override
    @NonNull
    @GetMapping("/api/v1/article/{articleId}")
    public FrontendArticleVO findArticleById(@PathVariable("articleId") @NonNull Long articleId) {
        return frontendArticleService.findArticle(articleId);
    }
}
