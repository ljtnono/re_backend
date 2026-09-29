package cn.lingjiatong.re.service.sys.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 后台绑定邮箱确认DTO对象
 *
 * @author Ling, Jiatong
 */
@Data
@Schema(name = "BackendUserBindEmailConfirmDTO", description = "后台绑定邮箱确认DTO对象")
public class BackendUserBindEmailConfirmDTO {

    /**
     * 邮箱
     */
    @Schema(description = "邮箱")
    private String email;

    /**
     * 验证码
     */
    @Schema(description = "验证码")
    private String code;
}
