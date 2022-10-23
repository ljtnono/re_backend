package cn.lingjiatong.re.auth.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * 用户登录DTO对象
 *
 * @author Ling, Jiatong
 * Date: 2022/10/23 01:15
 */
@Data
@ApiModel(description = "用户登录DTO对象")
public class LoginDTO {

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 验证码key
     */
    private String verifyCodeKey;

    /**
     * 验证码值
     */
    private String verifyCodeValue;
}
