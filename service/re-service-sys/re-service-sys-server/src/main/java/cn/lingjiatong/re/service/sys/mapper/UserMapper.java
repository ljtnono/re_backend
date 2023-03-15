package cn.lingjiatong.re.service.sys.mapper;

import cn.lingjiatong.re.common.entity.Role;
import cn.lingjiatong.re.common.entity.User;
import cn.lingjiatong.re.service.sys.api.dto.BackendUserListDTO;
import cn.lingjiatong.re.service.sys.api.dto.BackendUserUpdateDeleteStatusBatchDTO;
import cn.lingjiatong.re.service.sys.api.vo.BackendUserListVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户mapper层
 *
 * @author Ling, Jiatong
 * Date: 2022/10/15 22:58
 */
public interface UserMapper extends BaseMapper<User> {


    /**
     * 分页获取用户列表
     * @param page 分页对象
     * @param dto 后台获取用户列表DTO对象
     * @return 后台获取用户列表VO对象分页对象
     */
    Page<BackendUserListVO> findUserList(IPage<User> page, @Param("dto") BackendUserListDTO dto);

    /**
     * 分页获取用户列表-查询总数
     *
     * @param dto 后台获取用户列表DTO对象
     * @return 用户总数
     */
    long findUserListTotal(@Param("dto") BackendUserListDTO dto);

    /**
     * 批量更新用户删除状态
     *
     * @param dto 后台批量更改用户删除状态DTO对象
     */
    void updateUserDeleteStatusBatch(@Param("dto") BackendUserUpdateDeleteStatusBatchDTO dto);

    /**
     * 根据用户id获取用户角色列表
     *
     * @param id
     * @return 角色列表
     */
    List<Role> findUserRoleListById(Long id);

}
