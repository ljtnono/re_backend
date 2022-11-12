package cn.lingjiatong.re.common.entity;

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
     * 主键id，自增
     */
    @TableId
    private Long id;

    /**
     * 权限名
     */
    private String name;

    /**
     * 权限类型
     */
    private Byte type;

    /**
     * 父权限id
     */
    private Long parentId;

    /**
     * 权限表达式
     * 格式: xxx父权限名:xxx子权限:查看(view)|新增(add)|修改(update)|删除(delete)
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
    private Byte delete;


}
