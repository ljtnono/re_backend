package cn.lingjiatong.re.service.sys.api.client;

import org.springframework.cloud.openfeign.FeignClient;

/**
 * 前端消息feign接口层
 *
 * @author Ling, Jiatong
 * Date: 2022/10/11 00:28
 */
@FeignClient(name = "re-service-sys-server", path = "/sys", contextId = "FrontendMessageFeignClient")
public interface FrontendMessageFeignClient {
}
