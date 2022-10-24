package cn.lingjiatong.re.auth.component;

import cn.lingjiatong.re.auth.config.ReSecurityProperties;
import cn.lingjiatong.re.auth.constant.AuthErrorMessageConstant;
import cn.lingjiatong.re.auth.dto.LoginDTO;
import cn.lingjiatong.re.auth.config.JwtUtil;
import cn.lingjiatong.re.common.constant.CommonConstant;
import cn.lingjiatong.re.common.constant.RedisCacheKeyEnum;
import cn.lingjiatong.re.common.entity.Permission;
import cn.lingjiatong.re.common.entity.Role;
import cn.lingjiatong.re.common.entity.User;
import cn.lingjiatong.re.common.entity.cache.LoginVerifyCodeCache;
import cn.lingjiatong.re.common.entity.cache.UserInfoCache;
import cn.lingjiatong.re.common.exception.BusinessException;
import cn.lingjiatong.re.common.exception.ErrorEnum;
import cn.lingjiatong.re.common.exception.ParamErrorException;
import cn.lingjiatong.re.common.exception.ResourceNotExistException;
import cn.lingjiatong.re.common.mapper.PermissionMapper;
import cn.lingjiatong.re.common.mapper.RoleMapper;
import cn.lingjiatong.re.common.mapper.UserMapper;
import cn.lingjiatong.re.common.util.DateUtil;
import cn.lingjiatong.re.common.util.RedisUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 用户模块service层
 *
 * @author Ling, Jiatong
 * Date: 2022/10/22 18:56
 */
@Slf4j
@Service
public class UserService implements UserDetailsService {

    @Resource
    private UserMapper userMapper;
    @Resource
    private RoleMapper roleMapper;
    @Resource
    private PermissionMapper permissionMapper;
    @Autowired
    private PasswordEncoderImpl passwordEncoder;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private ReSecurityProperties reSecurityProperties;
    @Autowired
    private DefaultKaptcha defaultKaptcha;

    // ********************************新增类接口********************************

    // ********************************删除类接口********************************

    // ********************************修改类接口********************************

