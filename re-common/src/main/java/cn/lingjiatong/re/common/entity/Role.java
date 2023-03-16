package cn.lingjiatong.re.common.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 系统角色实体
 *
 * @author Ling, Jiatong
 * Date: 2022/10/22 18:46
 */
@Data
@TableName(value = "role", schema = "re_sys")
public class Role {

    /**
     * 主键，自增
     */
    @TableId
    private Long id;

    /**
     * 系统角色名
     */
    private String name;

    /**
     * 角色描述
     */
    private String description;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 最后修改时间
     */
    private LocalDateTime modifyTime;

    /**
     * 是否删除
     *
     * 0 正常 1 已删除
     */
    @TableField("is_deleted")
    private Byte deleted;
}
