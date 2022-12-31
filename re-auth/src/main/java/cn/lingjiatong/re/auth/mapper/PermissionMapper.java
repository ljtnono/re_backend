package cn.lingjiatong.re.auth.mapper;

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
     * 根据角色id列表获取角色的权限列表
     *
     * @param roleIdList 角色id列表
     * @param projectName 项目名
     * @return 权限列表
     */
    List<Permission> findPermissionListByRoleIdListAndProjectName(@Param("roleIdList") List<Long> roleIdList, @Param("projectName") String projectName);

}
