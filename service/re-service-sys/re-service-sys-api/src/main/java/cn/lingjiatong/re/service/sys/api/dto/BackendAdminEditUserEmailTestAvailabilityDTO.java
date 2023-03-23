package cn.lingjiatong.re.service.sys.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 后台管理员用户编辑用户信息表单测试邮箱是否可用DTO对象
 *
 * @author Ling, Jiatong
 * Date: 3/23/23 11:26 PM
 */
@Data
@Schema(name = "BackendAdminEditUserEmailTestAvailabilityDTO", description = "后台管理员用户编辑用户信息表单测试邮箱是否可用DTO对象")
public class BackendAdminEditUserEmailTestAvailabilityDTO {

    /**
     * 邮箱
     */
    @Schema(description = "邮箱")
    private String email;

    /**
     * 用户id
     */
    @Schema(description = "用户id")
    private Long userId;

}
