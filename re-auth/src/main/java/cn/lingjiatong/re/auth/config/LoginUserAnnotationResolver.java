package cn.lingjiatong.re.auth.config;

import cn.lingjiatong.re.common.annotation.LoginUser;
import cn.lingjiatong.re.common.constant.CommonConstant;
import cn.lingjiatong.re.common.entity.Role;
import cn.lingjiatong.re.common.entity.User;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.List;
import java.util.stream.Collectors;

/**
 * LoginUser注解处理类
 *
 * @author Ling, Jiatong
 * Date: 2020/7/10 9:22 上午
 */
@Slf4j
@Component
public class LoginUserAnnotationResolver implements HandlerMethodArgumentResolver {

    private final JwtUtil jwtUtil;

    public LoginUserAnnotationResolver(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        // 是UserDetails 类型，并且打上了注解，那么就注入
        return parameter.getParameterType().isAssignableFrom(User.class) && parameter.hasParameterAnnotation(LoginUser.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        String token = webRequest.getHeader(CommonConstant.TOKE_HTTP_HEADER);
        // token格式错误或者过期，参数类型不正确都会返回null
        if (!StringUtils.hasLength(token) || !token.startsWith(CommonConstant.TOKEN_PREFIX) || parameter.getParameterType().isInstance(User.class)) {
            return null;
        }
        token = token.substring(CommonConstant.TOKEN_PREFIX.length());
        try {
            jwtUtil.isTokenExpired(token);
        } catch (Exception e) {
            log.error(e.toString(), e);
            return null;
        }
        Claims claims = jwtUtil.getClaimsFromToken(token);
        User user = new User();
        Integer userId = claims.get("userId", Integer.class);
        String username = claims.get("username", String.class);
        List<String> roleNameList = claims.get("roleNameList", List.class);
        user.setId(userId);
        user.setUsername(username);
        List<Role> roles = roleNameList.stream().map(roleName -> {
            Role role = new Role();
            role.setName(roleName);
            return role;
        }).collect(Collectors.toList());
        user.setRoles(roles);
        return user;
    }
}
