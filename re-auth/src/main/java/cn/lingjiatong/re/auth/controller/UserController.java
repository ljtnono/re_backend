package cn.lingjiatong.re.auth.controller;

import cn.lingjiatong.re.auth.component.UserService;
import cn.lingjiatong.re.auth.dto.LoginDTO;
import cn.lingjiatong.re.common.ResultVO;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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
@RequestMapping("/auth")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 用户登录
     *
     * @param dto 用户登录DTO对象
     * @return token
     */
    @PostMapping("/login")
    @ApiOperation(value = "用户登录", httpMethod = "POST")
    public ResultVO<?> login(@RequestBody LoginDTO dto) {
        log.info("==========用户登录，参数：{}", dto);
        return ResultVO.success(userService.login(dto));
    }


    /**
     * 刷新登录验证码
     *
     * @param verifyCodeKey 前端传递过来的验证码随机值
     */
    @GetMapping("/refreshVerifyCode")
    @ApiOperation(value = "刷新登录验证码", httpMethod = "POST")
    public void refreshVerifyCode(@RequestParam("verifyCodeKey") String verifyCodeKey, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {
        log.info("==========刷新登录验证码，参数：{}", verifyCodeKey);
        userService.refreshVerifyCode(verifyCodeKey, httpServletRequest, httpServletResponse);
    }
}
