package cn.ljtnono.re.entity.domain.blog;

import cn.ljtnono.re.entity.domain.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 博客标签实体
 *
 * @author Ling, Jiatong
 * Date: 2021/8/9 11:20 下午
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "blog_tag", excludeProperty = {"createDate", "modifyDate"})
public class Tag extends BaseEntity {

    /**
     * 标签名
     */
    private String name;

}
