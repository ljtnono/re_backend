package cn.lingjiatong.re.service.sys.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 后台绑定邮箱发送验证码DTO对象
 *
 * @author Ling, Jiatong
 */
@Data
@Schema(name = "BackendUserBindEmailSendCodeDTO", description = "后台绑定邮箱发送验证码DTO对象")
public class BackendUserBindEmailSendCodeDTO {

    /**
     * 邮箱
     */
    @Schema(description = "邮箱")
    private String email;
}
