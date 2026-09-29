package cn.lingjiatong.re.service.sys.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 后台忘记密码发送验证码DTO对象
 *
 * @author Ling, Jiatong
 */
@Data
@Schema(name = "BackendUserForgetPasswordSendCodeDTO", description = "后台忘记密码发送验证码DTO对象")
public class BackendUserForgetPasswordSendCodeDTO {

    /**
     * 用户名
     */
    @Schema(description = "用户名")
    private String username;

    /**
     * 邮箱（须与账号绑定的邮箱一致）
     */
    @Schema(description = "邮箱")
    private String email;
}
