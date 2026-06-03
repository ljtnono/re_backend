package cn.lingjiatong.re.api.backend.controller;

import cn.lingjiatong.re.common.ResultVO;
import cn.lingjiatong.re.service.sys.api.client.BackendUserFeignClient;
import cn.lingjiatong.re.service.sys.api.dto.*;
import cn.lingjiatong.re.service.sys.api.vo.BackendUserListVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 后端管理系统User模块接口
 *
 * @author Ling, Jiatong
 * Date: 2023/2/26 17:03
 */
@Slf4j
@RestController
@RequestMapping("/user")
@Tag(name = "后端管理系统User模块接口")
public class UserController {

    @Autowired
    private BackendUserFeignClient backendUserFeignClient;

    // ********************************新增类接口********************************

    /**
     * 新增用户
     *
     * @param dto 后台保存用户DTO对象
     * @return 通用消息返回对象
     */
    @PostMapping
//    @PreAuthorize("hasAuthority('system:user:write')")
    @Operation(summary = "新增用户", method = "POST")
    public ResultVO<?> saveUser(@RequestBody BackendUserSaveDTO dto) {
        log.info("==========新增用户，参数：{}", dto);
        return backendUserFeignClient.saveUser(dto);
    }

    // ********************************删除类接口********************************

    @DeleteMapping("/deleteUserBatch")
//    @PreAuthorize("hasAuthority('system:user:write')")
    @Operation(summary = "批量删除用户", method = "DELETE")
    public ResultVO<?> deleteUserBatch(@RequestBody BackendUserPhysicDeleteBatchDTO dto) {
        log.info("==========批量删除用户，参数：{}", dto);
        return backendUserFeignClient.deleteUserBatch(dto);
    }

    // ********************************修改类接口********************************

    /**
     * 批量更新用户状态
     *
     * @param dto 后台批量更改用户删除状态DTO对象
     * @return 通用消息返回对象
     */
    @PutMapping("/updateUserDeleteStatusBatch")
//    @PreAuthorize("hasAuthority('system:user:write')")
    @Operation(summary = "批量更新用户删除状态", method = "PUT")
    public ResultVO<?> updateUserDeleteStatusBatch(@RequestBody BackendUserUpdateDeleteStatusBatchDTO dto) {
        log.info("==========批量更新用户删除状态，参数：{}", dto);
        return backendUserFeignClient.updateUserDeleteStatusBatch(dto);
    }


    /**
     * 超级管理员编辑用户信息
     *
     * @param dto 后台管理员编辑用户信息DTO对象
     * @return 通用消息返回对象
     */
    @PutMapping("/adminEditUser")
//    @PreAuthorize("hasAuthority('system:user:write')")
    @Operation(summary = "超级管理员编辑用户信息", method = "PUT")
    public ResultVO<?> adminEditUser(@RequestBody BackendAdminUpdateUserDTO dto) {
        log.info("==========超级管理员编辑用户信息，参数：{}", dto);
        return backendUserFeignClient.adminEditUser(dto);
    }

    // ********************************查询类接口********************************

    /**
     * 后端获取文章标签列表
     *
     * @return 后端获取文章标签列表VO对象列表
     */
    @GetMapping("/list")
    @Operation(summary = "后端获取用户列表", method = "GET")
//    @PreAuthorize("hasAuthority('system:user') || hasAuthority('system:user:read')")
    public ResultVO<Page<BackendUserListVO>> findTagList(BackendUserListDTO dto) {
        log.info("==========后端获取文章标签列表");
        return backendUserFeignClient.findUserList(dto);
    }

    /**
     * 校验用户名是否重复
     *
     * @param username 用户名
     * @return 重复返回true，不重复返回false
     */
    @GetMapping("/testUsernameDuplicate")
    @Operation(summary = "校验用户名是否重复", method = "GET")
//    @PreAuthorize("hasAuthority('system:user') || hasAuthority('system:user:read')")
    public ResultVO<Boolean> testUsernameDuplicate(String username) {
        log.info("==========校验用户名是否重复，参数：{}", username);
        return backendUserFeignClient.testUsernameDuplicate(username);
    }

    /**
     * 校验邮箱是否重复
     *
     * @param email 邮箱
     * @return 重复返回true，不重复返回false
     */
    @GetMapping("/testEmailDuplicate")
    @Operation(summary = "测试邮箱是否重复", method = "GET")
//    @PreAuthorize("hasAuthority('system:user') || hasAuthority('system:user:read')")
    public ResultVO<Boolean> testEmailDuplicate(String email) {
        log.info("==========测试邮箱是否重复，参数：{}", email);
        return backendUserFeignClient.testEmailDuplicate(email);
    }

    /**
     * 管理员编辑用户表单测试邮箱是否可用接口
     *
     * @param dto 后台管理员用户编辑用户信息表单测试邮箱是否可用DTO对象
     * @return 可用返回true，不可用返回false
     */
    @GetMapping("/adminEditUserTestEmailAvailability")
    @Operation(summary = "管理员编辑用户表单测试邮箱是否可用接口", method = "GET")
//    @PreAuthorize("hasAuthority('system:user') || hasAuthority('system:user:read')")
    public ResultVO<Boolean> adminEditUserTestEmailAvailability(BackendAdminEditUserEmailTestAvailabilityDTO dto) {
        log.info("==========管理员编辑用户表单测试邮箱是否可用接口，参数：{}", dto);
        return backendUserFeignClient.adminEditUserTestEmailAvailability(dto);
    }

    // ********************************私有函数********************************
    // ********************************公用函数********************************
}
