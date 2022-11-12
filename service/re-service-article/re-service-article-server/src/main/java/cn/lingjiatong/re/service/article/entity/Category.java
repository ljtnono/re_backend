package cn.lingjiatong.re.service.article.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 博客类型实体
 *
 * @author Ling, Jiatong
 * Date: 2022/10/16 11:32
 */
@Data
@TableName("category")
public class Category {

    /**
     * 主键id
     */
    @TableId
    private Long id;

    /**
     * 类型名
     */
    private String name;

    /**
     * 类型总浏览量
     */
    private Long view;

    /**
     * 类型总喜欢数
     */
    private Long favorite;

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
     * 是否删除
     * 0 正常 1 已删除
     */
    @TableField("is_deleted")
    private Byte delete;

}
