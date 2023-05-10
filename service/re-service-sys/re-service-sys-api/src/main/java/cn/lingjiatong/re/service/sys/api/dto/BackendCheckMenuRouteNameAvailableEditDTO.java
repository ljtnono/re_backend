package cn.lingjiatong.re.service.sys.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 后台校验菜单路由名称在编辑时是否可用DTO对象
 *
 * @author Ling, Jiatong
 * Date: 2023/5/10 13:57
 */
@Data
@Schema(name = "BackendCheckMenuRouteNameAvailableEditDTO", description = "后台校验菜单路由名称在编辑时是否可用DTO对象")
public class BackendCheckMenuRouteNameAvailableEditDTO {

    /**
     * 菜单id
     */
    @Schema(description = "菜单id")
    private String menuId;


    /**
     * 路由名称
     */
    @Schema(description = "路由名称")
    private String routeName;
}
