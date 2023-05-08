package cn.lingjiatong.re.service.sys.service;

import cn.lingjiatong.re.common.constant.CommonConstant;
import cn.lingjiatong.re.common.constant.MenuErrorMessageConstant;
import cn.lingjiatong.re.common.constant.MenuRegexConstant;
import cn.lingjiatong.re.common.constant.UserConstant;
import cn.lingjiatong.re.common.entity.Menu;
import cn.lingjiatong.re.common.entity.Route;
import cn.lingjiatong.re.common.entity.User;
import cn.lingjiatong.re.common.exception.*;
import cn.lingjiatong.re.common.util.JSONUtil;
import cn.lingjiatong.re.common.util.RandomUtil;
import cn.lingjiatong.re.common.util.SnowflakeIdWorkerUtil;
import cn.lingjiatong.re.service.sys.api.dto.BackendMenuListDTO;
import cn.lingjiatong.re.service.sys.api.dto.BackendMenuSaveDTO;
import cn.lingjiatong.re.service.sys.api.vo.BackendBreadcrumbListVO;
import cn.lingjiatong.re.service.sys.api.vo.BackendMenuListVO;
import cn.lingjiatong.re.service.sys.api.vo.BackendMenuTreeVO;
import cn.lingjiatong.re.service.sys.mapper.MenuMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 后台菜单相关接口service层
 *
 * @author Ling, Jiatong
 * Date: 3/25/23 10:12 PM
 */
@Slf4j
@Service
public class BackendMenuService {

    @Autowired
    private MenuMapper menuMapper;
    @Autowired
    private BackendRouteService backendRouteService;
    @Autowired
    private SnowflakeIdWorkerUtil snowflakeIdWorkerUtil;
    @Autowired
    private PermissionService permissionService;
    @Autowired
    private TrRolePermissionService trRolePermissionService;
    @Autowired
    private TrRoleMenuService trRoleMenuService;

    // ********************************新增类接口********************************

    /**
     * 新增菜单
     *
     * @param dto         后台新增菜单DTO对象
     * @param currentUser 当前登录用户
     */
    @Transactional(rollbackFor = Exception.class)
    public void saveMenu(BackendMenuSaveDTO dto, User currentUser) {
        // 校验是是否是超级管理员
        Long userId = currentUser.getId();
        if (!UserConstant.SUPER_ADMIN_USER_ID.equals(userId)) {
            // 只允许lingjiatong账号修改菜单相关内容
            throw new PermissionException(ErrorEnum.NO_PERMISSION_ERROR);
        }
        // 校验新增菜单DTO对象
        checkBackendMenuSaveDTO(dto);

        try {
            // 新增菜单
            Menu menu = new Menu();
            menu.setId(snowflakeIdWorkerUtil.nextId());
            menu.setParentId(dto.getParentId());
            menu.setIcon(dto.getIcon());
            menu.setRoutePath(dto.getRoutePath());
            menu.setRouteName(dto.getRouteName());
            menu.setComponentPath(dto.getComponentPath());
            menu.setTitle(dto.getTitle());
            menu.setProjectName(CommonConstant.PROJECT_NAME_BACKEND_PAGE);
            menuMapper.insert(menu);

            // 生成新菜单的路由
            backendRouteService.saveNewMenuRoute(menu);
            List<BackendMenuSaveDTO.MenuPermission> permissionList = dto.getPermissionList();
            if (!CollectionUtils.isEmpty(permissionList)) {
                permissionService.saveNewMenuPermission(menu.getId(), permissionList);
            }
        } catch (Exception e) {
            log.error(e.toString(), e);
            throw new ServerException(ErrorEnum.DATABASE_OPERATION_ERROR);
        }
    }

    // ********************************删除类接口********************************

