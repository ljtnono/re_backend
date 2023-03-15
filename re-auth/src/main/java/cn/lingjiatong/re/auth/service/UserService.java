package cn.lingjiatong.re.auth.service;

import cn.lingjiatong.re.auth.mapper.UserMapper;
import cn.lingjiatong.re.auth.security.JwtUtil;
import cn.lingjiatong.re.auth.vo.UserLoginVO;
import cn.lingjiatong.re.common.constant.CommonConstant;
import cn.lingjiatong.re.common.constant.RedisCacheKeyEnum;
import cn.lingjiatong.re.common.entity.*;
import cn.lingjiatong.re.common.entity.cache.LoginVerifyCodeCache;
import cn.lingjiatong.re.common.entity.cache.UserInfoCache;
import cn.lingjiatong.re.common.exception.ErrorEnum;
import cn.lingjiatong.re.common.exception.ResourceNotExistException;
import cn.lingjiatong.re.common.util.*;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;
import org.springframework.util.StringUtils;
import org.springframework.web.HttpRequestMethodNotSupportedException;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
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
public class UserService implements UserDetailsService {

    @Resource
    private UserMapper userMapper;
    @Autowired
    private PermissionService permissionService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private MenuService menuService;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private SnowflakeIdWorkerUtil snowflakeIdWorkerUtil;
    @Autowired
    private UserLoginLogService userLoginLogService;

    // ********************************新增类接口********************************
    // ********************************删除类接口********************************
    // ********************************修改类接口********************************

