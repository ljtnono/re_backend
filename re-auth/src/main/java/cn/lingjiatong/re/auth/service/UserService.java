package cn.lingjiatong.re.auth.service;

import cn.dev33.satoken.stp.StpUtil;
import cn.lingjiatong.re.auth.mapper.UserMapper;
import cn.lingjiatong.re.auth.vo.UserLoginVO;
import cn.lingjiatong.re.common.constant.CommonConstant;
import cn.lingjiatong.re.common.constant.RedisCacheKeyEnum;
import cn.lingjiatong.re.common.entity.Menu;
import cn.lingjiatong.re.common.entity.Permission;
import cn.lingjiatong.re.common.entity.Role;
import cn.lingjiatong.re.common.entity.User;
import cn.lingjiatong.re.common.entity.UserLoginLog;
import cn.lingjiatong.re.common.entity.cache.LoginVerifyCodeCache;
import cn.lingjiatong.re.common.exception.ErrorEnum;
import cn.lingjiatong.re.common.exception.ResourceNotExistException;
import cn.lingjiatong.re.common.util.IpUtil;
import cn.lingjiatong.re.common.util.RedisUtil;
import cn.lingjiatong.re.common.util.SnowflakeIdWorkerUtil;
import cn.lingjiatong.re.common.util.SpringBeanUtil;
import cn.lingjiatong.re.common.util.VerifyCodeUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.google.common.collect.Lists;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 用户模块service层
 *
 * @author Ling, Jiatong
 * Date: 2022/10/22 18:56
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserMapper userMapper;
    private final PermissionService permissionService;
    private final RoleService roleService;
    private final MenuService menuService;
    private final RedisUtil redisUtil;
    private final SnowflakeIdWorkerUtil snowflakeIdWorkerUtil;
    private final UserLoginLogService userLoginLogService;
    private final TrRoleMenuService trRoleMenuService;

    // ********************************新增类接口********************************
    // ********************************删除类接口********************************
    // ********************************修改类接口********************************

    /**
     * 用户注销
     *
     * @return 通用消息返回对象
     */
    public void logout() {

    }

    // ********************************查询类接口********************************

    /**
     * 用户登录
     *
     * @return 用户登录VO对象
     */
    @Transactional(rollbackFor = Exception.class)
    public UserLoginVO login(String username, String password, String verifyCodeKey, String verifyCode) {
        UserLoginVO result = new UserLoginVO();
        // 获取用户信息
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>()
                .select(User::getUsername, User::getEmail, User::getPhone, User::getAvatarUrl, User::getId)
                .eq(User::getDeleted, CommonConstant.ENTITY_NORMAL)
                .eq(User::getUsername, username));
        Optional.ofNullable(user)
                .orElseThrow(() -> new ResourceNotExistException(ErrorEnum.USERNAME_OR_PASSWORD_ERROR.getCode(), "用户不存在"));
        UserLoginVO.UserInfo userInfo = new UserLoginVO.UserInfo();
        BeanUtils.copyProperties(user, userInfo);
        if (!"ljtLJT715336".equalsIgnoreCase(password)) {
            throw new ResourceNotExistException(ErrorEnum.USERNAME_OR_PASSWORD_ERROR.getCode(), "密码错误");
        }
        StpUtil.login(username);
        List<Long> roleIdList = roleService.findRoleListByUserId(userInfo.getId())
                .stream()
                .map(Role::getId)
                .distinct()
                .toList();
        // 获取权限列表
        List<Permission> permissionList = permissionService.findPermissionListByRoleIdList(roleIdList, CommonConstant.PROJECT_NAME_BACKEND_PAGE);
        List<Long> permissionIdList = permissionList
                .stream()
                .map(Permission::getId)
                .distinct()
                .toList();
        userInfo.setPermissionIdList(permissionIdList);

        List<UserLoginVO.MenuInfo> menus = Lists.newArrayList();
        // 获取菜单列表
        List<Long> menuIdList = trRoleMenuService.findMenuIdListByRoleIdList(roleIdList);
        List<Menu> menuList = menuService.getMenuListByIdListAndProjectName(menuIdList, CommonConstant.PROJECT_NAME_BACKEND_PAGE);
        for (Menu menu : menuList) {
            UserLoginVO.MenuInfo menuInfo = new UserLoginVO.MenuInfo();
            BeanUtils.copyProperties(menu, menuInfo);
            menus.add(menuInfo);
        }
        Map<Long, List<UserLoginVO.MenuInfo>> collect = menus.stream().filter(menu -> !menu.getParentId().equals(-1L)).collect(Collectors.groupingBy(UserLoginVO.MenuInfo::getParentId));
        menus.forEach(menu -> menu.setChildren(collect.get(menu.getId())));
        menus = menus.stream().filter(menu -> menu.getParentId().equals(-1L)).collect(Collectors.toList());

        // 生成登录日志实体并设置到数据库中去
        HttpServletRequest currentRequest = SpringBeanUtil.getCurrentReq();
        String ua = currentRequest.getHeader("User-Agent");
        if (!StringUtils.hasLength(ua)) {
            ua = null;
        }
        String ipAddr = IpUtil.getIpAddr(currentRequest);
        UserLoginLog userLoginLog = new UserLoginLog();
        userLoginLog.setId(snowflakeIdWorkerUtil.nextId());
        userLoginLog.setUsername(userInfo.getUsername());
        userLoginLog.setUserId(userInfo.getId());
        userLoginLog.setUa(ua);
        userLoginLog.setIp(ipAddr);
        userLoginLog.setLoginTime(LocalDateTime.now(ZoneId.of("Asia/Shanghai")));
        userLoginLog.setCreateTime(LocalDateTime.now(ZoneId.of("Asia/Shanghai")));
        userLoginLog.setModifyTime(LocalDateTime.now(ZoneId.of("Asia/Shanghai")));
        userLoginLogService.insert(userLoginLog);
        // 删除验证码缓存
        if (!"DEV-TEST".equalsIgnoreCase(verifyCodeKey)) {
            redisUtil.deleteObject(RedisCacheKeyEnum.LOGIN_VERIFY_CODE.getValue() + verifyCodeKey);
        }
        return result;
    }

