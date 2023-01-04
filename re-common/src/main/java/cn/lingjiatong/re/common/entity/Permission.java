package cn.lingjiatong.re.common.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 系统权限实体
 *
 * @author Ling, Jiatong
 * Date: 2022/10/22 18:51
 */
@Data
@TableName("permission")
public class Permission {

    /**
     * 主键id
     */
    @TableId
    private Long id;

    /**
     * 父权限id
     */
    private Long parentId;

    /**
     * 权限所属项目名称
     */
    private String projectName;

    /**
     * 权限所属菜单id
     */
    private Long menuId;

    /**
     * 权限名
     */
    private String name;

    /**
     * 权限类型 0 菜单项 1 具体某个权限
     */
    private Byte type;

    /**
     * 权限表达式
     * 格式: xxx父权限名:xxx子权限
     */
    private String expression;

    /**
     * 权限创建时间
     */
    private LocalDateTime createTime;

    /**
     * 最后修改时间
     */
    private LocalDateTime modifyTime;

    /**
     * 是否删除
     * 0 正常 1 已删除
     */
    @TableField("is_deleted")
    private Byte deleted;

}
