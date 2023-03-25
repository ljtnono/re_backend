package cn.lingjiatong.re.service.sys.service;

import cn.lingjiatong.re.common.entity.Permission;
import cn.lingjiatong.re.service.sys.mapper.PermissionMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public List<Permission> findPermissionListByMenuIdAndProjectName(Long menuId, String projectName) {
        return permissionMapper.selectList(new LambdaQueryWrapper<Permission>()
                .eq(Permission::getMenuId, menuId)
                .eq(Permission::getProjectName, projectName));
    }

}
