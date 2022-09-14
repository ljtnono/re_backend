package cn.ljtnono.re.entity.domain.blog;

import cn.ljtnono.re.entity.domain.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 博客文章与标签关联表
 *
 * @author Ling, Jiatong
 * Date: 2021/8/9 11:18 下午
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "blog_article_tag", excludeProperty = {"createDate", "modifyDate"})
public class ArticleTag extends BaseEntity {

    /**
     * 博客id
     */
    private Integer articleId;

    /**
     * 博客标签id
     */
    private Integer tagId;
}
