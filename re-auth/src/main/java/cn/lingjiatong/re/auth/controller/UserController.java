package cn.lingjiatong.re.auth.controller;

import cn.lingjiatong.re.auth.service.UserService;
import cn.lingjiatong.re.auth.vo.UserLoginVO;
import cn.lingjiatong.re.common.ResultVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.SchemaProperty;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.*;

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
@RequestMapping("/user")
@Tag(name = "用户模块接口")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private TokenEndpoint tokenEndpoint;

    // ********************************新增类接口********************************
    // ********************************删除类接口********************************
    // ********************************修改类接口********************************
    // ********************************查询类接口********************************

    /**
     * 用户登录
     *
     * @param principal  principal
     * @param parameters 参数列表
     * @return 通用消息返回对象
     */
    @Operation(
            summary = "用户登录", method = "POST", requestBody = @RequestBody(content = {@Content(mediaType = MediaType.APPLICATION_FORM_URLENCODED_VALUE, schema = @Schema(type = "object"), schemaProperties = {
            @SchemaProperty(name = "grant_type", schema = @Schema(type = "string", description = "oauth2定义的验证类型", example = "verify_code")),
            @SchemaProperty(name = "client_id", schema = @Schema(type = "string", description = "oauth2定义的客户端id", example = "re_admin")),
            @SchemaProperty(name = "client_secret", schema = @Schema(type = "string", description = "oauth2定义的客户端密钥", example = "re_admin")),
            @SchemaProperty(name = "scope", schema = @Schema(type = "string", description = "oauth2定义的访问范围", example = "all")),
            @SchemaProperty(name = "verifyCodeKey", schema = @Schema(type = "string", description = "验证码key", example = "DEV-TEST")),
            @SchemaProperty(name = "verifyCode", schema = @Schema(type = "string", description = "验证码值", example = "Tonb")),
            @SchemaProperty(name = "username", schema = @Schema(type = "string", description = "用户名", example = "lingjiatong")),
            @SchemaProperty(name = "password", schema = @Schema(type = "string", description = "密码", example = "ljtLJT715336"))}
    )}))
    @PostMapping(value = "/login", consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public ResultVO<UserLoginVO> login(@Parameter(hidden = true) Principal principal, @Parameter(hidden = true) @RequestParam Map<String, String> parameters) throws HttpRequestMethodNotSupportedException {
        log.info("==========用户登录，参数：{}，{}", principal, parameters);
        return ResultVO.success(userService.login(principal, parameters, tokenEndpoint));
    }


    /**
     * 用户注销
     *
     * @return 通用消息返回对象
     */
    @PostMapping("/logout")
    @Operation(summary = "用户注销", method = "POST")
    public ResultVO<?> logout() {
        log.info("==========用户注销");
        userService.logout();
        return ResultVO.success();
    }

    /**
     * 刷新登录验证码
     *
     * @param verifyCodeKey       前端传递过来的验证码随机值
     * @return 验证码图片base64字符串
     */
    @GetMapping("/refreshVerifyCode")
    @Operation(summary = "刷新登录验证码", method = "POST")
    public ResultVO<String> refreshVerifyCode(@Parameter(name = "verifyCodeKey", description = "验证码key") String verifyCodeKey) throws IOException {
        log.info("==========刷新登录验证码，参数：{}", verifyCodeKey);
        return ResultVO.success(userService.refreshVerifyCode(verifyCodeKey));
    }


    // ********************************复写oauth2异常处理器********************************

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<OAuth2Exception> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) throws Exception {
        return tokenEndpoint.handleHttpRequestMethodNotSupportedException(e);
    }

    @ExceptionHandler(ClientRegistrationException.class)
    public ResponseEntity<OAuth2Exception> handleClientRegistrationException(Exception e) throws Exception {
        return tokenEndpoint.handleClientRegistrationException(e);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<OAuth2Exception> handleException(Exception e) throws Exception {
        return tokenEndpoint.handleException(e);
    }

    @ExceptionHandler(OAuth2Exception.class)
    public ResponseEntity<OAuth2Exception> handleException(OAuth2Exception e) throws Exception {
        return tokenEndpoint.handleException(e);
    }

}
