package cn.lingjiatong.re.service.sys.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 后台角色名称校验DTO对象
 *
 * @author Ling, Jiatong
 * Date: 2023/3/30 09:46
 */
@Data
@Schema(name = "BackendRoleNameCheckDTO", description = "后台角色名称校验DTO对象")
public class BackendRoleNameCheckDTO {

    /**
     * 角色id
     */
    @Schema(description = "角色id")
    private String roleId;

    /**
     * 角色名称
     */
    @Schema(description = "角色名称")
    private String roleName;
}