    // ********************************查询类接口********************************

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 根据用户名查询用户信息，包括权限信息
        if (StringUtils.isEmpty(username)) {
            throw new UsernameNotFoundException("用户名不能为空");
        }
        // 这里包含了已删除的用户
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>()
                .select(User::getId, User::getUsername, User::getPassword, User::getEmail, User::getPhone)
                .eq(User::getUsername, username));
        Optional.ofNullable(user)
                .orElseThrow(() -> new UsernameNotFoundException("用户不存在"));
        // 查询角色和权限列表
        List<Role> roles = roleMapper.findRoleByUserId(user.getId());
        // 根据权限id去重
        Set<Permission> finalPermissions = Sets.newTreeSet(Comparator.comparing(Permission::getId));

        roles.forEach(role -> {
            List<Permission> permissionList = permissionMapper.findPermissionByRoleId(role.getId());
            finalPermissions.addAll(permissionList);
        });

        user.setRoles(roles);
        user.setPermissions(finalPermissions);
        return user;
    }


    /**
     * 用户登录
     *
     * @param dto 用户登录DTO对象
     * @return token
     */
    public String login(LoginDTO dto) {
        String username = dto.getUsername();
        String password = dto.getPassword();
        String verifyCodeKey = dto.getVerifyCodeKey();
        String verifyCodeValue = dto.getVerifyCodeValue();
        // 这里不再校验用户名密码的严格格式，只校验是否为空，因为此处用户名和密码已经录入数据库
        if (!StringUtils.hasLength(username)) {
            throw new ParamErrorException(ErrorEnum.REQUEST_PARAM_ERROR.getCode(), AuthErrorMessageConstant.USERNAME_EMPTY_ERROR_MESSAGE);
        }
        if (!StringUtils.hasLength(password)) {
            throw new ParamErrorException(ErrorEnum.REQUEST_PARAM_ERROR.getCode(), AuthErrorMessageConstant.PASSWORD_EMPTY_ERROR_MESSAGE);
        }
        if (!StringUtils.hasLength(verifyCodeValue)) {
            throw new ParamErrorException(ErrorEnum.REQUEST_PARAM_ERROR.getCode(), AuthErrorMessageConstant.VERIFY_CODE_VALUE_EMPTY_ERROR_MESSAGE);
        }
        // 从redis查找验证码key
        LoginVerifyCodeCache cache = (LoginVerifyCodeCache) redisUtil.getCacheObject(RedisCacheKeyEnum.LOGIN_VERIFY_CODE.getValue() + verifyCodeKey);
        Optional.ofNullable(cache)
                .orElseThrow(() -> new BusinessException(ErrorEnum.LOGIN_VERIFY_CODE_EXPIRED_ERROR));
        // 验证码校验错误
        if (!verifyCodeValue.equalsIgnoreCase(cache.getValue())) {
            throw new ParamErrorException(ErrorEnum.REQUEST_PARAM_ERROR.getCode(), AuthErrorMessageConstant.VERIFY_CODE_ERROR_MESSAGE);
        }

        // 获取用户信息
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>()
                .select(User::getId, User::getUsername, User::getEmail, User::getPhone)
                .eq(User::getUsername, username)
                .eq(User::getDeleted, CommonConstant.ENTITY_NORMAL)
                .eq(User::getPassword, passwordEncoder.encode(password)));
        Optional.ofNullable(user)
                .orElseThrow(() -> new ResourceNotExistException(ErrorEnum.RESOURCE_NOT_EXIST_ERROR));

        // 获取用户的角色列表
        List<Role> roles = roleMapper.findRoleByUserId(user.getId());
        Set<Permission> finalPermissions = Sets.newTreeSet(Comparator.comparing(Permission::getId));
        user.setRoles(roles);
        // 获取用户的权限列表
        roles.forEach(role -> {
            List<Permission> permissions = permissionMapper.findPermissionByRoleId(role.getId());
            finalPermissions.addAll(permissions);
        });
        user.setPermissions(finalPermissions);
        // 生成用户的token并放入redis中
        String token = jwtUtil.generateToken(user.getId(), user.getUsername(), user.getEmail(), user.getPhone());
        UserInfoCache userInfoCache = new UserInfoCache();
        userInfoCache.setUserId(user.getId());
        userInfoCache.setUsername(user.getUsername());
        userInfoCache.setRoles(user.getRoles());
        userInfoCache.setEmail(user.getEmail());
        userInfoCache.setPhone(user.getPhone());
        userInfoCache.setPermissions(user.getPermissions());
        userInfoCache.setToken(token);
        userInfoCache.setLoginDate(DateUtil.getLocalDateTimeNow());
        // 设置token到redis中
        redisUtil.setCacheObject(RedisCacheKeyEnum.USER_INFO.getValue() + user.getId(), userInfoCache, reSecurityProperties.getTokenExpireTime(), TimeUnit.HOURS);
        return token;
    }


    /**
     * 刷新登录验证码
     *
     * @param verifyCodeKey 前端传递过来的验证码随机值
     */
    public void refreshVerifyCode(String verifyCodeKey, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {
        byte[] captchaChallengeAsJpeg;
        ByteArrayOutputStream jpegOutputStream = new ByteArrayOutputStream();
        // 生产验证码字符串并保存到redis中
        String createText = defaultKaptcha.createText();
        LoginVerifyCodeCache cache = new LoginVerifyCodeCache();
        cache.setValue(createText);
        redisUtil.setCacheObject(RedisCacheKeyEnum.LOGIN_VERIFY_CODE.getValue() + verifyCodeKey, cache);
        // 使用生成的验证码字符串返回一个BufferedImage对象并转为byte写入到byte数组中
        BufferedImage challenge = defaultKaptcha.createImage(createText);
        ImageIO.write(challenge, "jpg", jpegOutputStream);
        // 定义response输出类型为image/jpeg类型，使用response输出流输出图片的byte数组
        captchaChallengeAsJpeg = jpegOutputStream.toByteArray();
        httpServletResponse.setHeader("Cache-Control", "no-store");
        httpServletResponse.setHeader("Pragma", "no-cache");
        httpServletResponse.setDateHeader("Expires", 0);
        httpServletResponse.setContentType("image/jpeg");
        ServletOutputStream responseOutputStream = httpServletResponse.getOutputStream();
        responseOutputStream.write(captchaChallengeAsJpeg);
        responseOutputStream.flush();
        responseOutputStream.close();
    }
}
