package cn.lingjiatong.re.service.article.security;

import cn.lingjiatong.re.common.ResultVO;
import cn.lingjiatong.re.common.exception.ErrorEnum;
import cn.lingjiatong.re.common.util.JSONUtil;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 授权处理
 *
 * @author Ling, Jiatong
 * Date: 2020/7/13 19:12 下午
 */
@Component("authenticationHandler")
public class AuthenticationHandler implements AccessDeniedHandler, AuthenticationEntryPoint {

    /**
     *  AccessDeniedHandler 用来解决认证过的用户访问无权限资源时的异常
     */
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json;charset=utf-8");
        response.setStatus(HttpStatus.OK.value());
        response.getWriter().write(JSONUtil.objectToString(ResultVO.error(ErrorEnum.PERMISSION_DENIED_ERROR)));
    }

    /**
     *  AuthenticationEntryPoint 用来解决匿名用户访问无权限资源时的异常
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json;charset=utf-8");
        response.setStatus(HttpStatus.OK.value());
        response.getWriter().write(JSONUtil.objectToString(ResultVO.error(ErrorEnum.USER_NOT_AUTHTICATE_ERROR)));
    }
}
