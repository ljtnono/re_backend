package cn.lingjiatong.re.common.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 菜单表实体
 *
 * @author Ling, Jiatong
 * Date: 2022/12/29 22:02
 */
@Data
@TableName(value = "menu", schema = "re_sys")
public class Menu {

    /**
     * 主键id
     */
    @TableId
    private Long id;

    /**
     * 菜单所属项目名称
     */
    private String projectName;

    /**
     * 父菜单id，没有则为-1
     */
    private Long parentId;

    /**
     * 菜单标题
     */
    private String title;

    /**
     * 菜单用的icon的class值
     */
    private String icon;

    /**
     * 菜单的路由路径
     */
    private String routePath;

    /**
     * 菜单路由名称
     */
    private String routeName;

    /**
     * 菜单组件路径
     */
    private String componentPath;
}
