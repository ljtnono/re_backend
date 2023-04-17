package cn.lingjiatong.re.service.sys.api.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 后台获取路由列表VO对象
 *
 * @author Ling, Jiatong
 * Date: 4/10/23 9:52 PM
 */
@Data
@Schema(name = "BackendRouteListVO", description = "后台获取路由列表VO对象")
public class BackendRouteListVO {

    /**
     * 主键id
     */
    @Schema(description = "主键id")
    private Long id;

    /**
     * 路由所属项目名称
     */
    @Schema(description = "路由所属项目名称")
    private String projectName;

    /**
     * 路由名称
     */
    @Schema(description = "路由名称")
    private String name;

    /**
     * 命名视图组件
     */
    @Schema(description = "命名视图组件")
    private String component;


    /**
     * 进入路由之前钩子函数
     */
    @Schema(description = "进入路由之前钩子函数")
    private String beforeEnter;

    /**
     * 匹配规则是否大小写敏感
     */
    @Schema(description = "匹配规则是否大小写敏感")
    private Byte caseSensitive;

    /**
     * 编译正则的选项
     */
    @Schema(description = "编译正则的选项")
    private String pathToRegexOptions;

    /**
     * 路由路径
     */
    @Schema(description = "路由路径")
    private String path;

    /**
     * 路由元信息
     */
    @Schema(description = "路由元信息")
    private String meta;

    /**
     * 重定向信息
     */
    @Schema(description = "重定向信息")
    private String redirect;

    /**
     * 别名
     */
    @Schema(description = "别名")
    private String alias;

    /**
     * 路由参数
     */
    @Schema(description = "路由参数")
    private String props;

    /**
     * 嵌套路由
     */
    @Schema(description = "嵌套路由")
    private List<BackendRouteListVO> children;
}
