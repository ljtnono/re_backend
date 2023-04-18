package cn.lingjiatong.re.service.sys.service;

import cn.lingjiatong.re.common.entity.Menu;
import cn.lingjiatong.re.common.entity.Route;
import cn.lingjiatong.re.common.util.JSONUtil;
import cn.lingjiatong.re.service.sys.api.vo.BackendRouteListVO;
import cn.lingjiatong.re.service.sys.mapper.RouteMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 后台路由模块service层
 *
 * @author Ling, Jiatong
 * Date: 4/10/23 10:42 PM
 */
@Service
public class BackendRouteService {

    @Autowired
    private RouteMapper routeMapper;
    @Autowired
    private BackendMenuService backendMenuService;

    // ********************************新增类接口********************************

    /**
     * 新增菜单路由实体
     *
     * @param route 菜单路由实体
     */
    @Transactional(rollbackFor = Exception.class)
    public void save(Route route) {
        routeMapper.insert(route);
    }

    // ********************************删除类接口********************************
    // ********************************修改类接口********************************
    // ********************************查询类接口********************************

    /**
     * 获取前端路由列表
     *
     * @param projectName 所属项目名称
     * @return 前端路由列表
     */
    @Transactional(readOnly = true)
    public List<BackendRouteListVO> findBackendRouteList(String projectName) {
        return getRoutesByParentId(-1L, projectName);
    }

    // ********************************私有函数********************************

    /**
     * 递归获取路由列表
     *
     * @param parentId    根父路由id
     * @param projectName 项目名称
     * @return 路由列表
     */
    private List<BackendRouteListVO> getRoutesByParentId(Long parentId, String projectName) {
        // 首先查询当前节点的子节点
        List<BackendRouteListVO> childRoutes = getChildRoutesByParentId(parentId, projectName);
        // 如果没有子节点，返回空列表
        if (childRoutes.isEmpty()) {
            return Collections.emptyList();
        }
        // 遍历子节点，查找每个子节点的子节点
        for (BackendRouteListVO childRoute : childRoutes) {
            childRoute.setChildren(getRoutesByParentId(childRoute.getId(), projectName));
        }
        return childRoutes;
    }

    /**
     * 根据父路由id获取子路由列表
     *
     * @param parentId    父路由id
     * @param projectName 项目名称
     * @return 子路由列表
     */
    private List<BackendRouteListVO> getChildRoutesByParentId(Long parentId, String projectName) {
        return routeMapper.selectList(new LambdaQueryWrapper<Route>()
                        .eq(Route::getParentId, parentId)
                        .eq(Route::getProjectName, projectName))
                .stream()
                .map(route -> {
                    BackendRouteListVO vo = new BackendRouteListVO();
                    BeanUtils.copyProperties(route, vo);
                    return vo;
                }).collect(Collectors.toList());
    }

    // ********************************公共函数********************************

    /**
     * 保存新菜单对应的路由数据
     *
     * @param menu 菜单实体
     */
    public void saveNewMenuRoute(Menu menu, String projectName) {
        Long parentId = menu.getParentId();
        // 新增菜单的路由项
        Route route = new Route();
        if (Long.valueOf(-1L).equals(parentId)) {
            // 如果是顶层菜单，那么将id + 100，parentId设置为1001
            Route lastChildRoute = routeMapper.selectOne(new LambdaQueryWrapper<Route>()
                    .select(Route::getId)
                    .eq(Route::getParentId, 1001L)
                    .orderByDesc(Route::getId)
                    .last("LIMIT 1"));
            if (lastChildRoute == null) {
                route.setId(1002L);
            } else {
                route.setId(lastChildRoute.getId() + 100);
            }
            route.setParentId(1001L);
        } else {
            // 如果是子级菜单，那么需要找到对应的父级路由
            Route parentRoute = routeMapper.selectOne(new LambdaQueryWrapper<Route>()
                    .select(Route::getId)
                    .eq(Route::getMenuId, parentId)
                    .orderByDesc(Route::getId)
                    .last("LIMIT 1"));
            route.setId(parentRoute.getId() + 1);
            route.setParentId(parentRoute.getId());
        }
        route.setComponent(menu.getComponentPath());
        route.setPath(menu.getPath());
        route.setName(menu.getName());
        route.setProjectName(projectName);
        Map<String, Object> meta = new HashMap<>(2);
        meta.put("title", menu.getTitle());
        meta.put("hideInMenu", true);
        route.setMeta(JSONUtil.objectToString(meta));
        route.setMenuId(menu.getId());
        save(route);
    }

}
