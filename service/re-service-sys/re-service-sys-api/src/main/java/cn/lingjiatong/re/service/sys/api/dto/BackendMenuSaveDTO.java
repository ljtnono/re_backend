package cn.lingjiatong.re.service.sys.api.dto;

import cn.lingjiatong.re.service.sys.api.common.BackendMenuPermission;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 后台新增菜单DTO对象
 *
 * @author Ling, Jiatong
 * Date: 2023/4/14 09:59
 */
@Data
@Schema(name = "BackendMenuSaveDTO", description = "后台新增菜单DTO对象")
public class BackendMenuSaveDTO {

    /**
     * 菜单标题
     */
    @Schema(description = "菜单标题")
    private String title;

    /**
     * 菜单icon
     */
    @Schema(description = "菜单icon")
    private String icon;

    /**
     * 菜单路由路径
     */
    @Schema(description = "菜单路由路径")
    private String routePath;

    /**
     * 父级菜单id
     */
    @Schema(description = "父级菜单id")
    private Long parentId;

    /**
     * 菜单路由名称
     */
    @Schema(description = "菜单路由名称")
    private String routeName;

    /**
     * 对应的组件路径
     */
    @Schema(description = "组件路径")
    private String componentPath;

    /**
     * 菜单权限列表
     */
    @Schema(description = "菜单权限列表")
    private List<BackendMenuPermission> permissionList;
}
