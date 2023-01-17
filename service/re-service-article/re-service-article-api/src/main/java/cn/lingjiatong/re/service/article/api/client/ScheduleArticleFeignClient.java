package cn.lingjiatong.re.service.article.api.client;

import cn.lingjiatong.re.common.config.FeignBasicAuthRequestInterceptor;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 文章定时任务feign客户端层接口
 *
 * @author Ling, Jiatong
 * Date: 2023/1/17 20:12
 */
@FeignClient(name = "re-service-article-server", path = "/article", contextId = "ScheduleArticleFeignClient", configuration = {FeignBasicAuthRequestInterceptor.class})
public interface ScheduleArticleFeignClient {
    // ********************************新增类接口********************************
    // ********************************删除类接口********************************
    // ********************************修改类接口********************************
    // ********************************查询类接口********************************

    /**
     * 同步文章列表到ES
     *
     * @see cn.lingjiatong.re.common.constant.DistributedTaskStatusEnum
     * @return 执行状态
     */
    @GetMapping("/schedule/api/v1/article/syncArticleToES")
    Integer syncArticleToES();
}
