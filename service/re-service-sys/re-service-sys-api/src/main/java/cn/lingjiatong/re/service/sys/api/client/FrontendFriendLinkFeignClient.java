package cn.lingjiatong.re.service.sys.api.client;

import org.springframework.cloud.openfeign.FeignClient;

/**
 * 前端友情链接feign接口层
 *
 * @author Ling, Jiatong
 * Date: 2022/10/11 00:23
 */
@FeignClient(name = "re-service-sys-server", path = "/sys", contextId = "FrontendFriendLinkFeignClient")
public interface FrontendFriendLinkFeignClient {

}
