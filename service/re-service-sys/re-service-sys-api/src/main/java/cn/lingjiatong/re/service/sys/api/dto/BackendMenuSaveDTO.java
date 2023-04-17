package cn.lingjiatong.re.service.sys.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

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
     * 菜单名称
     */
    @Schema(description = "菜单名称")
    private String name;

    /**
     * 菜单icon
     */
    @Schema(description = "菜单icon")
    private String icon;

    /**
     * 菜单路径
     */
    @Schema(description = "菜单路径")
    private String path;

    /**
     * 父级菜单id
     */
    @Schema(description = "父级菜单id")
    private Long parentId;

    /**
     * 对应组件名称
     */
    @Schema(description = "对应组件名称")
    private String componentName;

    /**
     * 对应的组件路径
     */
    @Schema(description = "组件路径")
    private String componentPath;


}
