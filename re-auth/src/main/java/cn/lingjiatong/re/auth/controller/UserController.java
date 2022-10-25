package cn.lingjiatong.re.auth.controller;

import cn.lingjiatong.re.auth.component.UserService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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
}
