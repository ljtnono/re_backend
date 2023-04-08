package cn.lingjiatong.re.service.sys.api.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 后台获取菜单列表VO对象
 *
 * @author Ling, Jiatong
 * Date: 4/8/23 9:51 PM
 */
@Data
@Schema(name = "BackendMenuListVO", description = "后台获取菜单列表VO对象")
public class BackendMenuListVO {

    /**
     * 菜单id
     */
    @Schema(description = "菜单id")
    private String id;

    /**
     * 父级菜单id
     */
    @Schema(description = "父级菜单id")
    private String parentId;

    /**
     * 菜单名称
     */
    @Schema(description = "菜单名称")
    private String name;

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
     * 菜单路径
     */
    @Schema(description = "菜单路径")
    private String path;

    /**
     * 子菜单列表
     */
    @Schema(description = "子菜单列表")
    private List<BackendMenuListVO> children;
}
