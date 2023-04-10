package cn.lingjiatong.re.api.backend.controller;

import cn.lingjiatong.re.common.ResultVO;
import cn.lingjiatong.re.common.annotation.CurrentUser;
import cn.lingjiatong.re.common.entity.User;
import cn.lingjiatong.re.service.sys.api.client.BackendRouteFeignClient;
import cn.lingjiatong.re.service.sys.api.vo.BackendRouteListVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 后台路由模块controller层
 *
 * @author Ling, Jiatong
 * Date: 4/10/23 10:46 PM
 */
@Slf4j
@RestController
@RequestMapping("/route")
public class RouteController {

    @Autowired
    private BackendRouteFeignClient backendRouteFeignClient;

    // ********************************新增类接口********************************
    // ********************************删除类接口********************************
    // ********************************修改类接口********************************
    // ********************************查询类接口********************************

    /**
     * 后台获取路由列表
     *
     * @param currentUser 当前登陆用户
     * @return 路由列表
     */
    @GetMapping("/list")
    @Operation(description = "获取路由列表", method = "GET")
    @PreAuthorize("isAuthenticated()")
    public ResultVO<List<BackendRouteListVO>> findBackendRouteList(@Parameter(hidden = true) @CurrentUser User currentUser) {
        log.info("==========获取路由列表");
        return backendRouteFeignClient.findBackendRouteList(currentUser);
    }
}
