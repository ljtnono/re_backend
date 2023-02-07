package cn.lingjiatong.re.service.sys.api.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 前端获取菜单VO对象
 *
 * @author Ling, Jiatong
 * Date: 2023/2/5 00:32
 */
@Data
@Schema(name = "FrontendMenuVO", description = "前端获取菜单VO对象")
public class FrontendMenuVO {

    /**
     * 菜单id
     */
    @JsonIgnore
    @Schema(hidden = true)
    private Long id;

    /**
     * 父菜单id
     */
    @JsonIgnore
    @Schema(hidden = true)
    private Long parentId;

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
     * 菜单名称
     */
    @Schema(description = "菜单名称")
    private String name;

    /**
     * 菜单路径
     */
    @Schema(description = "菜单路径")
    private String path;

    /**
     * 子菜单列表
     */
    @Schema(description = "子菜单列表")
    private List<FrontendMenuVO> children;
}
