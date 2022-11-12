package cn.lingjiatong.re.service.article.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 博客标签实体
 *
 * @author Ling, Jiatong
 * Date: 2022/10/16 11:32
 */
@Data
@TableName("tag")
public class Tag {

    /**
     * 主键id
     */
    @TableId
    private Long id;

    /**
     * 标签名
     */
    private String name;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 最后修改时间
     */
    private LocalDateTime modifyTime;

    /**
     * 操作用户
     */
    private String optUser;

    /**
     * 是否删除 0 正常 1 删除
     */
    @TableField("is_deleted")
    private Byte delete;

}
