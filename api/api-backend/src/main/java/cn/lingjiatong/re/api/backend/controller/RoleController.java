package cn.lingjiatong.re.api.backend.controller;

import cn.lingjiatong.re.common.ResultVO;
import cn.lingjiatong.re.service.sys.api.client.BackendRoleFeignClient;
import cn.lingjiatong.re.service.sys.api.dto.*;
import cn.lingjiatong.re.service.sys.api.vo.BackendRoleListVO;
import cn.lingjiatong.re.service.sys.api.vo.BackendRoleMenuTreeVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 后台角色模块接口
 *
 * @author Ling, Jiatong
 * Date: 2023/3/3 15:03
 */
@Slf4j
@RestController
@RequestMapping("/role")
@Tag(name = "后台角色模块接口")
public class RoleController {

    @Autowired
    private BackendRoleFeignClient backendRoleFeignClient;

    // ********************************新增类接口********************************

    /**
     * 后台新增角色
     *
     * @param dto 后台新增角色DTO对象
     * @return 通用消息返回对象
     */
    @PostMapping
    @Operation(summary = "后台新增角色", method = "POST")
//    @PreAuthorize("hasAuthority('system:role:write')")
    public ResultVO<?> saveRole(@RequestBody BackendRoleSaveDTO dto) {
        log.info("==========后台新增角色，参数：{}", dto);
        return backendRoleFeignClient.saveRole(dto);
    }

    // ********************************删除类接口********************************

    /**
     * 后台批量删除角色
     *
     * @param dto 后台批量删除角色DTO对象
     * @return 通用消息返回对象
     */
    @DeleteMapping("/deleteBatch")
    @Operation(summary = "后台批量删除角色", method = "DELETE")
//    @PreAuthorize("hasAuthority('system:role:write')")
    public ResultVO<?> deleteRoleBatch(@RequestBody BackendRoleDeleteBatchDTO dto) {
        log.info("==========后台批量删除角色，参数：{}", dto);
        return backendRoleFeignClient.deleteRoleBatch(dto);
    }


    // ********************************修改类接口********************************

    /**
     * 后台修改角色
     *
     * @param dto 后台修改角色
     * @return 通用消息对象
     */
    @PutMapping
    @Operation(summary = "后台修改角色", method = "PUT")
//    @PreAuthorize("hasAuthority('system:role:write')")
    public ResultVO<?> updateRole(@RequestBody BackendRoleUpdateDTO dto) {
        log.info("==========后台修改角色，参数：{}", dto);
        return backendRoleFeignClient.updateRole(dto);
    }


    // ********************************查询类接口********************************

    /**
     * 新增角色表单角色名称校验
     *
     * @param roleName 角色名称
     * @return 可用返回true, 不可用返回false
     */
    @GetMapping("/addFormRoleNameCheck")
    @Operation(summary = "新增角色表单名称校验", method = "GET")
//    @PreAuthorize("hasAuthority('system:role') || hasAuthority('system:role:read')")
    public ResultVO<Boolean> addFormRoleNameCheck(String roleName) {
        log.info("==========新增角色表单名称校验，参数：{}", roleName);
        return backendRoleFeignClient.addFormRoleNameCheck(roleName);
    }

    /**
     * 编辑角色表单角色名称校验
     *
     * @param dto 后台角色名称校验DTO对象
     * @return 可用返回true，不可用返回false
     */
    @GetMapping("/editFormRoleNameCheck")
    @Operation(summary = "编辑角色表单角色名称校验", method = "GET")
//    @PreAuthorize("hasAuthority('system:role') || hasAuthority('system:role:read')")
    public ResultVO<Boolean> editFormRoleNameCheck(BackendRoleNameCheckDTO dto) {
        log.info("==========编辑角色表单名称校验，参数：{}", dto);
        return backendRoleFeignClient.editFormRoleNameCheck(dto);
    }

    /**
     * 后台获取角色列表
     * 这里只需要角色登录即可
     *
     * @return 后台角色列表VO对象
     */
    @GetMapping("/list")
    @Operation(summary = "后台获取角色列表", method = "GET")
//    @PreAuthorize("isAuthenticated()")
    public ResultVO<List<BackendRoleListVO>> findRoleList() {
        log.info("==========获取角色列表");
        return backendRoleFeignClient.findRoleList();
    }

    /**
     * 后台分页获取角色列表
     *
     * @param dto 后台分页获取角色列表DTO对象
     * @return 后台获取角色列表VO对象分页对象
     */
    @GetMapping("/pageList")
    @Operation(summary = "后台分页获取角色列表", method = "GET")
//    @PreAuthorize("hasAuthority('system:role') || hasAuthority('system:role:read')")
//    @PassToken
    public ResultVO<Page<BackendRoleListVO>> findRoleList(BackendRolePageListDTO dto) {
        log.info("==========后台分页获取角色列表，参数：{}", dto);
        return backendRoleFeignClient.findRolePageList(dto);
    }

    /**
     * 后台获取角色菜单树
     *
     * @param roleId 角色id
     * @return 角色菜单树VO对象
     */
    @GetMapping("/menuTree/{roleId}")
    @Operation(summary = "后台获取角色菜单树", method = "GET")
//    @PreAuthorize("hasAuthority('system:role') || hasAuthority('system:role:read')")
    public ResultVO<BackendRoleMenuTreeVO> findRoleMenuTree(@PathVariable("roleId") Long roleId) {
        log.info("==========后台获取角色菜单树，参数：{}", roleId);
        return backendRoleFeignClient.findRoleMenuTree(roleId);
    }

    // ********************************私有函数********************************
    // ********************************公用函数********************************
}
