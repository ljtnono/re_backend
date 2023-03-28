package cn.lingjiatong.re.service.sys.api.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 角色菜单树VO对象
 *
 * @author Ling, Jiatong
 * Date: 3/25/23 9:42 PM
 */
@Data
@Schema(name = "BackendRoleMenuTreeVO", description = "角色菜单树VO对象")
public class BackendRoleMenuTreeVO {

    /**
     * 角色id
     */
    @Schema(description = "角色id")
    private Long roleId;

    /**
     * 角色对应的权限树
     */
    @Schema(description = "角色对应的权限树")
    private List<MenuTree> menuTree;

    @Data
    public final static class MenuTree {

        /**
         * 菜单名称
         */
        @Schema(description = "菜单名称")
        private String menuName;

        /**
         * 菜单标题
         */
        @Schema(description = "菜单标题")
        private String menuTitle;

        /**
         * 菜单id
         */
        @Schema(description = "菜单id")
        private Long menuId;

        /**
         * 父菜单id
         */
        @Schema(description = "父菜单id")
        private Long parentMenuId;

        /**
         * 菜单的权限列表
         */
        @Schema(description = "菜单的权限列表")
        private List<MenuPermission> permissionList;

        /**
         * 子菜单列表
         */
        @Schema(description = "子菜单列表")
        private List<MenuTree> children;
    }

    @Data
    public final static class MenuPermission {

        /**
         * 权限id
         */
        @Schema(description = "权限id")
        private Long permissionId;

        /**
         * 权限名称
         */
        @Schema(description = "权限名称")
        private String permissionName;

        /**
         * 权限表达式
         */
        @Schema(description = "权限表达式")
        private String expression;
    }
}