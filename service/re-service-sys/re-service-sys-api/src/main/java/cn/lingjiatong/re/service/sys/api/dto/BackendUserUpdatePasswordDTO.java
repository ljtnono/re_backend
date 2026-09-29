package cn.lingjiatong.re.service.sys.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 后台个人修改密码DTO对象
 *
 * @author Ling, Jiatong
 */
@Data
@Schema(name = "BackendUserUpdatePasswordDTO", description = "后台个人修改密码DTO对象")
public class BackendUserUpdatePasswordDTO {

    /**
     * 当前密码
     */
    @Schema(description = "当前密码")
    private String oldPassword;

    /**
     * 新密码
     */
    @Schema(description = "新密码")
    private String newPassword;

    /**
     * 邮箱验证码
     */
    @Schema(description = "邮箱验证码")
    private String emailCode;
}
