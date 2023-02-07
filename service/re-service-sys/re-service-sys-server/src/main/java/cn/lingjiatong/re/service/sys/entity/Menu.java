package cn.lingjiatong.re.service.sys.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 菜单实体
 *
 * @author Ling, Jiatong
 * Date: 2023/2/7 09:49
 */
@Data
@Schema(name = "Menu", description = "菜单实体")
@TableName(value = "menu", schema = "re_sys")
public class Menu {

    /**
     * 菜单id
     */
    private Long id;

    /**
     * 菜单所属项目名称
     */
    private String projectName;

    /**
     * 父菜单id
     */
    private Long parentId;

    /**
     * 菜单标题
     */
    private String title;

    /**
     * 菜单icon
     */
    private String icon;

    /**
     * 菜单名称
     */
    private String name;

    /**
     * 菜单路径
     */
    private String path;

}
