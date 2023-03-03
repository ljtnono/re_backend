package cn.lingjiatong.re.service.sys.service;

import cn.lingjiatong.re.common.constant.CommonConstant;
import cn.lingjiatong.re.common.constant.RedisCacheKeyEnum;
import cn.lingjiatong.re.common.constant.RoleConstant;
import cn.lingjiatong.re.common.constant.UserConstant;
import cn.lingjiatong.re.common.entity.Role;
import cn.lingjiatong.re.common.entity.User;
import cn.lingjiatong.re.common.exception.ErrorEnum;
import cn.lingjiatong.re.common.exception.ParamErrorException;
import cn.lingjiatong.re.common.exception.PermissionException;
import cn.lingjiatong.re.common.exception.ServerException;
import cn.lingjiatong.re.common.util.RedisUtil;
import cn.lingjiatong.re.service.sys.api.dto.BackendUserListDTO;
import cn.lingjiatong.re.service.sys.api.dto.BackendUserPhysicDeleteBatchDTO;
import cn.lingjiatong.re.service.sys.api.dto.BackendUserUpdateDTO;
import cn.lingjiatong.re.service.sys.api.dto.BackendUserUpdateDeleteStatusBatchDTO;
import cn.lingjiatong.re.service.sys.api.vo.BackendUserListVO;
import cn.lingjiatong.re.service.sys.constant.BackendUserErrorMessageConstant;
import cn.lingjiatong.re.service.sys.mapper.UserMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 后台用户模块service层
 *
 * @author Ling, Jiatong
 * Date: 2022/10/30 09:29
 */
@Slf4j
@Service
public class BackendUserService {

    @Resource
    private UserMapper userMapper;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private TrUserRoleService trUserRoleService;

    // ********************************新增类接口********************************
    // ********************************删除类接口********************************

    /**
     * 后台批量物理删除用户，规则如下：<br/>
     *
     * 1、只有超级管理员能物理删除（校验role角色）
     * 2、无法删除lingjiatong账号
     * @param dto 后台批量物理删除用户DTO对象
     * @param currentUser 当前登录用户
     */
    @Transactional(rollbackFor = Exception.class)
    public void physicDeleteUserBatch(BackendUserPhysicDeleteBatchDTO dto, User currentUser) {
        List<Long> userIdList = dto.getUserIdList();
        if (CollectionUtils.isEmpty(userIdList)) {
            return;
        }
        boolean[] isSuperAdmin = new boolean[]{false};
        Long currentUserId = currentUser.getId();
        // 获取用户所有的角色列表
        List<Role> roleList = userMapper.findUserRoleListById(currentUserId);
        roleList.forEach(role -> {
            if (RoleConstant.SUPER_ADMIN_ROLE_ID.equals(role.getId())) {
                isSuperAdmin[0] = true;
            }
        });
        if (!isSuperAdmin[0]) {
            // 没有操作权限
            throw new PermissionException(ErrorEnum.NO_PERMISSION_ERROR);
        }
        // 如果删除的用户中含有系统内置超级管理员，则无法删除
        if (userIdList.contains(UserConstant.SUPER_ADMIN_USER_ID)) {
            throw new PermissionException(ErrorEnum.NO_PERMISSION_ERROR.getCode(), BackendUserErrorMessageConstant.DELETE_SUPER_ADMIN_ERROR_MESSAGE);
        }
        try {
            // 删除用户
            userMapper.deleteBatchIds(userIdList);
            // 删除用户角色关联表
            trUserRoleService.deleteTrUserRoleBatchByUserIdList(userIdList);
        } catch (Exception e) {
            log.error(e.toString(), e);
            throw new ServerException(ErrorEnum.DATABASE_OPERATION_ERROR);
        }

        // 删除用户缓存
        Set<String> userInfoKeySet = userIdList
                .stream()
                .map(userId -> RedisCacheKeyEnum.USER_INFO.getValue() + userId)
                .collect(Collectors.toSet());
        if (!CollectionUtils.isEmpty(userInfoKeySet)) {
            redisUtil.deleteObjects(userInfoKeySet);
        }
    }


    // ********************************修改类接口********************************

