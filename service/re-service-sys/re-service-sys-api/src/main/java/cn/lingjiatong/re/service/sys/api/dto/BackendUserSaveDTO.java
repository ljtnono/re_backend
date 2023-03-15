package cn.lingjiatong.re.service.sys.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 后台保存用户DTO对象
 *
 * @author Ling, Jiatong
 * Date: 2023/3/3 15:39
 */
@Data
@Schema(name = "BackendUserSaveDTO", description = "后台保存用户DTO对象")
public class BackendUserSaveDTO {

    /**
     * 用户名
     */
    @Schema(description = "用户名")
    private String username;

    /**
     * 密码
     */
    @Schema(description = "密码")
    private String password;

    /**
     * 用户角色id列表
     */
    @Schema(description = "用户角色id列表")
    private Long roleId;

    /**
     * 用户邮箱
     */
    @Schema(description = "用户邮箱")
    private String email;

    /**
     * 用户手机号码
     */
    @Schema(description = "用户手机号码")
    private String phone;

    /**
     * 用户头像
     */
    @Schema(description = "用户头像")
    private String avatarUrl;
}
