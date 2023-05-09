package cn.lingjiatong.re.service.sys.service;

import cn.lingjiatong.re.common.constant.CommonConstant;
import cn.lingjiatong.re.common.entity.Permission;
import cn.lingjiatong.re.common.exception.ErrorEnum;
import cn.lingjiatong.re.common.exception.ResourceAlreadyExistException;
import cn.lingjiatong.re.common.util.SnowflakeIdWorkerUtil;
import cn.lingjiatong.re.service.sys.api.common.BackendMenuPermission;
import cn.lingjiatong.re.service.sys.mapper.PermissionMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
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
@Slf4j
@Service
public class PermissionService {

    @Autowired
    private PermissionMapper permissionMapper;
    @Autowired
    private SnowflakeIdWorkerUtil snowflakeIdWorkerUtil;

    // ********************************新增类接口********************************

    @Transactional(rollbackFor = Exception.class)
    public void save(Permission permission) {
        permissionMapper.insert(permission);
    }

    // ********************************删除类接口********************************
    // ********************************修改类接口********************************
    // ********************************查询类接口********************************


    /**
     * 根据菜单id列表和项目名称查询权限id列表
     *
     * @param menuIdCollection 菜单id集合
     * @param projectName      项目名称
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

    // ********************************私有函数********************************
    // ********************************公共函数********************************

    /**
     * 获取菜单权限列表
     *
     * @param menuId 菜单id
     * @return 后台菜单权限列表
     */
    public List<BackendMenuPermission> findMenuPermissionList(Long menuId) {
        List<Permission> permissionList = permissionMapper.selectList(new LambdaQueryWrapper<Permission>()
                .eq(Permission::getMenuId, menuId));
        if (CollectionUtils.isEmpty(permissionList)) {
           return Lists.newArrayList();
        }
        return permissionList
                .stream()
                .map(permission -> {
                    BackendMenuPermission backendMenuPermission = new BackendMenuPermission();
                    BeanUtils.copyProperties(permission, backendMenuPermission);
                    return backendMenuPermission;
                })
                .collect(Collectors.toList());
    }

    /**
     * 根据菜单id删除菜单的权限
     *
     * @param menuId 菜单id
     */
    public void deleteByMenuId(Long menuId) {
        permissionMapper.delete(new LambdaQueryWrapper<Permission>()
                .eq(Permission::getMenuId, menuId));
    }

    /**
     * 菜单权限是否已经存在
     *
     * @param menuId 菜单id
     * @param name 权限名称
     * @param expression 权限表达式
     * @return 存在返回true，不存在返回false
     */
    public boolean isMenuPermissionExist(Long menuId, String name, String expression) {
        return permissionMapper.selectOne(new LambdaQueryWrapper<Permission>()
                        .eq(Permission::getName, name)
                        .eq(Permission::getExpression, expression)
                .eq(Permission::getMenuId, menuId)) != null;
    }

    /**
     * 删除权限
     *
     * @param ids 权限id集合
     */
    public void deleteBatchIds(Collection<Long> ids) {
        permissionMapper.deleteBatchIds(ids);
    }

    /**
     * 保存新菜单的读写权限
     *
     * @param menuId 菜单id
     * @param permissionList 菜单权限列表
     */
    public void saveNewMenuPermission(Long menuId, List<BackendMenuPermission> permissionList) {
        if (CollectionUtils.isEmpty(permissionList)) {
            return;
        }
        List<Permission> permissions = permissionList
                .stream()
                .map(p -> {
                    Permission permission = new Permission();
                    permission.setMenuId(menuId);
                    permission.setName(p.getName());
                    permission.setExpression(p.getExpression());
                    permission.setProjectName(CommonConstant.PROJECT_NAME_BACKEND_PAGE);
                    permission.setId(snowflakeIdWorkerUtil.nextId());
                    return permission;
                }).collect(Collectors.toList());

        // 这里因为是新插入数据，所以不需要校验权限的重复性
        permissions.forEach(permission -> {
            try {
                permissionMapper.insert(permission);
            } catch (DuplicateKeyException e) {
                log.error(e.toString(), e);
                throw new ResourceAlreadyExistException(ErrorEnum.MENU_PERMISSION_EXIST_ERROR_MESSAGE);
            }
        });
    }

}
