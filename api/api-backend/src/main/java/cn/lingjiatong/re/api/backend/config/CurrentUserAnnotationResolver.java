package cn.lingjiatong.re.api.backend.config;

import cn.lingjiatong.re.api.backend.security.JwtUtil;
import cn.lingjiatong.re.common.annotation.CurrentUser;
import cn.lingjiatong.re.common.constant.CommonConstant;
import cn.lingjiatong.re.common.entity.User;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * CurrentUser注解处理类
 *
 * @author Ling, Jiatong
 * Date: 2020/7/10 9:22 上午
 */
@Slf4j
@Component
public class CurrentUserAnnotationResolver implements HandlerMethodArgumentResolver {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        // 是UserDetails 类型，并且打上了注解，那么就注入
        return parameter.getParameterType().isAssignableFrom(User.class) && parameter.hasParameterAnnotation(CurrentUser.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        String token = webRequest.getHeader(CommonConstant.TOKE_HTTP_HEADER);
        // token格式错误或者过期，参数类型不正确都会返回null
        if (!StringUtils.hasLength(token) || !token.startsWith(CommonConstant.TOKEN_PREFIX) || parameter.getParameterType().isInstance(User.class)) {
            return null;
        }
        token = token.substring(CommonConstant.TOKEN_PREFIX.length());
        Claims claims;
        try {
            claims = jwtUtil.getClaimsFromToken(token);
        } catch (Exception e) {
            log.error(e.toString(), e);
            return null;
        }
        User user = new User();
        Long userId = claims.get("userId", Long.class);
        String username = claims.get("username", String.class);
        String email = claims.get("email", String.class);
        String phone = claims.get("phone", String.class);
        user.setId(userId);
        user.setUsername(username);
        user.setEmail(email);
        user.setPhone(phone);
        return user;
    }
}
