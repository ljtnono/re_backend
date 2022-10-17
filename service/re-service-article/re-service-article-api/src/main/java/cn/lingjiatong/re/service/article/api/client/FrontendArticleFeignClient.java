package cn.lingjiatong.re.service.article.api.client;

import org.springframework.cloud.openfeign.FeignClient;

/**
 * 前端文章相关feign接口层
 *
 * @author Ling, Jiatong
 * Date: 2022/10/11 00:22
 */
@FeignClient(name = "re-service-article-server", path = "/article", contextId = "FrontendArticleFeignClient")
public interface FrontendArticleFeignClient {

}
