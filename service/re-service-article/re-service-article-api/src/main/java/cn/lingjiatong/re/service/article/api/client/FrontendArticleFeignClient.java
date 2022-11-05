package cn.lingjiatong.re.service.article.api.client;

import cn.lingjiatong.re.common.ResultVO;
import cn.lingjiatong.re.service.article.api.vo.FrontendArticleVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 前端文章相关feign接口层
 *
 * @author Ling, Jiatong
 * Date: 2022/10/11 00:22
 */
@FeignClient(name = "re-service-article-server", path = "/article", contextId = "FrontendArticleFeignClient")
public interface FrontendArticleFeignClient {


    /**
     * 获取单个文章信息
     *
     * @param articleId 文章id
     * @return 单个文章信息VO对象
     */
    @NonNull
    @GetMapping("/frontend/api/v1/article/{articleId}")
    ResultVO<FrontendArticleVO> findArticleById(@PathVariable("articleId") @NonNull Long articleId);

}
