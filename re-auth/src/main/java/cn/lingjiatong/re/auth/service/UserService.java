package cn.lingjiatong.re.auth.service;

import cn.lingjiatong.re.auth.mapper.UserMapper;
import cn.lingjiatong.re.auth.security.JwtUtil;
import cn.lingjiatong.re.auth.vo.UserLoginVO;
import cn.lingjiatong.re.common.constant.CommonConstant;
import cn.lingjiatong.re.common.constant.RedisCacheKeyEnum;
import cn.lingjiatong.re.common.entity.Menu;
import cn.lingjiatong.re.common.entity.Permission;
import cn.lingjiatong.re.common.entity.Role;
import cn.lingjiatong.re.common.entity.User;
import cn.lingjiatong.re.common.entity.cache.LoginVerifyCodeCache;
import cn.lingjiatong.re.common.entity.cache.UserInfoCache;
import cn.lingjiatong.re.common.exception.ErrorEnum;
import cn.lingjiatong.re.common.exception.ResourceNotExistException;
import cn.lingjiatong.re.common.util.RedisUtil;
import cn.lingjiatong.re.common.util.VerifyCodeUtil;
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
import org.springframework.web.HttpRequestMethodNotSupportedException;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
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
 * ????????????service???
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
    // ********************************???????????????********************************
    // ********************************???????????????********************************
    // ********************************???????????????********************************

    /**
     * ????????????
     *
     * @return ????????????????????????
     */
    public void logout() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object details = authentication.getDetails();
        if (details instanceof OAuth2AuthenticationDetails) {
            OAuth2AuthenticationDetails oAuth2AuthenticationDetails = (OAuth2AuthenticationDetails) details;
            String token = oAuth2AuthenticationDetails.getTokenValue();
            Claims claims = jwtUtil.getClaimsFromToken(token);
            String username = (String) claims.get("username");
            // ??????redis??????
            redisUtil.deleteObject(RedisCacheKeyEnum.USER_INFO.getValue() + username);
        }
    }

    // ********************************???????????????********************************

    /**
     * ????????????
     *
     * @param principal principal
     * @param parameters ????????????
     * @return ????????????VO??????
     */
    @Transactional(readOnly = true)
    public UserLoginVO login(Principal principal, Map<String, String> parameters, TokenEndpoint tokenEndpoint) throws HttpRequestMethodNotSupportedException {
        UserLoginVO result = new UserLoginVO();
        UserLoginVO.UserInfo userInfo;
        UserLoginVO.TokenInfo tokenInfo = new UserLoginVO.TokenInfo();
        List<UserLoginVO.MenuInfo> menus = Lists.newArrayList();
        // ??????token??????
        ResponseEntity<OAuth2AccessToken> token = tokenEndpoint.postAccessToken(principal, parameters);
        OAuth2AccessToken tokenBody = token.getBody();
        tokenInfo.setAccessToken(tokenBody.getValue());
        tokenInfo.setTokenType(tokenBody.getTokenType());
        tokenInfo.setExpiresIn(tokenBody.getExpiresIn());
        tokenInfo.setScope(tokenBody.getScope());
        tokenInfo.setRefreshToken(tokenBody.getRefreshToken().getValue());
        tokenInfo.setJti((String) tokenBody.getAdditionalInformation().get("jti"));

        // ??????????????????
        String username = parameters.get("username");
        userInfo = getUserInfoByUsername(username);
        List<Long> roleIdList = roleService.findRoleListByUserId(userInfo.getId())
                .stream()
                .map(Role::getId)
                .distinct()
                .collect(Collectors.toList());
        // ??????????????????
        List<Permission> permissionList = permissionService.findPermissionListByRoleIdList(roleIdList, CommonConstant.PROJECT_NAME_BACKEND_PAGE);
        List<Long> permissionIdList = permissionList
                .stream()
                .map(Permission::getId)
                .distinct()
                .collect(Collectors.toList());
        userInfo.setPermissionIdList(permissionIdList);

        // ??????????????????
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

        // ????????????????????????redis??????
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

        // ?????????????????????
        String verifyCodeKey = parameters.get("verifyCodeKey");
        if (!verifyCodeKey.equalsIgnoreCase("DEV-TEST")) {
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
        // ??????????????????????????????????????????????????????
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>()
                .select(User::getId, User::getUsername, User::getPassword, User::getEmail, User::getPhone)
                .eq(User::getUsername, username)
                .eq(User::getDeleted, CommonConstant.ENTITY_NORMAL));
        Optional.ofNullable(user)
                .orElseThrow(() -> new UsernameNotFoundException("???????????????"));
        // ???????????????????????????
        List<Role> roles = roleService.findRoleListByUserId(user.getId());
        // ????????????id??????
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
     * ?????????????????????
     *
     * @param verifyCodeKey ???????????????????????????????????????
     */
    public void refreshVerifyCode(String verifyCodeKey, HttpServletResponse httpServletResponse) throws IOException {
        byte[] captchaChallengeAsJpeg;
        ByteArrayOutputStream jpegOutputStream = new ByteArrayOutputStream();
        BufferedImage bufferedImage = new BufferedImage(300, 75, BufferedImage.TYPE_INT_RGB);
        String text = VerifyCodeUtil.getInstance().drawRandomText(bufferedImage);
        // ????????????????????????????????????redis???
        LoginVerifyCodeCache cache = new LoginVerifyCodeCache();
        cache.setValue(text);
        redisUtil.setCacheObject(RedisCacheKeyEnum.LOGIN_VERIFY_CODE.getValue() + verifyCodeKey, cache, 5, TimeUnit.MINUTES);
        // ?????????????????????????????????????????????BufferedImage???????????????byte?????????byte?????????
        ImageIO.write(bufferedImage, "jpg", jpegOutputStream);
        // ??????response???????????????image/jpeg???????????????response????????????????????????byte??????
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

    // ********************************????????????********************************

    /**
     * ?????????????????????????????????
     *
     * @param username ?????????
     * @return ??????????????????
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




    // ********************************????????????********************************
}
