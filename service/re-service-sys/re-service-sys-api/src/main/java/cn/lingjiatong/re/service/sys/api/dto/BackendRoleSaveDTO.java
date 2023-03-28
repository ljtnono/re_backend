package cn.lingjiatong.re.service.sys.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Set;

/**
 * 后台新增角色DTO对象
 *
 * @author Ling, Jiatong
 * Date: 3/28/23 9:13 PM
 */
@Data
@Schema(name = "BackendRoleSaveDTO", description = "后台新增角色DTO对象")
public class BackendRoleSaveDTO {

    /**
     * 角色名
     */
    @Schema(description = "角色名")
    private String name;

    /**
     * 角色描述
     */
    @Schema(description = "角色描述")
    private String description;

    /**
     * 角色菜单id集合
     */
    @Schema(description = "角色菜单id集合")
    private Set<Long> menuIdSet;

}
