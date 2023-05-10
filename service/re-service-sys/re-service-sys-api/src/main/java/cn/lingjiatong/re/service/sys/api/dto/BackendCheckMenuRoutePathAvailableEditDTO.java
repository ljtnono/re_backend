package cn.lingjiatong.re.service.sys.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 后台校验菜单路由路径在编辑时是否可用DTO对象
 *
 * @author Ling, Jiatong
 * Date: 2023/5/10 14:02
 */
@Data
@Schema(name = "BackendCheckMenuRoutePathAvailableEditDTO", description = "后台校验菜单路由路径在编辑时是否可用DTO对象")
public class BackendCheckMenuRoutePathAvailableEditDTO {

    /**
     * 菜单id
     */
    @Schema(description = "菜单id")
    private String menuId;

    /**
     * 菜单路由路径
     */
    @Schema(description = "菜单路由路径")
    private String routePath;

}
