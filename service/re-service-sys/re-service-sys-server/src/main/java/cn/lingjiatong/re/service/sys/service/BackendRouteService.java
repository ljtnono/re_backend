package cn.lingjiatong.re.service.sys.service;

import cn.lingjiatong.re.common.constant.CommonConstant;
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
     * 获取全部路由列表（非树结构）
     *
     * @return 全部路由列表
     */
    @Transactional(readOnly = true)
    public List<Route> findRouteListAll() {
        return routeMapper.selectList(new LambdaQueryWrapper<Route>());
    }

    /**
     * 根据id获取路由信息
     *
     * @param id 路由id
     * @return 路由实体
     */
    @Transactional(readOnly = true)
    public Route findRouteById(Long id) {
        return routeMapper.selectById(id);
    }

    /**
     * 根据parentId获取路由信息
     *
     * @param parentId 父路由id
     * @return 路由实体列表
     */
    @Transactional(readOnly = true)
    public List<Route> findRouteByParentId(Long parentId) {
        return routeMapper.selectList(new LambdaQueryWrapper<Route>().eq(Route::getParentId, parentId));
    }

    /**
     * 保存新菜单对应的路由数据
     *
     * @param menu 菜单实体
     */
    public void saveNewMenuRoute(Menu menu) {
        Long menuParentId = menu.getParentId();
        if (Long.valueOf(-1L).equals(menuParentId)) {
            // 新增的为顶级菜单
            Route route = new Route();
            route.setProjectName(CommonConstant.PROJECT_NAME_BACKEND_PAGE);
            route.setPath(menu.getRoutePath());
            route.setName(menu.getRouteName());
            route.setComponent(menu.getComponentPath());
            route.setRedirect(null);
            route.setProps(null);
            route.setAlias(null);
            route.setBeforeEnter(null);
            Map<String, Object> metaMap = new HashMap<>(2);
            metaMap.put("title", menu.getTitle());
            metaMap.put("hideInMenu", true);
            route.setMeta(JSONUtil.objectToString(metaMap));
            route.setCaseSensitive((byte) 1);
            route.setPathToRegexOptions(null);
            route.setParentId(1001L);
            route.setMenuId(menu.getId());
            this.save(route);
        } else {
            // 新增的为子级菜单

        }
    }

}
