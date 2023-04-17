package cn.lingjiatong.re.service.sys.service;

import cn.lingjiatong.re.common.constant.CommonConstant;
import cn.lingjiatong.re.common.entity.Menu;
import cn.lingjiatong.re.common.entity.Permission;
import cn.lingjiatong.re.service.sys.mapper.PermissionMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.time.ZoneId;
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
     * 保存新菜单的读写权限
     *
     * @param projectName 项目名称
     * @param menu 菜单实体
     */
    @Transactional(rollbackFor = Exception.class)
    public void saveNewMenuPermission(Menu menu, String projectName) {
//        Long menuParentId = menu.getParentId();
//
//        // 自动生成菜单的读写权限
//        Permission permission = new Permission();
//        permission.setProjectName(projectName);
//        permission.setMenuId(menu.getId());
//        permission.setCreateTime(LocalDateTime.now(ZoneId.of("Asia/Shanghai")));
//        permission.setModifyTime(LocalDateTime.now(ZoneId.of("Asia/Shanghai")));
//        permission.setDeleted(CommonConstant.ENTITY_NORMAL);
//        permission.setName(menu.getTitle());
//        permission.setType((byte) 1);
//        permission.setId(snowflakeIdWorkerUtil.nextId());
//        permission.setParentId();
//        permission.setExpression(menu.getName().toLowerCase());
//
//
//        // TODO 菜单权限
//        permissionMapper.insert(permission);
//        // TODO 菜单-读权限
//        permissionMapper.insert(permission);
//        // TODO 菜单-写权限
//        permissionMapper.insert(permission);
    }

}
