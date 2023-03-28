package cn.lingjiatong.re.service.sys.controller;

import cn.lingjiatong.re.common.ResultVO;
import cn.lingjiatong.re.common.entity.User;
import cn.lingjiatong.re.service.sys.api.client.BackendRoleFeignClient;
import cn.lingjiatong.re.service.sys.api.dto.BackendRoleDeleteBatchDTO;
import cn.lingjiatong.re.service.sys.api.dto.BackendRolePageListDTO;
import cn.lingjiatong.re.service.sys.api.dto.BackendRoleSaveDTO;
import cn.lingjiatong.re.service.sys.api.vo.BackendRoleListVO;
import cn.lingjiatong.re.service.sys.api.vo.BackendRoleMenuTreeVO;
import cn.lingjiatong.re.service.sys.service.BackendRoleService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @Override
    @PostMapping("/backend/api/v1/role/save")
    public ResultVO<?> saveRole(@RequestBody BackendRoleSaveDTO dto, User currentUser) {
        backendRoleService.saveRole(dto, currentUser);
        return ResultVO.success();
    }


    // ********************************删除类接口********************************

    @Override
    @DeleteMapping("/backend/api/v1/role/deleteBatch")
    public ResultVO<?> deleteRoleBatch(@RequestBody BackendRoleDeleteBatchDTO dto, User currentUser) {
        backendRoleService.deleteRoleBatch(dto, currentUser);
        return ResultVO.success();
    }

    // ********************************修改类接口********************************
    // ********************************查询类接口********************************


    @Override
    @GetMapping("/backend/api/v1/role/list")
    public ResultVO<List<BackendRoleListVO>> findRoleList(User currentUser) {
        return ResultVO.success(backendRoleService.findRoleList(currentUser));
    }

    @Override
    @GetMapping("/backend/api/v1/role/pageList")
    public ResultVO<Page<BackendRoleListVO>> findRolePageList(BackendRolePageListDTO dto, User currentUser) {
        return ResultVO.success(backendRoleService.findRolePageList(dto, currentUser));
    }

    @Override
    @GetMapping("/backend/api/v1/role/menuTree/{roleId}")
    public ResultVO<BackendRoleMenuTreeVO> findRoleMenuTree(@PathVariable("roleId") Long roleId, User currentUser) {
        return ResultVO.success(backendRoleService.findRoleMenuTree(roleId, currentUser));
    }


    // ********************************私有函数********************************
    // ********************************公用函数********************************
}
