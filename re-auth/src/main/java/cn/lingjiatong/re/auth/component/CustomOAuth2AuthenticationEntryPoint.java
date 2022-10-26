package cn.lingjiatong.re.auth.component;

import cn.lingjiatong.re.common.ResultVO;
import cn.lingjiatong.re.common.exception.ErrorEnum;
import cn.lingjiatong.re.common.util.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 自定义AuthenticationEntryPoint返回错误消息
 *
 * @author Ling, Jiatong
 * Date: 2022/10/26 00:13
 */
@Slf4j
@Component("customOAuth2AuthenticationEntryPoint")
public class CustomOAuth2AuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException {
        ResultVO<?> result;
        log.error(e.toString(), e);
        if (e instanceof BadCredentialsException) {
            result = ResultVO.error(ErrorEnum.CLIENT_ID_OR_CLIENT_SECRET_ERROR);
        } else {
            result = ResultVO.error(ErrorEnum.UNKNOWN_ERROR);
        }
        response.setStatus(HttpStatus.OK.value());
        response.setHeader("Content-Type", "application/json;charset=utf-8");
        response.getWriter().print(JSONUtil.objectToString(result));
        response.getWriter().flush();
    }
}
