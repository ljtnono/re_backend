package cn.lingjiatong.re.service.sys.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Set;

/**
 * 后台更新角色DTO对象
 *
 * @author Ling, Jiatong
 * Date: 2023/3/29 15:57
 */
@Data
@Schema(name = "BackendRoleUpdateDTO", description = "后台更新角色DTO对象")
public class BackendRoleUpdateDTO {


    /**
     * 角色id
     */
    @Schema(description = "角色id")
    private Long id;

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
