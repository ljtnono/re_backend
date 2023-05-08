package cn.lingjiatong.re.service.sys.controller;

import cn.lingjiatong.re.common.ResultVO;
import cn.lingjiatong.re.common.entity.User;
import cn.lingjiatong.re.service.sys.api.client.BackendMenuFeignClient;
import cn.lingjiatong.re.service.sys.api.dto.BackendMenuListDTO;
import cn.lingjiatong.re.service.sys.api.dto.BackendMenuSaveDTO;
import cn.lingjiatong.re.service.sys.api.vo.BackendBreadcrumbListVO;
import cn.lingjiatong.re.service.sys.api.vo.BackendMenuListVO;
import cn.lingjiatong.re.service.sys.api.vo.BackendMenuTreeVO;
import cn.lingjiatong.re.service.sys.service.BackendMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 后台获取菜单树controller层
 *
 * @author Ling, Jiatong
 * Date: 3/26/23 10:22 PM
 */
@RestController
public class BackendMenuController implements BackendMenuFeignClient {

    @Autowired
    private BackendMenuService backendMenuService;

    // ********************************新增类接口********************************

    @Override
    @PostMapping("/backend/api/v1/menu/save")
    public ResultVO<?> saveMenu(@RequestBody BackendMenuSaveDTO dto, User currentUser) {
        backendMenuService.saveMenu(dto, currentUser);
        return ResultVO.success();
    }



    @Override
    @GetMapping("/backend/api/v1/menu/breadcrumbList")
    public ResultVO<List<BackendBreadcrumbListVO>> findBreadcrumbList(User currentUser) {
        return ResultVO.success(backendMenuService.findBreadcrumbList(currentUser));
    }

    // ********************************删除类接口********************************

    @Override
    @DeleteMapping("/backend/api/v1/menu/delete/{menuId}")
    public ResultVO<?> deleteMenu(@PathVariable("menuId") Long menuId, User currentUser) {
        backendMenuService.deleteMenu(menuId, currentUser);
        return ResultVO.success();
    }



    // ********************************修改类接口********************************
    // ********************************查询类接口********************************

    @Override
    @GetMapping("/backend/api/v1/menu/checkRouteNameDuplicate")
    public ResultVO<Boolean> checkRouteNameDuplicate(@RequestParam("routeName") String routeName, User currentUser) {
        routeName = routeName.split(",")[0];
        return ResultVO.success(backendMenuService.checkRouteNameDuplicate(routeName, currentUser));
    }

    @Override
    @GetMapping("/backend/api/v1/menu/checkRoutePathDuplicate")
    public ResultVO<Boolean> checkRoutePathDuplicate(@RequestParam("routePath") String routePath, User currentUser) {
        routePath = routePath.split(",")[0];
        return ResultVO.success(backendMenuService.checkRoutePathDuplicate(routePath, currentUser));
    }

    @Override
    @GetMapping("/backend/api/v1/menu/list")
    public ResultVO<List<BackendMenuListVO>> findMenuList(BackendMenuListDTO dto, User currentUser) {
        return ResultVO.success(backendMenuService.findMenuList(dto, currentUser));
    }

    @Override
    @GetMapping("/backend/api/v1/menu/tree")
    public ResultVO<List<BackendMenuTreeVO>> findBackendMenuTree(User currentUser) {
        return ResultVO.success(backendMenuService.findBackendMenuTree(currentUser));
    }


}
