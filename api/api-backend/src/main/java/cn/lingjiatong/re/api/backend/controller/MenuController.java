package cn.lingjiatong.re.api.backend.controller;

import cn.lingjiatong.re.common.ResultVO;
import cn.lingjiatong.re.common.annotation.CurrentUser;
import cn.lingjiatong.re.common.entity.User;
import cn.lingjiatong.re.service.sys.api.client.BackendMenuFeignClient;
import cn.lingjiatong.re.service.sys.api.dto.BackendMenuListDTO;
import cn.lingjiatong.re.service.sys.api.dto.BackendMenuSaveDTO;
import cn.lingjiatong.re.service.sys.api.vo.BackendBreadcrumbListVO;
import cn.lingjiatong.re.service.sys.api.vo.BackendMenuListVO;
import cn.lingjiatong.re.service.sys.api.vo.BackendMenuTreeVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 后台菜单模块接口
 *
 * @author Ling, Jiatong
 * Date: 3/26/23 10:39 PM
 */
@Slf4j
@RestController
@RequestMapping("/menu")
@Tag(name = "后台菜单模块接口")
public class MenuController {

    @Autowired
    private BackendMenuFeignClient backendMenuFeignClient;

    // ********************************新增类接口********************************

    /**
     * 后台新增菜单
     *
     * @param dto 后台新增菜单DTO对象
     * @param currentUser 当前登陆用户
     * @return 通用消息返回对象
     */
    @PostMapping
    @PreAuthorize("isAuthenticated()")
    @Operation(description = "后台新增菜单", method = "POST")
    public ResultVO<?> saveMenu(@RequestBody BackendMenuSaveDTO dto, @Parameter(hidden = true) @CurrentUser User currentUser) {
        log.info("==========后台新增菜单，参数：{}", dto);
        return backendMenuFeignClient.saveMenu(dto, currentUser);
    }

    // ********************************删除类接口********************************

    /**
     * 后台删除菜单
     *
     * @param menuId 菜单id
     * @param currentUser 当前登录用户
     * @return 通用消息返回对象
     */
    @DeleteMapping("/{menuId}")
    @PreAuthorize("isAuthenticated()")
    @Operation(description = "后台删除菜单", method = "DELETE")
    public ResultVO<?> deleteMenu(@PathVariable Long menuId, @Parameter(hidden = true) @CurrentUser User currentUser) {
        log.info("==========后台删除菜单：，参数：{}", menuId);
        return backendMenuFeignClient.deleteMenu(menuId, currentUser);
    }

    // ********************************修改类接口********************************



    // ********************************查询类接口********************************

    /**
     * 后台获取面包屑导航列表
     *
     * @param currentUser 当前登陆用户
     * @return 面包屑导航VO对象列表
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/breadcrumbList")
    @Operation(description = "后台获取面包屑导航列表", method = "GET")
    public ResultVO<List<BackendBreadcrumbListVO>> findBreadcrumbList(@Parameter(hidden = true) @CurrentUser User currentUser) {
        log.info("==========后台获取面包屑导航");
        return backendMenuFeignClient.findBreadcrumbList(currentUser);
    }


    /**
     * 后台获取菜单树
     *
     * @param currentUser 当前登陆用户
     * @return 后台获取菜单树VO对象列表
     */
    @GetMapping("/tree")
    @Operation(description = "后台获取菜单树", method = "GET")
    @PreAuthorize("isAuthenticated()")
    public ResultVO<List<BackendMenuTreeVO>> findMenuTree(@Parameter(hidden = true) @CurrentUser User currentUser) {
        log.info("==========后台获取菜单树");
        return backendMenuFeignClient.findBackendMenuTree(currentUser);
    }

    /**
     * 后台分获取菜单列表
     *
     * @param dto 后台分页获取菜单列表DTO对象
     * @param currentUser 当前登陆用户
     * @return 后台分页获取菜单列表VO对象
     */
    @GetMapping("/list")
    @PreAuthorize("isAuthenticated()")
    @Operation(description = "后台获取菜单列表", method = "GET")
    public ResultVO<List<BackendMenuListVO>> findMenuList(BackendMenuListDTO dto, @Parameter(hidden = true) @CurrentUser User currentUser) {
        log.info("==========后台获取菜单列表，参数：{}", dto);
        return backendMenuFeignClient.findMenuList(dto, currentUser);
    }
}
