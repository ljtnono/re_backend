package cn.lingjiatong.re.api.backend.config;

import cn.dev33.satoken.stp.StpUtil;
import cn.lingjiatong.re.common.ResultVO;
import cn.lingjiatong.re.common.annotation.CurrentUser;
import cn.lingjiatong.re.common.constant.CommonConstant;
import cn.lingjiatong.re.common.entity.User;
import cn.lingjiatong.re.service.sys.api.client.BackendUserFeignClient;
import jakarta.servlet.http.HttpServletRequest;
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
 * 从请求头 Authorization 中读取 JWT token，通过 Sa-Token 解析出用户名，
 * 再通过 Feign 查询数据库获取完整用户信息
 *
 * @author Ling, Jiatong
 * Date: 2020/7/10 9:22 上午
 */
@Slf4j
@Component
public class CurrentUserAnnotationResolver implements HandlerMethodArgumentResolver {

    @Autowired
    private BackendUserFeignClient backendUserFeignClient;

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
            String authHeader = request.getHeader(CommonConstant.TOKE_HTTP_HEADER);
            if (!StringUtils.hasLength(authHeader)) {
                return null;
            }
            String token = authHeader;
            if (authHeader.startsWith(CommonConstant.TOKEN_PREFIX)) {
                token = authHeader.substring(CommonConstant.TOKEN_PREFIX.length());
            }
            Object loginId = StpUtil.getLoginIdByToken(token);
            if (loginId == null) {
                return null;
            }
            String username = loginId.toString();
            return backendUserFeignClient.getCurrentUserByUsername(username);
        } catch (Exception e) {
            log.error("解析当前用户信息失败: {}", e.getMessage());
            return null;
        }
    }
}
