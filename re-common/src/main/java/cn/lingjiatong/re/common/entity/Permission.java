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
@TableName(value = "permission", schema = "re_sys")
public class Permission {

    /**
     * 主键id
     */
    @TableId
    private Long id;

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
     * 权限表达式
     * 格式: xxx父权限名:xxx子权限
     */
    private String expression;
}
