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
     * 角色对应的菜单树
     */
    @Schema(description = "角色对应的菜单树")
    private List<MenuTree> menuTree;

    @Data
    public final static class MenuTree {


        /**
         * 菜单标题
         */
        @Schema(description = "菜单标题")
        private String menuTitle;

        /**
         * 菜单id
         */
        @Schema(description = "菜单id")
        private String menuId;

        /**
         * 父菜单id
         */
        @Schema(description = "父菜单id")
        private String parentMenuId;

        /**
         * 子菜单列表
         */
        @Schema(description = "子菜单列表")
        private List<MenuTree> children;
    }
}
