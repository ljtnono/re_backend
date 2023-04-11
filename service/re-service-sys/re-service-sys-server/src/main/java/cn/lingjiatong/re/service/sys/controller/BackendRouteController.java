package cn.lingjiatong.re.service.sys.controller;

import cn.lingjiatong.re.common.ResultVO;
import cn.lingjiatong.re.common.annotation.PassToken;
import cn.lingjiatong.re.common.constant.CommonConstant;
import cn.lingjiatong.re.common.entity.User;
import cn.lingjiatong.re.service.sys.api.client.BackendRouteFeignClient;
import cn.lingjiatong.re.service.sys.api.vo.BackendRouteListVO;
import cn.lingjiatong.re.service.sys.service.BackendRouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 后台路由模块controller层
 *
 * @author Ling, Jiatong
 * Date: 4/10/23 10:40 PM
 */
@RestController
public class BackendRouteController implements BackendRouteFeignClient {

    @Autowired
    private BackendRouteService backendRouteService;

    // ********************************新增类接口********************************
    // ********************************删除类接口********************************
    // ********************************修改类接口********************************
    // ********************************查询类接口********************************

    @Override
    @PassToken
    @GetMapping("/backend/api/v1/route/list")
    public ResultVO<List<BackendRouteListVO>> findBackendRouteList() {
        return ResultVO.success(backendRouteService.findBackendRouteList(CommonConstant.PROJECT_NAME_BACKEND_PAGE));
    }
}
