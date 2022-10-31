package cn.lingjiatong.re.auth.mapper;

import cn.lingjiatong.re.common.entity.Role;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 系统角色实体
 *
 * @author Ling, Jiatong
 * Date: 2022/10/22 19:15
 */
public interface RoleMapper extends BaseMapper<Role> {


    /**
     * 根据用户id获取用户角色列表
     *
     * @param userId 用户id
     * @return 用户角色列表
     */
    List<Role> findRoleByUserId(@Param("userId") Integer userId);
}
