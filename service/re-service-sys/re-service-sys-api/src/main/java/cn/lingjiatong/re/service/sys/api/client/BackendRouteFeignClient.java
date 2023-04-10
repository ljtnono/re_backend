package cn.lingjiatong.re.service.sys.api.client;

import cn.lingjiatong.re.common.ResultVO;
import cn.lingjiatong.re.common.config.FeignBasicAuthRequestInterceptor;
import cn.lingjiatong.re.common.entity.User;
import cn.lingjiatong.re.service.sys.api.vo.BackendRouteListVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * 后台路由模块feign客户端接口
 *
 * @author Ling, Jiatong
 * Date: 4/10/23 10:39 PM
 */
@FeignClient(value = "re-service-sys-server", path = "/sys", contextId = "BackendRouteFeignClient", configuration = {FeignBasicAuthRequestInterceptor.class})
public interface BackendRouteFeignClient {

    // ********************************新增类接口********************************
    // ********************************删除类接口********************************
    // ********************************修改类接口********************************
    // ********************************查询类接口********************************

    /**
     * 获取后台前端所有的路由列表
     *
     * @param currentUser 当前登陆用户
     * @return 前端路由列表VO对象列表
     */
    @GetMapping("/backend/api/v1/route/list")
    ResultVO<List<BackendRouteListVO>> findBackendRouteList(@SpringQueryMap User currentUser);

}
