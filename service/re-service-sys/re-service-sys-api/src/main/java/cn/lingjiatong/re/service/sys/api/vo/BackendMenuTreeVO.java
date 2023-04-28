package cn.lingjiatong.re.service.sys.api.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 后台获取菜单树VO对象
 *
 * @author Ling, Jiatong
 * Date: 3/26/23 10:17 PM
 */
@Data
@Schema(name = "BackendMenuTreeVO", description = "后台获取菜单树VO对象")
public class BackendMenuTreeVO {

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
    private List<BackendMenuTreeVO> children;

}
