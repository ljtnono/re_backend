package cn.lingjiatong.re.service.sys.service;

import cn.lingjiatong.re.common.constant.CommonConstant;
import cn.lingjiatong.re.common.entity.Role;
import cn.lingjiatong.re.common.entity.User;
import cn.lingjiatong.re.service.sys.api.vo.BackendRoleListVO;
import cn.lingjiatong.re.service.sys.mapper.RoleMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 后台角色模块service层
 *
 * @author Ling, Jiatong
 * Date: 2023/3/3 14:34
 */
@Slf4j
@Service
public class BackendRoleService {

    @Autowired
    private RoleMapper roleMapper;

    // ********************************新增类接口********************************
    // ********************************删除类接口********************************
    // ********************************修改类接口********************************
    // ********************************查询类接口********************************

    /**
     * 获取角色列表
     *
     * @param currentUser 当前用户
     * @return 后端角色列表VO对象列表
     */
    public List<BackendRoleListVO> findRoleList(User currentUser) {
        List<Role> roleList = roleMapper.selectList(new LambdaQueryWrapper<Role>()
                .select(Role::getId, Role::getName)
                .eq(Role::getDeleted, CommonConstant.ENTITY_NORMAL));
        return roleList
                .stream()
                .map(role -> {
                    BackendRoleListVO vo = new BackendRoleListVO();
                    vo.setId(role.getId());
                    vo.setName(role.getName());
                    return vo;
                })
                .collect(Collectors.toList());
    }

    // ********************************私有函数********************************
    // ********************************公用函数********************************

    /**
     * 校验角色是否存在
     *
     * @param roleId 角色id
     * @return 存在返回true，不存在返回false
     */
    public boolean isRoleExist(Long roleId) {
        Role role = roleMapper.selectOne(new LambdaQueryWrapper<Role>()
                .select(Role::getId)
                .eq(Role::getId, roleId));
        return role != null;
    }
}
