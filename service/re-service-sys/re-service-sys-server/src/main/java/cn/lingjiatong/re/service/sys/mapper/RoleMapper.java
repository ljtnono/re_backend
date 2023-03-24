package cn.lingjiatong.re.service.sys.mapper;

import cn.lingjiatong.re.common.entity.Role;
import cn.lingjiatong.re.service.sys.api.dto.BackendRolePageListDTO;
import cn.lingjiatong.re.service.sys.api.vo.BackendRoleListVO;
import cn.lingjiatong.re.service.sys.api.vo.BackendUserListVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

/**
 * 角色模块mapper层
 *
 * @author Ling, Jiatong
 * Date: 2023/3/3 14:55
 */
public interface RoleMapper extends BaseMapper<Role> {

    // ********************************新增类接口********************************
    // ********************************删除类接口********************************
    // ********************************修改类接口********************************
    // ********************************查询类接口********************************

    /**
     * 后台分页获取角色列表
     *
     * @param page 分页对象
     * @param dto 后台分页获取角色列表DTO对象
     * @return 后台获取用户列表VO对象分页对象
     */
    Page<BackendRoleListVO> findRolePageList(Page<?> page, @Param("dto") BackendRolePageListDTO dto);

    /**
     * 后台分页获取角色列表-查询总数
     *
     * @param dto 后台分页获取角色列表DTO对象
     * @return 角色总数
     */
    long findRolePageListTotal(@Param("dto") BackendRolePageListDTO dto);


    // ********************************私有函数********************************
    // ********************************公用函数********************************
}
