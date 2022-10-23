package cn.lingjiatong.re.common.mapper;

import cn.lingjiatong.re.common.entity.Permission;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 系统权限mapper
 *
 * @author Ling, Jiatong
 * Date: 2022/10/22 19:16
 */
public interface PermissionMapper extends BaseMapper<Permission> {


    /**
     * 根据角色id获取角色的权限列表
     *
     * @param roleId 角色id
     * @return 权限列表
     */
    List<Permission> findPermissionByRoleId(@Param("roleId") Integer roleId);

}
