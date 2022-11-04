package cn.lingjiatong.re.auth.controller;

import cn.lingjiatong.re.auth.component.UserService;
import cn.lingjiatong.re.common.ResultVO;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;
import java.util.Map;

/**
 * 用户模块controller层
 *
 * @author Ling, Jiatong
 * Date: 2022/10/23 01:02
 */
@Slf4j
@RestController
@RequestMapping
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private TokenEndpoint tokenEndpoint;

    /**
     * 刷新登录验证码
     *
     * @param verifyCodeKey 前端传递过来的验证码随机值
     */
    @GetMapping("/refreshVerifyCode")
    @ApiOperation(value = "刷新登录验证码", httpMethod = "POST")
    public void refreshVerifyCode(@RequestParam("verifyCodeKey") String verifyCodeKey, HttpServletResponse httpServletResponse) throws IOException {
        log.info("==========刷新登录验证码，参数：{}", verifyCodeKey);
        userService.refreshVerifyCode(verifyCodeKey, httpServletResponse);
    }

    /**
     * 获取token
     */
    @PostMapping("/oauth/token")
    @ApiOperation(value = "获取token", httpMethod = "POST")
    public ResultVO<?> getAccessToken(Principal principal, @RequestParam Map<String, String> parameters) throws HttpRequestMethodNotSupportedException {
        log.info("==========获取token，参数：{}, {}", principal, parameters);
        ResponseEntity<OAuth2AccessToken> accessToken = tokenEndpoint.postAccessToken(principal, parameters);
        // TODO 获取用户信息、菜单列表、tokenInfo
        return ResultVO.success(accessToken.getBody());
    }

    /**
     * 获取token
     */
    @GetMapping("/oauth/token")
    @ApiOperation(value = "获取token", httpMethod = "GET")
    public ResultVO<?> postAccessToken(Principal principal, @RequestParam Map<String, String> parameters) throws HttpRequestMethodNotSupportedException {
        log.info("==========获取token，参数：{}, {}", principal, parameters);
        ResponseEntity<OAuth2AccessToken> accessToken = tokenEndpoint.getAccessToken(principal, parameters);
        return ResultVO.success(accessToken.getBody());
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<OAuth2Exception> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) throws Exception {
        return tokenEndpoint.handleHttpRequestMethodNotSupportedException(e);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<OAuth2Exception> handleException(Exception e) throws Exception {
        return tokenEndpoint.handleException(e);
    }

    @ExceptionHandler(ClientRegistrationException.class)
    public ResponseEntity<OAuth2Exception> handleClientRegistrationException(Exception e) throws Exception {
        return tokenEndpoint.handleClientRegistrationException(e);
    }

    @ExceptionHandler(OAuth2Exception.class)
    public ResponseEntity<OAuth2Exception> handleException(OAuth2Exception e) throws Exception {
        return tokenEndpoint.handleException(e);
    }

}
