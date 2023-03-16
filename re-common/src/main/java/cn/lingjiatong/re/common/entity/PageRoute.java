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
@TableName(value = "page_route", schema = "re_sys")
public class PageRoute {

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
     * 父路由信息，不存在则为-1
     */
    private Long parentId;

    /**
     * 路由名称
     */
    private String name;

    /**
     * 路由描述
     */
    private String description;

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
