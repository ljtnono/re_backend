package cn.lingjiatong.re.service.sys.service;

import cn.lingjiatong.re.common.entity.TrUserRole;
import cn.lingjiatong.re.service.sys.mapper.TrUserRoleMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
     * 根据用户id列表批量删除用户角色关系
     *
     * @param userIdList 用户id列表
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteTrUserRoleBatchByUserIdList(List<Long> userIdList) {
        trUserRoleMapper.delete(new LambdaQueryWrapper<TrUserRole>()
                .in(TrUserRole::getUserId, userIdList));
    }
}
