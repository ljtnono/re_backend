package cn.lingjiatong.re.service.sys.api.client;

import cn.lingjiatong.re.common.ResultVO;
import cn.lingjiatong.re.common.config.FeignBasicAuthRequestInterceptor;
import cn.lingjiatong.re.service.sys.api.vo.FrontendUserListVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 前端用户模块feign客户端层
 *
 * @author Ling, Jiatong
 * Date: 2023/1/17 10:38
 */
@FeignClient(value = "re-service-sys-server", path = "/sys", contextId = "FrontendUserFeignClient", configuration = {FeignBasicAuthRequestInterceptor.class})
public interface FrontendUserFeignClient {

    // ********************************新增类接口********************************
    // ********************************删除类接口********************************
    // ********************************修改类接口********************************
    // ********************************查询类接口********************************

    /**
     * 根据用户id列表获取用户列表
     *
     * @param userIdList 用户id列表
     * @return 前端获取用户列表VO对象列表
     */
    @GetMapping("/frontend/api/v1/user/findUserByUserIdList")
    ResultVO<List<FrontendUserListVO>> findUserListByUserIdList(@RequestParam("userIdList") List<Long> userIdList);

}
