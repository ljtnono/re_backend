package cn.lingjiatong.re.service.article.api.client;

import org.springframework.cloud.openfeign.FeignClient;

/**
 * 后台文章模块feign客户端接口层
 *
 * @author Ling, Jiatong
 * Date: 2022/10/17 21:56
 */
@FeignClient(name = "re-service-article-server", path = "/article", contextId = "BackendArticleFeignClient")
public interface BackendArticleFeignClient {


}