    /**
     * 删除菜单
     *
     * @param menuId 菜单id
     * @param currentUser 当前登录用户
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteMenu(Long menuId, User currentUser) {
        // 校验菜单是否存在
        Menu menu = menuMapper.selectById(menuId);
        if (menu == null) {
            throw new ResourceNotExistException(ErrorEnum.RESOURCE_NOT_EXIST_ERROR);
        }
        //  递归删除
        dfsDeleteMenu(menuId);
    }

    // ********************************修改类接口********************************

    @Transactional(rollbackFor = Exception.class)
    public void updateMenu() {


    }

    // ********************************查询类接口********************************

    /**
     * 校验菜单路由名称是否重复
     *
     * @param routeName 菜单路由名称
     * @param currentUser 当前登录用户
     * @return 重复返回true，不重复返回false
     */
    public boolean checkRouteNameDuplicate(String routeName, User currentUser) {
        if (!StringUtils.hasLength(routeName)) {
            throw new ResourceAlreadyExistException(ErrorEnum.ROUTE_NAME_EXIST_ERROR_MESSAGE);
        }
        Menu menu = menuMapper.selectOne(new LambdaQueryWrapper<Menu>()
                .select(Menu::getId)
                .eq(Menu::getRouteName, routeName));
        return menu != null;
    }

    /**
     * 校验菜单路由路径是否重复
     *
     * @param routePath 路由路径
     * @param currentUser 当前登录用户
     * @return 重复返回true，不重复返回false
     */
    public boolean checkRoutePathDuplicate(String routePath, User currentUser) {
        if (!StringUtils.hasLength(routePath)) {
            throw new ResourceAlreadyExistException(ErrorEnum.ROUTE_PATH_EXIST_ERROR_MESSAGE);
        }
        Menu menu = menuMapper.selectOne(new LambdaQueryWrapper<Menu>()
                .select(Menu::getId)
                .eq(Menu::getRoutePath, routePath));
        return menu != null;
    }

    /**
     * 后台获取面包屑导航
     *
     * @param currentUser 当前登陆用户
     * @return 后台面包屑导航VO对象列表
     */
    @Transactional(readOnly = true)
    public List<BackendBreadcrumbListVO> findBreadcrumbList(User currentUser) {
        List<Route> routeList = backendRouteService.findRouteListAll();
        List<Long> parentIdList = routeList.stream()
                .map(Route::getParentId)
                .distinct()
                .collect(Collectors.toList());
        List<Route> realChildRouteList = routeList
                .stream()
                .filter(route -> !parentIdList.contains(route.getId()))
                .collect(Collectors.toList());
        List<BackendBreadcrumbListVO> result = Lists.newArrayList();
        realChildRouteList.forEach(route -> {
            BackendBreadcrumbListVO vo = new BackendBreadcrumbListVO();
            vo.setRouteName(route.getName());
            vo.setBreadcrumbList(Lists.newArrayList());
            dfsRouteToGenerateBreadcrumb(route, vo.getBreadcrumbList());
            vo.setBreadcrumbList(Lists.reverse(vo.getBreadcrumbList()));
            result.add(vo);
        });
        return result;
    }

    /**
     * 分页获取菜单列表
     *
     * @param dto         后台获取菜单列表DTO对象
     * @param currentUser 当前登陆用户
     * @return 后台获取菜单列表VO对象列表
     */
    @Transactional(readOnly = true)
    public List<BackendMenuListVO> findMenuList(BackendMenuListDTO dto, User currentUser) {
        List<BackendMenuListVO> menuList = menuMapper.findMenuList(dto);
        menuList.forEach(this::dfsGenerateMenuChildren);
        return menuList;
    }

    /**
     * 根据菜单id列表获取菜单列表
     *
     * @param menuIdList 菜单id列表
     * @return 菜单实体列表
     */
    public List<Menu> findMenuListByIdList(List<Long> menuIdList) {
        if (CollectionUtils.isEmpty(menuIdList)) {
            return Lists.newArrayList();
        }
        return menuMapper.selectList(new LambdaQueryWrapper<Menu>()
                .in(Menu::getId, menuIdList));
    }

