package cn.lingjiatong.re.service.sys.service;

import cn.lingjiatong.re.common.entity.TrUserRole;
import cn.lingjiatong.re.service.sys.mapper.TrUserRoleMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.apache.commons.compress.utils.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户角色模块service层
 *
 * @author Ling, Jiatong
 * Date: 2023/3/3 11:44
 */
@Service
public class TrUserRoleService {

    @Autowired
    private TrUserRoleMapper trUserRoleMapper;

    // ********************************新增类接口********************************
    // ********************************删除类接口********************************
    // ********************************修改类接口********************************
    // ********************************查询类接口********************************
    // ********************************私有函数********************************
    // ********************************公用函数********************************

    /**
     * 根据角色id获取该角色关联的用户id列表
     *
     * @param roleId 角色id
     * @return 用户id列表
     */
    @Transactional(readOnly = true)
    public List<Long> findUserIdListByRoleId(Long roleId) {
        List<TrUserRole> trUserRoleList = trUserRoleMapper.selectList(new LambdaQueryWrapper<TrUserRole>()
                .select(TrUserRole::getUserId)
                .eq(TrUserRole::getRoleId, roleId));
        if (CollectionUtils.isEmpty(trUserRoleList)) {
            return Lists.newArrayList();
        }
        return trUserRoleList.stream().map(TrUserRole::getUserId).collect(Collectors.toList());
    }

    /**
     * 根据用户id列表批量删除用户角色关系
     *
     * @param userIdList 用户id列表
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteTrUserRoleBatchByUserIdList(List<Long> userIdList) {
        trUserRoleMapper.delete(new LambdaQueryWrapper<TrUserRole>()
                .in(TrUserRole::getUserId, userIdList));
    }

    /**
     * 保存用户角色关系对象
     *
     * @param trUserRole 用户角色关系实体
     */
    @Transactional(rollbackFor = Exception.class)
    public void saveTrUserRole(TrUserRole trUserRole) {
        trUserRoleMapper.insert(trUserRole);
    }
}
