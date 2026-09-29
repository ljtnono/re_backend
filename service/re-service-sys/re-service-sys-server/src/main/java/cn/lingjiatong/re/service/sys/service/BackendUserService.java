package cn.lingjiatong.re.service.sys.service;

import cn.lingjiatong.re.common.constant.*;
import cn.lingjiatong.re.common.entity.*;
import cn.lingjiatong.re.common.exception.*;
import cn.lingjiatong.re.common.util.EncryptUtil;
import cn.lingjiatong.re.common.util.RedisUtil;
import cn.lingjiatong.re.common.util.SnowflakeIdWorkerUtil;
import cn.lingjiatong.re.service.sys.api.dto.*;
import cn.lingjiatong.re.service.sys.api.vo.BackendUserListVO;
import cn.lingjiatong.re.service.sys.mapper.*;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import jakarta.annotation.Resource;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
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
    @Autowired
    private UserLoginLogService userLoginLogService;
    @Autowired
    private SnowflakeIdWorkerUtil snowflakeIdWorkerUtil;
    @Autowired
    private BackendRoleService backendRoleService;
    @Autowired
    @Qualifier("commonThreadPool")
    private ExecutorService commonThreadPool;
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private TrRolePermissionMapper trRolePermissionMapper;
    @Autowired
    private PermissionMapper permissionMapper;
    @Autowired
    private TrUserRoleMapper trUserRoleMapper;
    @Autowired
    private MailService mailService;



    // ********************************新增类接口********************************

    /**
     * 后台保存用户信息
     *
     * @param dto         后台保存用户DTO对象
     * @param currentUser 当前登录用户
     * @return 通用消息返回对象
     */
    @Transactional(rollbackFor = Exception.class)
    public void saveUser(BackendUserSaveDTO dto, User currentUser) {
        // 校验保存用户参数
        checkBackendUserSaveDTO(dto);

        // 插入用户实体
        User user = new User();
        user.setId(snowflakeIdWorkerUtil.nextId());
        user.setUsername(dto.getUsername());
        user.setPassword(EncryptUtil.getInstance().getMd5LowerCase(dto.getPassword()));
        user.setEmail(dto.getEmail());
        user.setAvatarUrl(dto.getAvatarUrl());
        user.setPhone(dto.getPhone());
        user.setCreateTime(LocalDateTime.now(ZoneId.of("Asia/Shanghai")));
        user.setModifyTime(LocalDateTime.now(ZoneId.of("Asia/Shanghai")));
        user.setDeleted(CommonConstant.ENTITY_NORMAL);
        userMapper.insert(user);

        // 插入用户角色关联实体
        TrUserRole trUserRole = new TrUserRole();
        trUserRole.setUserId(user.getId());
        trUserRole.setRoleId(dto.getRoleId());
        trUserRoleService.saveTrUserRole(trUserRole);

    }

    // ********************************删除类接口********************************

    /**
     * 后台批量物理删除用户，规则如下：<br/>
     * <p>
     * 1、只有超级管理员能物理删除（校验role角色）
     * 2、无法删除lingjiatong账号
     *
     * @param dto         后台批量物理删除用户DTO对象
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
            throw new PermissionException(ErrorEnum.NO_PERMISSION_ERROR.getCode(), UserErrorMessageConstant.DELETE_SUPER_ADMIN_ERROR_MESSAGE);
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

        // TODO 删除用户产生的所有其他资源
        // 异步删除用户的登陆日志
        commonThreadPool.submit(() -> userLoginLogService.deleteUserLoginLogByUserIdList(userIdList));

        // 删除用户缓存
        Set<String> userInfoKeySet = userIdList.stream().map(userId -> RedisCacheKeyEnum.USER_INFO.getValue() + userId).collect(Collectors.toSet());
        if (!CollectionUtils.isEmpty(userInfoKeySet)) {
            redisUtil.deleteObjects(userInfoKeySet);
        }
    }


    // ********************************修改类接口********************************

    /**
     * 批量更新用户删除状态，更新规则：<br/>
     * <p>
     * 1、lingjiatong账号不能被删除 <br/>
     *
     * @param dto         后台批量更改用户删除状态DTO对象
     * @param currentUser 当前用户
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateUserDeleteStatusBatch(BackendUserUpdateDeleteStatusBatchDTO dto, User currentUser) {
        List<Long> userIdList = dto.getUserIdList();
        Byte deleteStatus = dto.getDeleteStatus();
        if (CollectionUtils.isEmpty(userIdList)) {
            return;
        }
        if (!CommonConstant.deleteValues().contains(deleteStatus)) {
            throw new ParamErrorException(ErrorEnum.REQUEST_PARAM_ERROR.getCode(), UserErrorMessageConstant.DELETE_STATUS_NOT_SUPPORT_ERROR_MESSAGE);
        }
        // 如果是删除操作
        if (CommonConstant.ENTITY_DELETE.equals(deleteStatus)) {
            // 如果删除的用户中含有系统内置超级管理员，则无法隐藏
            if (userIdList.contains(UserConstant.SUPER_ADMIN_USER_ID)) {
                throw new PermissionException(ErrorEnum.NO_PERMISSION_ERROR.getCode(), UserErrorMessageConstant.HIDDEN_SUPER_ADMIN_ERROR_MESSAGE);
            }
            try {
                // 更新用户删除状态
                userMapper.updateUserDeleteStatusBatch(dto);
                // TODO 如果是删除操作将用户相关的文章、评论、等都设置为隐藏
            } catch (Exception e) {
                log.error(e.toString(), e);
                throw new ServerException(ErrorEnum.DATABASE_OPERATION_ERROR);
            }
            // 删除用户缓存
            Set<String> userInfoKeySet = userIdList.stream().map(userId -> RedisCacheKeyEnum.USER_INFO.getValue() + userId).collect(Collectors.toSet());
            if (!CollectionUtils.isEmpty(userInfoKeySet)) {
                redisUtil.deleteObjects(userInfoKeySet);
            }
        } else {
            try {
                // 更新用户删除状态
                userMapper.updateUserDeleteStatusBatch(dto);
            } catch (Exception e) {
                log.error(e.toString(), e);
                throw new ServerException(ErrorEnum.DATABASE_OPERATION_ERROR);
            }
        }
    }

    /**
     * 个人更新用户信息
     * 个人能够编辑的字段：用户名、邮箱、手机号
     *
     * @param dto         后台编辑用户信息DTO对象
     * @param currentUser 当前用户实体
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateUser(BackendAdminUpdateUserDTO dto, User currentUser) {
//        // 校验参数
//        checkBackendUserUpdateDTO(dto, Boolean.FALSE);
//        // 校验需要更新信息的用户是否是当前用户，如果不是，那么报错
//        if (!currentUser.getId().equals(dto.getUserId())) {
//            throw new PermissionException(ErrorEnum.CAN_NOT_UPDATE_OTHER_USER_ERROR);
//        }
//        // 更新用户信息
//        try {
//            userMapper.update(null, new LambdaUpdateWrapper<User>().set(StringUtils.hasLength(dto.getUsername()), User::getUsername, dto.getUsername()).set(StringUtils.hasLength(dto.getEmail()), User::getEmail, dto.getEmail()).set(StringUtils.hasLength(dto.getPhone()), User::getPhone, dto.getPhone()).eq(User::getId, dto.getUserId()));
//        } catch (Exception e) {
//            log.error(e.toString(), e);
//            throw new ServerException(ErrorEnum.DATABASE_OPERATION_ERROR);
//        }
    }

    /**
     * 管理员更改用户信息
     *
     * @param dto 后台编辑用户信息DTO对象
     */
    @Transactional(rollbackFor = Exception.class)
    public void adminEditUser(BackendAdminUpdateUserDTO dto, User currentUser) {
        // 校验用户是否是超级管理员
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

        // 后台管理员编辑用户信息DTO对象
        checkBackendAdminUpdateUserDTO(dto);
        try {
            // 更新用户信息
            LambdaUpdateWrapper<User> userLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
            userLambdaUpdateWrapper.eq(User::getId, dto.getUserId());
            userLambdaUpdateWrapper.set(User::getEmail, dto.getEmail());
            if (StringUtils.hasLength(dto.getPassword())) {
                userLambdaUpdateWrapper.set(User::getPassword, EncryptUtil.getInstance().getMd5LowerCase(dto.getPassword()));
            }
            userMapper.update(null, userLambdaUpdateWrapper);
            // 先删除原来用户的角色信息
            trUserRoleService.deleteTrUserRoleBatchByUserIdList(List.of(dto.getUserId()));
            // 新增用户角色关联信息
            TrUserRole trUserRole = new TrUserRole();
            trUserRole.setUserId(dto.getUserId());
            trUserRole.setRoleId(dto.getRoleId());
            trUserRoleService.saveTrUserRole(trUserRole);
        } catch (Exception e) {
            log.error(e.toString(), e);
            throw new ServerException(ErrorEnum.DATABASE_OPERATION_ERROR);
        }
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
        dto.generateOrderCondition();
        Page page = new Page<>(dto.getPageNum(), dto.getPageSize());
        // 不查询总数
        page.setSearchCount(false);
        Page<BackendUserListVO> userList = userMapper.findUserList(new Page<>(dto.getPageNum(), dto.getPageSize()), dto);
        long total = userMapper.findUserListTotal(dto);
        page.setTotal(total);

        // 查询每个用户最后一次登录的信息数据
        List<Long> userIdList = userList.getRecords().stream().map(vo -> Long.valueOf(vo.getId())).collect(Collectors.toList());
        List<UserLoginLog> logList = userLoginLogService.findUserLastLoginLogListByUserIdList(userIdList);
        if (!CollectionUtils.isEmpty(logList)) {
            Map<Long, List<UserLoginLog>> map = logList.stream().collect(Collectors.groupingBy(UserLoginLog::getUserId));
            userList.getRecords().forEach(user -> {
                List<UserLoginLog> userLoginLogList = map.get(Long.valueOf(user.getId()));
                if (!CollectionUtils.isEmpty(userLoginLogList)) {
                    UserLoginLog userLoginLog = map.get(Long.valueOf(user.getId())).get(0);
                    user.setIp(userLoginLog.getIp());
                    user.setLastLoginTime(userLoginLog.getLoginTime());
                    user.setBrowserUA(userLoginLog.getUa());
                }
            });
        }

        return userList;
    }

    /**
     * 校验用户名是否重复
     *
     * @param username    用户名
     * @param currentUser 当前用户
     * @return 重复返回true，不重复返回false
     */
    @Transactional(readOnly = true)
    public Boolean testUsernameDuplicate(String username, User currentUser) {
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getUsername, username).eq(User::getDeleted, CommonConstant.ENTITY_NORMAL));
        return user != null;
    }

    /**
     * 校验邮箱是否重复
     *
     * @param email       邮箱
     * @param currentUser 当前用户
     * @return 重复返回true, 不重复返回false
     */
    @Transactional(readOnly = true)
    public Boolean testEmailDuplicate(String email, User currentUser) {
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getEmail, email).eq(User::getDeleted, CommonConstant.ENTITY_NORMAL));
        return user != null;
    }

    /**
     * 管理员编辑用户表单测试邮箱是否可用
     *
     * @param dto 后台管理员用户编辑用户信息表单测试邮箱是否可用DTO对象
     * @param currentUser 当前登陆用户
     * @return 可用返回true，不可用返回false
     */
    @Transactional(readOnly = true)
    public Boolean adminEditUserTestEmailAvailability(BackendAdminEditUserEmailTestAvailabilityDTO dto, User currentUser) {
        String email = dto.getEmail();
        Long userId = dto.getUserId();
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getEmail, email)
                .ne(User::getId, userId));
        return user == null;
    }

    // ********************************私有函数********************************

    /**
     * 校验后台管理员编辑用户信息DTO对象
     *
     * @param dto 后台管理员编辑用户信息DTO对象
     */
    private void checkBackendAdminUpdateUserDTO(BackendAdminUpdateUserDTO dto) {
        Long userId = dto.getUserId();
        String email = dto.getEmail();
        String password = dto.getPassword();
        Long roleId = dto.getRoleId();

        // 判空校验
        if (userId == null) {
            throw new ParamErrorException(ErrorEnum.ILLEGAL_PARAM_ERROR.getCode(), UserErrorMessageConstant.USER_ID_EMPTY_ERROR_MESSAGE);
        }
        if (!StringUtils.hasLength(email)) {
            throw new ParamErrorException(ErrorEnum.ILLEGAL_PARAM_ERROR.getCode(), UserErrorMessageConstant.EMAIL_EMPTY_ERROR_MESSAGE);
        }
        // 正则规则校验
        if (StringUtils.hasLength(password)) {
            if (!UserRegexConstant.PASSWORD_REGEX.matcher(password).matches()) {
                throw new ParamErrorException(ErrorEnum.ILLEGAL_PARAM_ERROR.getCode(), UserErrorMessageConstant.PASSWORD_FORMAT_ERROR_MESSAGE);
            }
        }
        if (!UserRegexConstant.EMAIL_REGEX.matcher(email).matches()) {
            throw new ParamErrorException(ErrorEnum.ILLEGAL_PARAM_ERROR.getCode(), UserErrorMessageConstant.EMAIL_FORMAT_ERROR_MESSAGE);
        }
        // 判断用户是否存在
        if (!isExist(userId)) {
            throw new ResourceNotExistException(ErrorEnum.RESOURCE_NOT_EXIST_ERROR.getCode(), UserErrorMessageConstant.USER_NOT_EXIST_ERROR_MESSAGE);
        }
        // 判断邮箱可用性
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getEmail, email)
                .ne(User::getId, userId));
        if (user != null) {
            throw new BusinessException(ErrorEnum.EMAIL_OCCUPY_BY_OTHER_USER_ERROR);
        }
        // 校验角色是否存在
        if (!backendRoleService.isRoleExist(roleId)) {
            throw new ResourceNotExistException(ErrorEnum.RESOURCE_NOT_EXIST_ERROR.getCode(), UserErrorMessageConstant.ROLE_NOT_EXIST_ERROR_MESSAGE);
        }
    }


    /**
     * 校验后台更新用户DTO对象
     * 个人能编辑的字段：用户名、邮箱、手机号
     * 管理员能编辑的信息：用户名、密码、邮箱、手机号、用户角色列表
     *
     * @param dto         后台编辑用户信息DTO对象
     * @param adminUpdate 是否是管理员编辑 true 是 false 不是
     */
    private void checkBackendUserUpdateDTO(BackendAdminUpdateUserDTO dto, boolean adminUpdate) {
//        Long userId = dto.getUserId();
//        String username = dto.getUsername();
//        String email = dto.getEmail();
//        String phone = dto.getPhone();
        // TODO 校验逻辑

    }

    /**
     * 校验后台保存用户DTO对象
     *
     * @param dto 后台保存用户DTO对象
     */
    private void checkBackendUserSaveDTO(BackendUserSaveDTO dto) {
        String username = dto.getUsername();
        String password = dto.getPassword();
        String avatarUrl = dto.getAvatarUrl();
        String email = dto.getEmail();
        Long roleId = dto.getRoleId();
        // 判空校验
        if (!StringUtils.hasLength(username)) {
            throw new ParamErrorException(ErrorEnum.ILLEGAL_PARAM_ERROR.getCode(), UserErrorMessageConstant.USERNAME_EMPTY_ERROR_MESSAGE);
        }
        if (!StringUtils.hasLength(password)) {
            throw new ParamErrorException(ErrorEnum.ILLEGAL_PARAM_ERROR.getCode(), UserErrorMessageConstant.PASSWORD_EMPTY_ERROR_MESSAGE);
        }
        if (!StringUtils.hasLength(email)) {
            throw new ParamErrorException(ErrorEnum.ILLEGAL_PARAM_ERROR.getCode(), UserErrorMessageConstant.EMAIL_EMPTY_ERROR_MESSAGE);
        }
        if (!StringUtils.hasLength(avatarUrl)) {
            // 默认用户头像, 目前从阿尼亚图片中随机选一个，后面可以改为其他的
            Random random = new Random();
            int index = random.nextInt(40);
            avatarUrl = UserConstant.DEFAULT_USER_AVATAR_LIST.get(index);
            dto.setAvatarUrl(avatarUrl);
        }
        // 正则规则校验
        if (!UserRegexConstant.USERNAME_REGEX.matcher(username).matches()) {
            throw new ParamErrorException(ErrorEnum.ILLEGAL_PARAM_ERROR.getCode(), UserErrorMessageConstant.USERNAME_FORMAT_ERROR_MESSAGE);
        }
        if (!UserRegexConstant.PASSWORD_REGEX.matcher(password).matches()) {
            throw new ParamErrorException(ErrorEnum.ILLEGAL_PARAM_ERROR.getCode(), UserErrorMessageConstant.PASSWORD_FORMAT_ERROR_MESSAGE);
        }
        if (!UserRegexConstant.EMAIL_REGEX.matcher(email).matches()) {
            throw new ParamErrorException(ErrorEnum.ILLEGAL_PARAM_ERROR.getCode(), UserErrorMessageConstant.EMAIL_FORMAT_ERROR_MESSAGE);
        }
        // 校验角色是否存在
        if (!backendRoleService.isRoleExist(roleId)) {
            throw new ResourceNotExistException(ErrorEnum.RESOURCE_NOT_EXIST_ERROR.getCode(), UserErrorMessageConstant.ROLE_NOT_EXIST_ERROR_MESSAGE);
        }
    }

    // ********************************公用函数********************************

    /**
     * 根据用户id列表获取用户列表
     *
     * @param userIdList 用户id列表
     * @param fields     要获取的字段列表
     * @return 后台获取用户列表VO对象列表
     */
    public List<BackendUserListVO> findUserListByUserIdList(List<Long> userIdList, SFunction<User, ?>... fields) {
        if (CollectionUtils.isEmpty(userIdList)) {
            return List.of();
        }
        List<User> users;
        if (fields == null || fields.length == 0) {
            users = userMapper.selectList(new LambdaQueryWrapper<User>().in(User::getId, userIdList));
        } else {
            users = userMapper.selectList(new LambdaQueryWrapper<User>().select(fields).in(User::getId, userIdList));
        }
        return users.stream().map(user -> {
            BackendUserListVO vo = new BackendUserListVO();
            BeanUtils.copyProperties(user, vo);
            vo.setId(String.valueOf(user.getId()));
            return vo;
        }).collect(Collectors.toList());
    }

    /**
     * 判断用户是否存在
     *
     * @param userId 用户id
     * @return 存在返回true, 不存在返回false
     */
    public boolean isExist(Long userId) {
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>()
                .select(User::getId)
                .eq(User::getId, userId));
        return user != null;
    }

    /**
     * 根据用户名查询用户完整信息（内部调用，供CurrentUser解析器使用）
     *
     * @param username 用户名
     * @return 用户实体（含角色和权限，密码已置空）
     */
    @Transactional(readOnly = true)
    public User getCurrentUserByUsername(String username) {
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, username)
                .eq(User::getDeleted, CommonConstant.ENTITY_NORMAL));
        if (user == null) {
            return null;
        }
        user.setPassword(null);

        // 查询用户角色
        List<TrUserRole> trUserRoleList = trUserRoleMapper.selectList(new LambdaQueryWrapper<TrUserRole>()
                .eq(TrUserRole::getUserId, user.getId()));
        if (!CollectionUtils.isEmpty(trUserRoleList)) {
            List<Long> roleIdList = trUserRoleList.stream()
                    .map(TrUserRole::getRoleId)
                    .distinct()
                    .collect(Collectors.toList());
            List<Role> roles = roleMapper.selectList(new LambdaQueryWrapper<Role>()
                    .in(Role::getId, roleIdList));
            user.setRoles(roles);

            // 查询角色关联的权限
            List<TrRolePermission> trRolePermissionList = trRolePermissionMapper.selectList(new LambdaQueryWrapper<TrRolePermission>()
                    .in(TrRolePermission::getRoleId, roleIdList));
            if (!CollectionUtils.isEmpty(trRolePermissionList)) {
                List<Long> permissionIdList = trRolePermissionList.stream()
                        .map(TrRolePermission::getPermissionId)
                        .distinct()
                        .collect(Collectors.toList());
                List<Permission> permissions = permissionMapper.selectList(new LambdaQueryWrapper<Permission>()
                        .in(Permission::getId, permissionIdList)
                        .eq(Permission::getProjectName, CommonConstant.PROJECT_NAME_BACKEND_PAGE));
                user.setPermissions(permissions);
            }
        }

        return user;
    }


    // ********************************查询类接口********************************

    /**
     * 发送绑定邮箱验证码
     *
     * @param dto 后台绑定邮箱发送验证码DTO对象
     * @param currentUser 当前登录用户
     */
    public void sendBindEmailCode(BackendUserBindEmailSendCodeDTO dto, User currentUser) {
        String email = dto.getEmail();
        if (!StringUtils.hasLength(email)) {
            throw new ParamErrorException(ErrorEnum.ILLEGAL_PARAM_ERROR.getCode(), UserErrorMessageConstant.EMAIL_EMPTY_ERROR_MESSAGE);
        }
        if (!UserRegexConstant.EMAIL_REGEX.matcher(email).matches()) {
            throw new ParamErrorException(ErrorEnum.ILLEGAL_PARAM_ERROR.getCode(), UserErrorMessageConstant.EMAIL_FORMAT_ERROR_MESSAGE);
        }
        // 判断邮箱是否被其他用户占用
        Long emailUsedCount = userMapper.selectCount(new LambdaQueryWrapper<User>()
                .eq(User::getEmail, email)
                .eq(User::getDeleted, CommonConstant.ENTITY_NORMAL));
        if (emailUsedCount != null && emailUsedCount > 0) {
            throw new BusinessException(ErrorEnum.EMAIL_OCCUPY_BY_OTHER_USER_ERROR);
        }
        // 发送频率限制：60秒内只能发送一次
        String rateLimitKey = RedisCacheKeyEnum.EMAIL_BIND_SEND_RATE_LIMIT.getValue() + currentUser.getId();
        if (redisUtil.getCacheObject(rateLimitKey) != null) {
            throw new BusinessException(ErrorEnum.EMAIL_SEND_TOO_FREQUENT_ERROR);
        }
        // 生成6位数字验证码，5分钟有效，同时记录目标邮箱防止换绑
        String code = generateEmailCode();
        String codeKey = RedisCacheKeyEnum.EMAIL_BIND_CODE.getValue() + currentUser.getId();
        redisUtil.setCacheObject(codeKey, email + ":" + code, 5, TimeUnit.MINUTES);
        redisUtil.setCacheObject(rateLimitKey, 1, 60, TimeUnit.SECONDS);
        mailService.sendVerifyCode(email, "绑定邮箱", code);
    }

    /**
     * 确认绑定邮箱
     *
     * @param dto 后台绑定邮箱确认DTO对象
     * @param currentUser 当前登录用户
     */
    public void bindEmail(BackendUserBindEmailConfirmDTO dto, User currentUser) {
        String email = dto.getEmail();
        String code = dto.getCode();
        if (!StringUtils.hasLength(email) || !StringUtils.hasLength(code)) {
            throw new ParamErrorException(ErrorEnum.ILLEGAL_PARAM_ERROR.getCode(), "邮箱和验证码不能为空");
        }
        // 校验验证码
        String codeKey = RedisCacheKeyEnum.EMAIL_BIND_CODE.getValue() + currentUser.getId();
        Object cached = redisUtil.getCacheObject(codeKey);
        if (cached == null || !cached.equals(email + ":" + code)) {
            throw new BusinessException(ErrorEnum.EMAIL_VERIFY_CODE_ERROR);
        }
        // 再次判断邮箱是否被其他用户占用
        Long emailUsedCount = userMapper.selectCount(new LambdaQueryWrapper<User>()
                .eq(User::getEmail, email)
                .eq(User::getDeleted, CommonConstant.ENTITY_NORMAL));
        if (emailUsedCount != null && emailUsedCount > 0) {
            throw new BusinessException(ErrorEnum.EMAIL_OCCUPY_BY_OTHER_USER_ERROR);
        }
        // 绑定邮箱
        User updateUser = new User();
        updateUser.setId(currentUser.getId());
        updateUser.setEmail(email);
        updateUser.setModifyTime(LocalDateTime.now());
        userMapper.updateById(updateUser);
        redisUtil.deleteObject(codeKey);
    }

    /**
     * 发送修改密码邮箱验证码（发送到已绑定邮箱）
     *
     * @param currentUser 当前登录用户
     */
    public void sendUpdatePasswordEmailCode(User currentUser) {
        User user = userMapper.selectById(currentUser.getId());
        if (user == null || !StringUtils.hasLength(user.getEmail())) {
            throw new BusinessException(ErrorEnum.EMAIL_NOT_BOUND_ERROR);
        }
        // 发送频率限制：60秒内只能发送一次
        String rateLimitKey = RedisCacheKeyEnum.EMAIL_UPDATE_PASSWORD_SEND_RATE_LIMIT.getValue() + currentUser.getId();
        if (redisUtil.getCacheObject(rateLimitKey) != null) {
            throw new BusinessException(ErrorEnum.EMAIL_SEND_TOO_FREQUENT_ERROR);
        }
        String code = generateEmailCode();
        String codeKey = RedisCacheKeyEnum.EMAIL_UPDATE_PASSWORD_CODE.getValue() + currentUser.getId();
        redisUtil.setCacheObject(codeKey, code, 5, TimeUnit.MINUTES);
        redisUtil.setCacheObject(rateLimitKey, 1, 60, TimeUnit.SECONDS);
        mailService.sendVerifyCode(user.getEmail(), "修改密码", code);
    }

    /**
     * 个人修改密码
     *
     * @param dto 后台个人修改密码DTO对象
     * @param currentUser 当前登录用户
     */
    public void updatePassword(BackendUserUpdatePasswordDTO dto, User currentUser) {
        String oldPassword = dto.getOldPassword();
        String newPassword = dto.getNewPassword();
        String emailCode = dto.getEmailCode();
        if (!StringUtils.hasLength(oldPassword) || !StringUtils.hasLength(newPassword) || !StringUtils.hasLength(emailCode)) {
            throw new ParamErrorException(ErrorEnum.ILLEGAL_PARAM_ERROR.getCode(), "当前密码、新密码和邮箱验证码均不能为空");
        }
        if (!UserRegexConstant.PASSWORD_REGEX.matcher(newPassword).matches()) {
            throw new ParamErrorException(ErrorEnum.ILLEGAL_PARAM_ERROR.getCode(), UserErrorMessageConstant.PASSWORD_FORMAT_ERROR_MESSAGE);
        }
        // 校验邮箱验证码
        String codeKey = RedisCacheKeyEnum.EMAIL_UPDATE_PASSWORD_CODE.getValue() + currentUser.getId();
        Object cached = redisUtil.getCacheObject(codeKey);
        if (cached == null || !cached.equals(emailCode)) {
            throw new BusinessException(ErrorEnum.EMAIL_VERIFY_CODE_ERROR);
        }
        // 校验当前密码（数据库存储为MD5加密形式）
        User user = userMapper.selectById(currentUser.getId());
        if (user == null) {
            throw new ResourceNotExistException(ErrorEnum.RESOURCE_NOT_EXIST_ERROR.getCode(), UserErrorMessageConstant.USER_NOT_EXIST_ERROR_MESSAGE);
        }
        if (!EncryptUtil.getInstance().getMd5LowerCase(oldPassword).equalsIgnoreCase(user.getPassword())) {
            throw new BusinessException(ErrorEnum.OLD_PASSWORD_ERROR);
        }
        // 更新密码
        User updateUser = new User();
        updateUser.setId(currentUser.getId());
        updateUser.setPassword(EncryptUtil.getInstance().getMd5LowerCase(newPassword));
        updateUser.setModifyTime(LocalDateTime.now());
        userMapper.updateById(updateUser);
        redisUtil.deleteObject(codeKey);
    }

    /**
     * 生成6位数字邮箱验证码
     *
     * @return 验证码
     */
    private String generateEmailCode() {
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            code.append(new Random().nextInt(10));
        }
        return code.toString();
    }
}