    /**
     * 用户注销
     *
     * @return 通用消息返回对象
     */
    public void logout() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object details = authentication.getDetails();
        if (details instanceof OAuth2AuthenticationDetails) {
            OAuth2AuthenticationDetails oAuth2AuthenticationDetails = (OAuth2AuthenticationDetails) details;
            String token = oAuth2AuthenticationDetails.getTokenValue();
            Claims claims = jwtUtil.getClaimsFromToken(token);
            String username = (String) claims.get("username");
            // 删除redis缓存
            redisUtil.deleteObject(RedisCacheKeyEnum.USER_INFO.getValue() + username);
        }
    }

    // ********************************查询类接口********************************

    /**
     * 用户登录
     *
     * @param principal principal
     * @param parameters 参数列表
     * @return 用户登录VO对象
     */
    @Transactional(rollbackFor = Exception.class)
    public UserLoginVO login(Principal principal, Map<String, String> parameters, TokenEndpoint tokenEndpoint) throws HttpRequestMethodNotSupportedException {
        UserLoginVO result = new UserLoginVO();
        UserLoginVO.UserInfo userInfo;
        UserLoginVO.TokenInfo tokenInfo = new UserLoginVO.TokenInfo();
        List<UserLoginVO.MenuInfo> menus = Lists.newArrayList();
        // 获取token信息
        ResponseEntity<OAuth2AccessToken> token = tokenEndpoint.postAccessToken(principal, parameters);
        OAuth2AccessToken tokenBody = token.getBody();
        tokenInfo.setAccessToken(tokenBody.getValue());
        tokenInfo.setTokenType(tokenBody.getTokenType());
        tokenInfo.setExpiresIn(tokenBody.getExpiresIn());
        tokenInfo.setScope(tokenBody.getScope());
        tokenInfo.setRefreshToken(tokenBody.getRefreshToken().getValue());
        tokenInfo.setJti((String) tokenBody.getAdditionalInformation().get("jti"));

        // 获取用户信息
        String username = parameters.get("username");
        userInfo = getUserInfoByUsername(username);
        List<Long> roleIdList = roleService.findRoleListByUserId(userInfo.getId())
                .stream()
                .map(Role::getId)
                .distinct()
                .collect(Collectors.toList());
        // 获取权限列表
        List<Permission> permissionList = permissionService.findPermissionListByRoleIdList(roleIdList, CommonConstant.PROJECT_NAME_BACKEND_PAGE);
        List<Long> permissionIdList = permissionList
                .stream()
                .map(Permission::getId)
                .distinct()
                .collect(Collectors.toList());
        userInfo.setPermissionIdList(permissionIdList);

        // 获取菜单列表
        List<Long> menuIdList = permissionList
                .stream()
                .map(Permission::getMenuId)
                .distinct()
                .collect(Collectors.toList());
        List<Menu> menuList = menuService.getMenuListByIdListAndProjectName(menuIdList, CommonConstant.PROJECT_NAME_BACKEND_PAGE);
        for (Menu menu : menuList) {
            UserLoginVO.MenuInfo menuInfo = new UserLoginVO.MenuInfo();
            BeanUtils.copyProperties(menu,menuInfo);
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

        // 将用户信息设置到redis中去
        UserInfoCache userInfoCache = new UserInfoCache();
        userInfoCache.setId(userInfo.getId());
        userInfoCache.setUsername(username);
        userInfoCache.setEmail(userInfo.getEmail());
        userInfoCache.setPhone(userInfo.getPhone());
        userInfoCache.setAccessToken(tokenBody.getValue());
        userInfoCache.setTokenType(tokenBody.getTokenType());
        userInfoCache.setExpiresIn(tokenBody.getExpiresIn());
        userInfoCache.setScope(tokenBody.getScope());
        userInfoCache.setRefreshToken(tokenBody.getRefreshToken().getValue());
        userInfoCache.setJti((String) tokenBody.getAdditionalInformation().get("jti"));
        userInfoCache.setLoginDate(LocalDateTime.now(ZoneId.of("Asia/Shanghai")));
        userInfoCache.setRoleIdList(roleIdList);
        userInfoCache.setPermissionIdList(permissionIdList);
        String userInfoCacheKey = RedisCacheKeyEnum.USER_INFO.getValue() + username;
        redisUtil.setCacheObject(userInfoCacheKey, userInfoCache, tokenBody.getExpiresIn(), TimeUnit.SECONDS);

        // 删除验证码缓存
        String verifyCodeKey = parameters.get("verifyCodeKey");
        if (!"DEV-TEST".equalsIgnoreCase(verifyCodeKey)) {
            redisUtil.deleteObject(RedisCacheKeyEnum.LOGIN_VERIFY_CODE.getValue() + verifyCodeKey);
        }

        result.setUserInfo(userInfo);
        result.setMenus(menus);
        result.setTokenInfo(tokenInfo);
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 根据用户名查询用户信息，包括权限信息
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>()
                .select(User::getId, User::getUsername, User::getPassword, User::getEmail, User::getPhone)
                .eq(User::getUsername, username)
                .eq(User::getDeleted, CommonConstant.ENTITY_NORMAL));
        Optional.ofNullable(user)
                .orElseThrow(() -> new UsernameNotFoundException("用户不存在"));
        // 查询角色和权限列表
        List<Role> roles = roleService.findRoleListByUserId(user.getId());
        // 根据权限id去重
        Set<Permission> finalPermissions = Sets.newTreeSet(Comparator.comparing(Permission::getId));

        roles.forEach(role -> {
            List<Permission> permissionList = permissionService.findPermissionListByRoleIdList(Collections.singletonList(role.getId()), CommonConstant.PROJECT_NAME_BACKEND_PAGE);
            finalPermissions.addAll(permissionList);
        });

        user.setRoles(roles);
        user.setPermissions(finalPermissions);
        return user;
    }

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
        return Base64Utils.encodeToString(captchaChallengeAsJpeg);
    }

    // ********************************私有函数********************************

    /**
     * 根据用户名获取用户信息
     *
     * @param username 用户名
     * @return 用户信息对象
     */
    private UserLoginVO.UserInfo getUserInfoByUsername(String username) {
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>()
                .select(User::getUsername, User::getEmail, User::getPhone, User::getAvatarUrl, User::getId)
                .eq(User::getDeleted, CommonConstant.ENTITY_NORMAL)
                .eq(User::getUsername, username));
        Optional.ofNullable(user)
                .orElseThrow(() -> new ResourceNotExistException(ErrorEnum.USERNAME_OR_PASSWORD_ERROR));
        UserLoginVO.UserInfo userInfo = new UserLoginVO.UserInfo();
        BeanUtils.copyProperties(user, userInfo);
        return userInfo;
    }




    // ********************************公用函数********************************
}
