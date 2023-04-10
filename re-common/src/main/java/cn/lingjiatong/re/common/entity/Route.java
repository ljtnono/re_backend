package cn.lingjiatong.re.common.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 页面路由实体
 *
 * @author Ling, Jiatong
 * Date: 2022/12/29 22:15
 */
@Data
@TableName(value = "route", schema = "re_sys")
public class Route {

    /**
     * 主键id
     */
    @TableId
    private Long id;

    /**
     * 路由所属项目名称
     */
    private String projectName;

    /**
     * 路由名称
     */
    private String name;

    /**
     * 命名视图组件
     */
    private String component;

    /**
     * 嵌套路由
     */
    private String children;

    /**
     * 进入路由之前钩子函数
     */
    private String beforeEnter;

    /**
     * 匹配规则是否大小写敏感
     */
    private Byte caseSensitive;

    /**
     * 编译正则的选项
     */
    private String pathToRegexOptions;

    /**
     * 路由路径
     */
    private String path;

    /**
     * 路由元信息
     */
    private String meta;

    /**
     * 重定向信息
     */
    private String redirect;

    /**
     * 别名
     */
    private String alias;

    /**
     * 路由参数
     */
    private String props;

}
