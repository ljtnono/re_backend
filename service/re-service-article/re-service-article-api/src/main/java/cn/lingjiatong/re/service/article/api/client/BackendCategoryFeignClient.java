package cn.lingjiatong.re.service.article.api.client;

import org.springframework.cloud.openfeign.FeignClient;

/**
 * 后台文章分类模块feign客户端接口层
 *
 * @author Ling, Jiatong
 * Date: 2023/1/2 20:12
 */
@FeignClient(name = "re-service-article-server", path = "/article", contextId = "BackendCategoryFeignClient")
public interface BackendCategoryFeignClient {

    // ********************************新增类接口********************************
    // ********************************删除类接口********************************
    // ********************************修改类接口********************************
    // ********************************查询类接口********************************

}
