package cn.lingjiatong.re.api.backend.controller;

import cn.lingjiatong.re.common.ResultVO;
import cn.lingjiatong.re.common.annotation.CurrentUser;
import cn.lingjiatong.re.common.entity.User;
import cn.lingjiatong.re.service.sys.api.client.BackendUserFeignClient;
import cn.lingjiatong.re.service.sys.api.dto.BackendUserListDTO;
import cn.lingjiatong.re.service.sys.api.dto.BackendUserSaveDTO;
import cn.lingjiatong.re.service.sys.api.vo.BackendUserListVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
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
     * @param currentUser 当前用户
     * @return 通用消息返回对象
     */
    @PostMapping
    @PreAuthorize("hasAuthority('system:user:write')")
    @Operation(summary = "新增用户", method = "POST")
    public ResultVO<?> saveUser(@RequestBody BackendUserSaveDTO dto, @Parameter(hidden = true) @CurrentUser User currentUser) {
        log.info("==========新增用户，参数：{}", dto);
        return backendUserFeignClient.saveUser(dto, currentUser);
    }

    // ********************************删除类接口********************************
    // ********************************修改类接口********************************
    // ********************************查询类接口********************************

    /**
     * 后端获取文章标签列表
     *
     * @param currentUser 当前登录用户
     * @return 后端获取文章标签列表VO对象列表
     */
    @GetMapping("/list")
    @Operation(summary = "后端获取用户列表", method = "GET")
    @PreAuthorize("hasAuthority('system:user') || hasAuthority('system:user:read')")
    public ResultVO<Page<BackendUserListVO>> findTagList(BackendUserListDTO dto, @Parameter(hidden = true) @CurrentUser User currentUser) {
        log.info("==========后端获取文章标签列表");
        return backendUserFeignClient.findUserList(dto, currentUser);
    }

    /**
     * 校验用户名是否重复
     *
     * @param username 用户名
     * @param currentUser 当前用户
     * @return 重复返回true，不重复返回false
     */
    @GetMapping("/testUsernameDuplicate")
    @Operation(summary = "校验用户名是否重复", method = "GET")
    @PreAuthorize("hasAuthority('system:user') || hasAuthority('system:user:read')")
    public ResultVO<Boolean> testUsernameDuplicate(String username, @Parameter(hidden = true) @CurrentUser User currentUser) {
        log.info("==========校验用户名是否重复，参数：{}", username);
        return backendUserFeignClient.testUsernameDuplicate(username, currentUser);
    }

    // ********************************私有函数********************************
    // ********************************公用函数********************************
}
