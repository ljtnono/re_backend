package cn.lingjiatong.re.auth.service;

import cn.lingjiatong.re.auth.mapper.PermissionMapper;
import cn.lingjiatong.re.common.entity.Permission;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 权限模块service层
 *
 * @author Ling, Jiatong
 * Date: 2022/12/31 15:40
 */
@Slf4j
@Service
public class PermissionService {

    @Resource
    private PermissionMapper permissionMapper;

    // ********************************新增类接口********************************
    // ********************************删除类接口********************************
    // ********************************修改类接口********************************
    // ********************************查询类接口********************************
    // ********************************私有函数********************************

    // ********************************公用函数********************************

    /**
     * 根据角色id列表获取角色的权限列表
     *
     * @param roleIdList 角色id列表
     * @return 权限列表
     */
    public List<Permission> findPermissionListByRoleIdList(List<Long> roleIdList, String projectName) {
        return permissionMapper.findPermissionListByRoleIdListAndProjectName(roleIdList, projectName);
    }


}