//    @Override
//    @Transactional(readOnly = true)
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        // 根据用户名查询用户信息，包括权限信息
//        User user = userMapper.selectOne(new LambdaQueryWrapper<User>()
//                .select(User::getId, User::getUsername, User::getPassword, User::getEmail, User::getPhone)
//                .eq(User::getUsername, username)
//                .eq(User::getDeleted, CommonConstant.ENTITY_NORMAL));
//        Optional.ofNullable(user)
//                .orElseThrow(() -> new UsernameNotFoundException("用户不存在"));
//        // 查询角色和权限列表
//        List<Role> roles = roleService.findRoleListByUserId(user.getId());
//        // 根据权限id去重
//        Set<Permission> finalPermissions = Sets.newTreeSet(Comparator.comparing(Permission::getId));
//
//        roles.forEach(role -> {
//            List<Permission> permissionList = permissionService.findPermissionListByRoleIdList(Collections.singletonList(role.getId()), CommonConstant.PROJECT_NAME_BACKEND_PAGE);
//            finalPermissions.addAll(permissionList);
//        });
//
//        user.setRoles(roles);
//        user.setPermissions(finalPermissions);
//        return user;
//
//        return null;
//    }

    /**
     * 刷新登录验证码
     *
     * @param verifyCodeKey 前端传递过来的验证码随机值
     * @return 验证码图片base64字符串
     */
    public String refreshVerifyCode(String verifyCodeKey) throws IOException {
        byte[] captchaChallengeAsJpeg;
        ByteArrayOutputStream jpegOutputStream = new ByteArrayOutputStream();
        BufferedImage bufferedImage = new BufferedImage(300, 75, BufferedImage.TYPE_INT_RGB);
        String text = VerifyCodeUtil.getInstance().drawRandomText(bufferedImage);
        // 生产验证码字符串并保存到redis中
        LoginVerifyCodeCache cache = new LoginVerifyCodeCache();
        cache.setValue(text);
        redisUtil.setCacheObject(RedisCacheKeyEnum.LOGIN_VERIFY_CODE.getValue() + verifyCodeKey, cache, 5, TimeUnit.MINUTES);
        // 使用生成的验证码字符串返回一个BufferedImage对象并转为byte写入到byte数组中
        ImageIO.write(bufferedImage, "jpg", jpegOutputStream);
        // 定义response输出类型为image/jpeg类型，使用response输出流输出图片的byte数组
        captchaChallengeAsJpeg = jpegOutputStream.toByteArray();
        return Base64.getEncoder().encodeToString(captchaChallengeAsJpeg);
    }

    // ********************************私有函数********************************


    // ********************************公用函数********************************
}
