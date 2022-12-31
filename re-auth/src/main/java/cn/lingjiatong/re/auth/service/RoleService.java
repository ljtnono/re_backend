package cn.lingjiatong.re.auth.service;

import cn.lingjiatong.re.auth.mapper.RoleMapper;
import cn.lingjiatong.re.common.entity.Role;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 角色模块Service层
 *
 * @author Ling, Jiatong
 * Date: 2022/12/31 16:00
 */
@Slf4j
@Service
public class RoleService {


    @Resource
    private RoleMapper roleMapper;

    // ********************************新增类接口********************************
    // ********************************删除类接口********************************
    // ********************************修改类接口********************************
    // ********************************查询类接口********************************
    // ********************************私有函数********************************
    // ********************************公用函数********************************

    /**
     * 根据用户id获取用户角色列表
     *
     * @param userId 用户id
     * @return 用户角色列表
     */
    public List<Role> findRoleListByUserId(Long userId) {
        return roleMapper.findRoleListByUserId(userId);
    }

}