    /**
     * 批量更新用户删除状态，更新规则：<br/>
     *
     * 1、只有超级管理员有权限删除和恢复账号 <br/>
     * 2、lingjiatong账号不能被删除 <br/>
     * @param dto 后台批量更改用户删除状态DTO对象
     * @param currentUser 当前用户
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateUserDeleteStatusBatch(BackendUserUpdateDeleteStatusBatchDTO dto, User currentUser) {
        List<Long> userIdList = dto.getUserIdList();
        Byte deleteStatus = dto.getDeleteStatus();
        if (CollectionUtils.isEmpty(userIdList)) {
            return;
        }
        if (CommonConstant.deleteValues().contains(deleteStatus)) {
            throw new ParamErrorException(ErrorEnum.REQUEST_PARAM_ERROR.getCode(), BackendUserErrorMessageConstant.DELETE_STATUS_NOT_SUPPORT_ERROR_MESSAGE);
        }
        // 如果是删除操作，需要判断是否是超级管理员角色
        if (deleteStatus.equals(CommonConstant.ENTITY_DELETE)) {
            boolean[] isSuperAdmin = new boolean[]{false};
            Long currentUserId = currentUser.getId();
            // 获取用户所有的角色列表
            List<Role> roleList = userMapper.findUserRoleListById(currentUserId);
            roleList.forEach(role -> {
                if (RoleConstant.SUPER_ADMIN_ROLE_ID.equals(role.getId())) {
                    isSuperAdmin[0] = true;
                }
            });
            if (!isSuperAdmin[0]) {
                // 没有操作权限
                throw new PermissionException(ErrorEnum.NO_PERMISSION_ERROR);
            }
        }
        // 如果删除的用户中含有系统内置超级管理员，则无法删除
        if (userIdList.contains(UserConstant.SUPER_ADMIN_USER_ID)) {
            throw new PermissionException(ErrorEnum.NO_PERMISSION_ERROR.getCode(), BackendUserErrorMessageConstant.DELETE_SUPER_ADMIN_ERROR_MESSAGE);
        }
        // 更新用户删除状态
        try {
            userMapper.updateUserDeleteStatusBatch(dto);
        } catch (Exception e) {
            log.error(e.toString(), e);
            throw new ServerException(ErrorEnum.DATABASE_OPERATION_ERROR);
        }
        // 删除用户缓存
        if (CommonConstant.ENTITY_DELETE.equals(deleteStatus)) {
            Set<String> userInfoKeySet = userIdList
                    .stream()
                    .map(userId -> RedisCacheKeyEnum.USER_INFO.getValue() + userId)
                    .collect(Collectors.toSet());
            if (!CollectionUtils.isEmpty(userInfoKeySet)) {
                redisUtil.deleteObjects(userInfoKeySet);
            }
        }
    }

    /**
     * 个人更新用户信息
     * 个人能够编辑的字段：用户名、邮箱、手机号
     *
     * @param dto 后台编辑用户信息DTO对象
     * @param currentUser 当前用户实体
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateUser(BackendUserUpdateDTO dto, User currentUser) {
        // 校验参数
        checkBackendUserUpdateDTO(dto, Boolean.FALSE);
        // 校验需要更新信息的用户是否是当前用户，如果不是，那么报错
        if (!currentUser.getId().equals(dto.getUserId())) {
            throw new PermissionException(ErrorEnum.CAN_NOT_UPDATE_OTHER_USER_ERROR);
        }
        // 更新用户信息
        try {
            userMapper.update(null, new LambdaUpdateWrapper<User>()
                    .set(StringUtils.hasLength(dto.getUsername()), User::getUsername, dto.getUsername())
                    .set(StringUtils.hasLength(dto.getEmail()), User::getEmail, dto.getEmail())
                    .set(StringUtils.hasLength(dto.getPhone()), User::getPhone, dto.getPhone())
                    .eq(User::getId, dto.getUserId()));
        } catch (Exception e) {
            log.error(e.toString(), e);
            throw new ServerException(ErrorEnum.DATABASE_OPERATION_ERROR);
        }
    }

    /**
     * 管理员更改用户信息
     *
     * @param dto 后台编辑用户信息DTO对象
     */
    public void adminUpdateUser(BackendUserUpdateDTO dto) {

    }

    // ********************************查询类接口********************************

    /**
     * 后台获取用户列表
     *
     * @param dto 后台获取用户列表DTO对象
     * @return 后台获取用户列表VO对象分页对象
     */
    @Transactional(readOnly = true)
    public Page<BackendUserListVO> findUserList(BackendUserListDTO dto, User currentUser) {
        return userMapper.findUserList(new Page<>(dto.getPageNum(), dto.getPageSize()), dto);
    }


    // ********************************私有函数********************************

    /**
     * 校验后台更新用户DTO对象
     * 个人能编辑的字段：用户名、邮箱、手机号
     * 管理员能编辑的信息：用户名、密码、邮箱、手机号、用户角色列表
     *
     * @param dto 后台编辑用户信息DTO对象
     * @param adminUpdate 是否是管理员编辑 true 是 false 不是
     */
    private void checkBackendUserUpdateDTO(BackendUserUpdateDTO dto, boolean adminUpdate) {
        Long userId = dto.getUserId();
        String username = dto.getUsername();
        String email = dto.getEmail();
        String phone = dto.getPhone();
        // TODO 校验逻辑

    }


    // ********************************公用函数********************************

    /**
     * 根据用户id列表获取用户列表
     *
     * @param userIdList 用户id列表
     * @param fields 要获取的字段列表
     * @return 后台获取用户列表VO对象列表
     */
    public List<BackendUserListVO> findUserListByUserIdList(List<Long> userIdList, SFunction<User, ?>... fields) {
        if (CollectionUtils.isEmpty(userIdList)) {
            return List.of();
        }
        List<User> users;
        if (fields == null || fields.length == 0) {
            users = userMapper.selectList(new LambdaQueryWrapper<User>()
                    .in(User::getId, userIdList));
        } else {
            users = userMapper.selectList(new LambdaQueryWrapper<User>()
                    .select(fields)
                    .in(User::getId, userIdList));
        }
        return users.stream()
                .map(user -> {
                    BackendUserListVO vo = new BackendUserListVO();
                    BeanUtils.copyProperties(user, vo);
                    vo.setId(String.valueOf(user.getId()));
                    return vo;
                }).collect(Collectors.toList());
    }

}