    /**
     * 根据菜单id列表校验菜单是否都存在
     *
     * @param menuIdCollection 菜单id集合
     * @return 都存在则返回true，否则返回false，参数为空返回false
     */
    public boolean isExistsByIdList(Collection<Long> menuIdCollection) {
        if (CollectionUtils.isEmpty(menuIdCollection)) {
            return false;
        }
        Integer count = menuMapper.selectCount(new LambdaQueryWrapper<Menu>()
                .in(Menu::getId, menuIdCollection));
        return count.equals(menuIdCollection.size());
    }


    /**
     * 后台获取菜单树
     *
     * @param currentUser 当前登陆用户
     * @return 后台获取菜单树VO对象列表
     */
    public List<BackendMenuTreeVO> findBackendMenuTree(User currentUser) {
        // 先查询后台管理系统所有的菜单列表
        List<Menu> menuList = menuMapper.selectList(new LambdaQueryWrapper<Menu>()
                .select(Menu::getId, Menu::getParentId, Menu::getTitle)
                .eq(Menu::getProjectName, CommonConstant.PROJECT_NAME_BACKEND_PAGE));

        List<BackendMenuTreeVO> backendMenuTreeVOList = menuList
                .stream()
                .map(menu -> {
                    BackendMenuTreeVO vo = new BackendMenuTreeVO();
                    vo.setMenuTitle(menu.getTitle());
                    vo.setMenuId(String.valueOf(menu.getId()));
                    vo.setParentMenuId(String.valueOf(menu.getParentId()));
                    return vo;
                }).collect(Collectors.toList());

        return menuListToMenuTree(backendMenuTreeVOList, "-1");
    }

    // ********************************私有函数********************************


    /**
     * 递归删除菜单
     *
     * @param menuId 菜单id
     */
    private void dfsDeleteMenu(Long menuId) {
        List<Menu> menus = menuMapper.selectList(new LambdaQueryWrapper<Menu>()
                .eq(Menu::getParentId, menuId));
        // 删除菜单
        menuMapper.delete(new LambdaQueryWrapper<Menu>()
                .eq(Menu::getId, menuId));
        // 删除路由
        backendRouteService.deleteByMenuId(menuId);
        // 获取菜单对应的权限id列表
        List<Long> permissionIdList = permissionService
                .findPermissionIdListByMenuIdCollectionAndProjectName(List.of(menuId), CommonConstant.PROJECT_NAME_BACKEND_PAGE);
        if (!CollectionUtils.isEmpty(permissionIdList)) {
            // 删除权限
            permissionService.deleteBatchIds(permissionIdList);
            // 删除角色权限关联
            trRolePermissionService.deleteByPermissionIdCollection(permissionIdList);
        }
        // 删除角色菜单关联
        trRoleMenuService.deleteByMenuId(menuId);
        if (!CollectionUtils.isEmpty(menus)) {
            menus.forEach(menu -> dfsDeleteMenu(menu.getId()));
        }
    }

    /**
     * 深度优先搜索生成面包屑导航
     *
     * @param route          当前路由实体
     * @param breadcrumbList 面包屑导航列表
     */
    private void dfsRouteToGenerateBreadcrumb(Route route, List<String> breadcrumbList) {
        if (Long.valueOf(1001L).equals(route.getId())) {
            return;
        }
        Map map = JSONUtil.stringToObject(route.getMeta(), Map.class);
        String title = (String) map.get("title");
        breadcrumbList.add(title);

        Route parentRoute = backendRouteService.findRouteById(route.getParentId());
        if (parentRoute != null) {
            dfsRouteToGenerateBreadcrumb(parentRoute, breadcrumbList);
        }
    }

    /**
     * 深度优先搜索生成菜单的子菜单
     *
     * @param menu 每次循环的菜单
     */
    private void dfsGenerateMenuChildren(BackendMenuListVO menu) {
        Long id = Long.valueOf(menu.getId());
        List<BackendMenuListVO> cc = menu.getChildren();
        menu.setHash(RandomUtil.getInstance().generateUUID());
        if (CollectionUtils.isEmpty(cc)) {
            cc = Lists.newArrayList();
            menu.setChildren(cc);
        }
        // 查询子菜单
        List<Menu> childMenuList = menuMapper.selectList(new LambdaQueryWrapper<Menu>()
                .eq(Menu::getParentId, id));
        if (!CollectionUtils.isEmpty(childMenuList)) {
            for (Menu childMenu : childMenuList) {
                BackendMenuListVO childVO = new BackendMenuListVO();
                BeanUtils.copyProperties(childMenu, childVO);
                childVO.setId(String.valueOf(childMenu.getId()));
                childVO.setParentId(String.valueOf(childMenu.getParentId()));
                childVO.setHash(RandomUtil.getInstance().generateUUID());
                cc.add(childVO);
                dfsGenerateMenuChildren(childVO);
            }
        }
    }

