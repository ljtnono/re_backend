package cn.lingjiatong.re.api.backend.controller;

import cn.lingjiatong.re.common.ResultVO;
import cn.lingjiatong.re.common.annotation.PassToken;
import cn.lingjiatong.re.common.entity.User;
import cn.lingjiatong.re.service.sys.api.client.BackendRoleFeignClient;
import cn.lingjiatong.re.service.sys.api.dto.BackendRolePageListDTO;
import cn.lingjiatong.re.service.sys.api.vo.BackendRoleListVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    // ********************************删除类接口********************************
    // ********************************修改类接口********************************
    // ********************************查询类接口********************************

    /**
     * 后台获取角色列表
     *
     * @param currentUser 当前用户
     * @return 后台角色列表VO对象
     */
    @GetMapping("/list")
    @Operation(summary = "后台获取角色列表", method = "GET")
    @PreAuthorize("hasAuthority('system:role') || hasAuthority('system:role:read')")
    public ResultVO<List<BackendRoleListVO>> findRoleList(@Parameter(hidden = true) User currentUser) {
        log.info("==========获取角色列表");
        return backendRoleFeignClient.findRoleList(currentUser);
    }

    /**
     * 后台分页获取角色列表
     *
     * @param dto 后台分页获取角色列表DTO对象
     * @param currentUser 当前用户
     * @return 后台获取角色列表VO对象分页对象
     */
    @GetMapping("/pageList")
    @Operation(summary = "后台分页获取角色列表", method = "GET")
//    @PreAuthorize("hasAuthority('system:role') || hasAuthority('system:role:read')")
    @PassToken
    public ResultVO<Page<BackendRoleListVO>> findRoleList(BackendRolePageListDTO dto, @Parameter(hidden = true) User currentUser) {
        log.info("==========后台分页获取角色列表，参数：{}", dto);
        return backendRoleFeignClient.findRolePageList(dto, currentUser);
    }


    // ********************************私有函数********************************
    // ********************************公用函数********************************
}
