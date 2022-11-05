package cn.lingjiatong.re.service.sys.api.client;

import cn.lingjiatong.re.common.ResultVO;
import cn.lingjiatong.re.service.sys.api.config.FeignBasicAuthRequestInterceptor;
import cn.lingjiatong.re.service.sys.api.vo.FrontendFriendLinkListVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * 前端友情链接feign接口层
 *
 * @author Ling, Jiatong
 * Date: 2022/10/11 00:23
 */
@FeignClient(name = "re-service-sys-server", path = "/sys", contextId = "FrontendFriendLinkFeignClient", configuration = {FeignBasicAuthRequestInterceptor.class})
public interface FrontendFriendLinkFeignClient {


    /**
     * 获取前端友情链接列表
     *
     * @return 前端友情链接列表VO对象列表
     */
    @GetMapping("/frontend/api/v1/friendLink/list")
    @ApiOperation(value = "获取前端友情链接列表", httpMethod = "GET")
    ResultVO<List<FrontendFriendLinkListVO>> findFrontendNoticeList();

}
