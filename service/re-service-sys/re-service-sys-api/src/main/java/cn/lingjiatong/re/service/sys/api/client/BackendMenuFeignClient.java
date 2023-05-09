package cn.lingjiatong.re.service.sys.api.client;

import cn.lingjiatong.re.common.ResultVO;
import cn.lingjiatong.re.common.config.FeignBasicAuthRequestInterceptor;
import cn.lingjiatong.re.common.entity.User;
import cn.lingjiatong.re.service.sys.api.dto.BackendMenuEditDTO;
import cn.lingjiatong.re.service.sys.api.dto.BackendMenuListDTO;
import cn.lingjiatong.re.service.sys.api.dto.BackendMenuSaveDTO;
import cn.lingjiatong.re.service.sys.api.vo.BackendBreadcrumbListVO;
import cn.lingjiatong.re.service.sys.api.vo.BackendMenuListVO;
import cn.lingjiatong.re.service.sys.api.vo.BackendMenuTreeVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 后台菜单模块feign客户端接口
 *
 * @author Ling, Jiatong
 * Date: 3/26/23 10:20 PM
 */
@FeignClient(value = "re-service-sys-server", path = "/sys", contextId = "BackendMenuFeignClient", configuration = {FeignBasicAuthRequestInterceptor.class})
public interface BackendMenuFeignClient {

    // ********************************新增类接口********************************

    /**
     * 后台新增菜单
     *
     * @param dto 后台新增菜单DTO对象
     * @param currentUser 当前登录用户
     * @return 通用消息返回对象
     */
    @PostMapping("/backend/api/v1/menu/save")
    ResultVO<?> saveMenu(@RequestBody BackendMenuSaveDTO dto, @SpringQueryMap User currentUser);

    // ********************************删除类接口********************************

    /**
     * 删除菜单
     *
     * @param menuId 菜单id
     * @param currentUser 当前用户
     * @return 通用消息返回对象
     */
    @DeleteMapping("/backend/api/v1/menu/delete/{menuId}")
    ResultVO<?> deleteMenu(@PathVariable("menuId") Long menuId, @SpringQueryMap User currentUser);

    // ********************************修改类接口********************************

    /**
     * 编辑菜单
     *
     * @param dto 后台编辑菜单DTO对象
     * @param currentUser 当前登录用户
     * @return 通用消息返回对象
     */
    @PutMapping("/backend/api/v1/menu/editMenu")
    ResultVO<?> editMenu(@RequestBody BackendMenuEditDTO dto, @SpringQueryMap User currentUser);

    // ********************************查询类接口********************************

    /**
     * 校验菜单路由名称是否重复
     *
     * @param routeName 路由名称
     * @param currentUser 当前登录用户
     * @return 重复返回true，不重复返回false
     */
    @GetMapping("/backend/api/v1/menu/checkRouteNameDuplicate")
    ResultVO<Boolean> checkRouteNameDuplicate(@RequestParam("routeName") String routeName, @SpringQueryMap User currentUser);

    /**
     * 校验菜单路由路径是否重复
     *
     * @param routePath 路由路径
     * @param currentUser 当前登录用户
     * @return 重复返回true，不重复返回false
     */
    @GetMapping("/backend/api/v1/menu/checkRoutePathDuplicate")
    ResultVO<Boolean> checkRoutePathDuplicate(@RequestParam("routePath") String routePath, @SpringQueryMap User currentUser);

    /**
     * 后台获取面包屑导航列表
     *
     * @param currentUser 当前登陆用户
     * @return 面包屑导航VO对象列表
     */
    @GetMapping("/backend/api/v1/menu/breadcrumbList")
    ResultVO<List<BackendBreadcrumbListVO>> findBreadcrumbList(@SpringQueryMap User currentUser);

    /**
     * 获取后台菜单列表
     *
     * @param dto 获取后台菜单列表DTO对象
     * @param currentUser 当前登陆用户
     * @return 后台获取菜单列表VO对象
     */
    @GetMapping("/backend/api/v1/menu/list")
    ResultVO<List<BackendMenuListVO>> findMenuList(@SpringQueryMap BackendMenuListDTO dto, @SpringQueryMap User currentUser);

    /**
     * 获取后台所有的菜单树
     *
     * @param currentUser 当前登陆用户
     * @return 后台获取菜单树VO对象列表
     */
    @GetMapping("/backend/api/v1/menu/tree")
    ResultVO<List<BackendMenuTreeVO>> findBackendMenuTree(@SpringQueryMap User currentUser);

}
