package cn.lingjiatong.re.service.sys.service;

import cn.lingjiatong.re.common.entity.TrRolePermission;
import cn.lingjiatong.re.service.sys.mapper.TrRolePermissionMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Collection;

/**
 * 角色权限关联模块service层
 *
 * @author Ling, Jiatong
 * Date: 2023/3/30 10:11
 */
@Service
public class TrRolePermissionService {

    @Autowired
    private TrRolePermissionMapper trRolePermissionMapper;

    // ********************************新增类接口********************************
    // ********************************删除类接口********************************

    /**
     * 根据角色id集合删除角色权限对应关系
     *
     * @param roleIdCollection 角色id集合
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteByRoleIdCollection(Collection<Long> roleIdCollection) {
        if (CollectionUtils.isEmpty(roleIdCollection)) {
            return;
        }
        trRolePermissionMapper.delete(new LambdaQueryWrapper<TrRolePermission>().in(TrRolePermission::getRoleId, roleIdCollection));
    }

    // ********************************修改类接口********************************
    // ********************************查询类接口********************************

    /**
     * 批量插入角色权限关联表
     *
     * @param trRolePermissionCollection 角色权限关联实体
     */
    @Transactional(rollbackFor = Exception.class)
    public void saveTrRolePermissionBatch(Collection<TrRolePermission> trRolePermissionCollection) {
        if (CollectionUtils.isEmpty(trRolePermissionCollection)) {
            return;
        }
        trRolePermissionCollection.forEach(trRolePermission -> {
            trRolePermissionMapper.insert(trRolePermission);
        });
    }


    // ********************************私有函数********************************
    // ********************************公用函数********************************
}
