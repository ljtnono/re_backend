package cn.lingjiatong.re.api.backend.config;

import cn.lingjiatong.re.common.annotation.CurrentUser;
import cn.lingjiatong.re.common.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
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
 * 从 Gateway 转发的自定义请求头中读取当前登录用户信息，
 * 无需下游服务重复解析 JWT token。
 *
 * @author Ling, Jiatong
 * Date: 2020/7/10 9:22 上午
 */
@Slf4j
@Component
public class CurrentUserAnnotationResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().isAssignableFrom(User.class)
                && parameter.hasParameterAnnotation(CurrentUser.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        try {
            HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
            if (request == null) {
                return null;
            }
            // 从 Gateway 转发的自定义请求头中获取用户信息
            String username = request.getHeader("X-User-Username");
            String userIdStr = request.getHeader("X-User-Id");
            if (!StringUtils.hasLength(username)) {
                return null;
            }
            User user = new User();
            user.setUsername(username);
            if (StringUtils.hasLength(userIdStr)) {
                try {
                    user.setId(Long.valueOf(userIdStr));
                } catch (NumberFormatException e) {
                    log.debug("解析 X-User-Id 失败: {}", userIdStr);
                }
            }
            return user;
        } catch (Exception e) {
            log.error("解析当前用户信息失败: {}", e.getMessage());
            return null;
        }
    }
}
