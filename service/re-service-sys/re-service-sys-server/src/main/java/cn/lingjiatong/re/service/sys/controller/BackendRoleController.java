package cn.lingjiatong.re.service.sys.controller;

import cn.lingjiatong.re.common.ResultVO;
import cn.lingjiatong.re.common.entity.User;
import cn.lingjiatong.re.service.sys.api.client.BackendRoleFeignClient;
import cn.lingjiatong.re.service.sys.api.vo.BackendRoleListVO;
import cn.lingjiatong.re.service.sys.service.BackendRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 后端角色模块controller层
 *
 * @author Ling, Jiatong
 * Date: 2023/3/3 14:34
 */
@RestController
public class BackendRoleController implements BackendRoleFeignClient {

    @Autowired
    private BackendRoleService backendRoleService;

    // ********************************新增类接口********************************
    // ********************************删除类接口********************************
    // ********************************修改类接口********************************
    // ********************************查询类接口********************************

    @Override
    @GetMapping("/backend/api/v1/role/list")
    public ResultVO<List<BackendRoleListVO>> findRoleList(User currentUser) {
        return ResultVO.success(backendRoleService.findRoleList(currentUser));
    }


    // ********************************私有函数********************************
    // ********************************公用函数********************************
}