    /**
     * 将角色菜单权限列表转换为角色菜单权限树对象列表
     *
     * @param menuList 菜单实体列表
     * @return 角色菜单权限树对象列表
     */
    private List<BackendMenuTreeVO> menuListToMenuTree(List<BackendMenuTreeVO> menuList, String parentId) {
        List<BackendMenuTreeVO> result = menuList
                .stream()
                // 过滤父节点
                .filter(parent -> parent.getParentMenuId().equals(parentId))
                .map(child -> {
                    child.setChildren(menuListToMenuTree(menuList, child.getMenuId()));
                    return child;
                }).collect(Collectors.toList());
        return result;
    }

    /**
     * 校验新增菜单DTO对象
     *
     * @param dto 新增菜单DTO对象
     */
    private void checkBackendMenuSaveDTO(BackendMenuSaveDTO dto) {
        Long parentId = dto.getParentId();
        String title = dto.getTitle();
        String routePath = dto.getRoutePath();
        String routeName = dto.getRouteName();
        String componentPath = dto.getComponentPath();
        List<BackendMenuSaveDTO.MenuPermission> permissionList = dto.getPermissionList();

        if (parentId == null) {
            throw new ParamErrorException(ErrorEnum.ILLEGAL_PARAM_ERROR.getCode(), MenuErrorMessageConstant.PARENT_ID_EMPTY_ERROR_MESSAGE);
        }
        if (!StringUtils.hasLength(title)) {
            throw new ParamErrorException(ErrorEnum.ILLEGAL_PARAM_ERROR.getCode(), MenuErrorMessageConstant.MENU_TITLE_EMPTY_ERROR_MESSAGE);
        }
        if (!StringUtils.hasLength(routePath)) {
            throw new ParamErrorException(ErrorEnum.ILLEGAL_PARAM_ERROR.getCode(), MenuErrorMessageConstant.MENU_ROUTE_PATH_EMPTY_ERROR_MESSAGE);
        }
        if (!StringUtils.hasLength(routeName)) {
            throw new ParamErrorException(ErrorEnum.ILLEGAL_PARAM_ERROR.getCode(), MenuErrorMessageConstant.MENU_ROUTE_NAME_EMPTY_ERROR_MESSAGE);
        }
        if (!StringUtils.hasLength(componentPath)) {
            throw new ParamErrorException(ErrorEnum.ILLEGAL_PARAM_ERROR.getCode(), MenuErrorMessageConstant.MENU_COMPONENT_PATH_EMPTY_ERROR_MESSAGE);
        }

        // 正则校验
        if (!MenuRegexConstant.MENU_TITLE_REGEX.matcher(title).matches()) {
            throw new ParamErrorException(ErrorEnum.ILLEGAL_PARAM_ERROR.getCode(), MenuErrorMessageConstant.MENU_TITLE_FORMAT_ERROR_MESSAGE);
        }
        if (!MenuRegexConstant.MENU_ROUTE_PATH_REGEX.matcher(routePath).matches()) {
            throw new ParamErrorException(ErrorEnum.ILLEGAL_PARAM_ERROR.getCode(), MenuErrorMessageConstant.MENU_ROUTE_PATH_FORMAT_ERROR_MESSAGE);
        }
        if (!MenuRegexConstant.MENU_ROUTE_NAME_REGEX.matcher(routeName).matches()) {
            throw new ParamErrorException(ErrorEnum.ILLEGAL_PARAM_ERROR.getCode(), MenuErrorMessageConstant.MENU_ROUTE_NAME_FORMAT_ERROR_MESSAGE);
        }
        if (!MenuRegexConstant.MENU_COMPONENT_PATH_REGEX.matcher(componentPath).matches()) {
            throw new ParamErrorException(ErrorEnum.ILLEGAL_PARAM_ERROR.getCode(), MenuErrorMessageConstant.MENU_COMPONENT_PATH_FORMAT_ERROR_MESSAGE);
        }

        // 权限表达式校验
        if (!CollectionUtils.isEmpty(permissionList)) {
            permissionList.forEach(permission -> {
                String permissionName = permission.getName();
                String permissionExpression = permission.getExpression();
                // 校验权限名称长度，校验权限表达式长度
                if (!StringUtils.hasLength(permissionName)) {
                    throw new ParamErrorException(ErrorEnum.ILLEGAL_PARAM_ERROR.getCode(), MenuErrorMessageConstant.MENU_PERMISSION_NAME_EMPTY_ERROR_MESSAGE);
                }
                if (!StringUtils.hasLength(permissionExpression)) {
                    throw new ParamErrorException(ErrorEnum.ILLEGAL_PARAM_ERROR.getCode(), MenuErrorMessageConstant.MENU_PERMISSION_EXPRESSION_EMPTY_ERROR_MESSAGE);
                }
                // 权限正则校验
                if (!MenuRegexConstant.MENU_PERMISSION_NAME_REGEX.matcher(permissionName).matches()) {
                    throw new ParamErrorException(ErrorEnum.ILLEGAL_PARAM_ERROR.getCode(), MenuErrorMessageConstant.MENU_PERMISSION_NAME_FORMAT_ERROR_MESSAGE);
                }
                // 权限正则校验
                if (!MenuRegexConstant.MENU_PERMISSION_EXPRESSION_REGEX.matcher(permissionExpression).matches()) {
                    throw new ParamErrorException(ErrorEnum.ILLEGAL_PARAM_ERROR.getCode(), MenuErrorMessageConstant.MENU_PERMISSION_EXPRESSION_FORMAT_ERROR_MESSAGE);
                }
            });
        }

        // 业务校验
        // 校验父菜单是否存在
        if (!Long.valueOf(-1L).equals(parentId)) {
            Menu menu = menuMapper.selectOne(new LambdaQueryWrapper<Menu>()
                    .select(Menu::getId)
                    .eq(Menu::getId, parentId));
            if (menu == null) {
                throw new ResourceNotExistException(ErrorEnum.RESOURCE_NOT_EXIST_ERROR.getCode(), MenuErrorMessageConstant.MENU_PARENT_MENU_NOT_EXIST_ERROR_MESSAGE);
            }
        }
        // 校验菜单是否重复，路由名称和路由路径都不能有重复的
        if (isRouteNameDuplicate(routeName)) {
            throw new ResourceAlreadyExistException(ErrorEnum.ROUTE_NAME_EXIST_ERROR_MESSAGE);
        }
        if (isRoutePathDuplicate(routePath)) {
            throw new ResourceAlreadyExistException(ErrorEnum.ROUTE_PATH_EXIST_ERROR_MESSAGE);
        }

        // 新增菜单不用校验菜单的权限是否重复

    }


    // ********************************公用函数********************************

    /**
     * 菜单路由名称是否重复
     *
     * @param routeName 菜单路由名称
     * @return 重复返回true，不重复返回false
     */
    public boolean isRouteNameDuplicate(String routeName) {
        Menu menu = menuMapper.selectOne(new LambdaQueryWrapper<Menu>()
                .select(Menu::getId)
                .eq(Menu::getRouteName, routeName));
        return menu != null;
    }

    /**
     * 菜单路由路径是否重复
     *
     * @param routePath 菜单路由路径
     * @return 重复返回true，不重复返回false
     */
    public boolean isRoutePathDuplicate(String routePath) {
        Menu menu = menuMapper.selectOne(new LambdaQueryWrapper<Menu>()
                .select(Menu::getId)
                .eq(Menu::getRoutePath, routePath));
        return menu != null;
    }

}
