package cn.lingjiatong.re.service.sys.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 后台忘记密码重置密码DTO对象
 *
 * @author Ling, Jiatong
 */
@Data
@Schema(name = "BackendUserForgetPasswordResetDTO", description = "后台忘记密码重置密码DTO对象")
public class BackendUserForgetPasswordResetDTO {

    /**
     * 用户名
     */
    @Schema(description = "用户名")
    private String username;

    /**
     * 邮箱验证码
     */
    @Schema(description = "邮箱验证码")
    private String code;

    /**
     * 新密码
     */
    @Schema(description = "新密码")
    private String newPassword;
}
