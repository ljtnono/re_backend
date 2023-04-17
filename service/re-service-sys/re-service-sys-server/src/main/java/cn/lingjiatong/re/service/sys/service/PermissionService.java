package cn.lingjiatong.re.service.sys.service;

import cn.lingjiatong.re.common.entity.Permission;
import cn.lingjiatong.re.service.sys.mapper.PermissionMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 权限service层
 *
 * @author Ling, Jiatong
 * Date: 3/25/23 10:23 PM
 */
@Service
public class PermissionService {

    @Autowired
    private PermissionMapper permissionMapper;

    // ********************************新增类接口********************************

    @Transactional(rollbackFor = Exception.class)
    public void save(Permission permission) {
        permissionMapper.insert(permission);
    }

    // ********************************删除类接口********************************
    // ********************************修改类接口********************************
    // ********************************查询类接口********************************

    /**
     * 根据菜单id和项目名称查询菜单对应的权限列表
     *
     * @param menuId 菜单id
     * @param projectName 项目名称
     * @return 菜单对应的权限列表
     */
    @Transactional(readOnly = true)
    public List<Permission> findPermissionListByMenuIdAndProjectName(Long menuId, String projectName) {
        return permissionMapper.selectList(new LambdaQueryWrapper<Permission>()
                .eq(Permission::getMenuId, menuId)
                .eq(Permission::getProjectName, projectName));
    }


    /**
     * 根据菜单id列表和项目名称查询权限id列表
     *
     * @param menuIdCollection 菜单id集合
     * @param projectName 项目名称
     * @return 权限id列表
     */
    @Transactional(readOnly = true)
    public List<Long> findPermissionIdListByMenuIdCollectionAndProjectName(Collection<Long> menuIdCollection, String projectName) {
        if (CollectionUtils.isEmpty(menuIdCollection)) {
            return Lists.newArrayList();
        }
        List<Permission> permissionList = permissionMapper.selectList(new LambdaQueryWrapper<Permission>()
                .in(Permission::getMenuId, menuIdCollection)
                .eq(Permission::getProjectName, projectName));
        if (CollectionUtils.isEmpty(permissionList)) {
            return Lists.newArrayList();
        }
        return permissionList.stream().map(Permission::getId).collect(Collectors.toList());
    }
}
