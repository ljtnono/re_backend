package cn.lingjiatong.re.service.sys.api.client;

import cn.lingjiatong.re.common.config.FeignBasicAuthRequestInterceptor;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 用户登陆日志feign客户端
 *
 * @author Ling, Jiatong
 * Date: 3/22/23 10:39 PM
 */
@FeignClient(value = "re-service-sys-server", path = "/sys", contextId = "ScheduleUserLoginLogFeignClient", configuration = {FeignBasicAuthRequestInterceptor.class})
public interface ScheduleUserLoginLogFeignClient {

    // ********************************新增类接口********************************
    // ********************************删除类接口********************************
    // ********************************修改类接口********************************
    // ********************************查询类接口********************************



    // ********************************其他微服务调用********************************

    /**
     * 定期删除用户登陆日志
     */
    @GetMapping("/schedule/api/v1/article/syncArticleToES")
    void deleteUserLoginLogSchedule();

}

