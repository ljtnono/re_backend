package cn.lingjiatong.re.service.article.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 文章标签关联表
 *
 * @author Ling, Jiatong
 * Date: 2022/10/16 12:47
 */
@Data
@TableName(value = "tr_article_tag", schema = "re_article")
public class ArticleTag {

    /**
     * 主键id
     */
    @TableId
    private Long id;

    /**
     * 标签id
     */
    private Long tagId;

    /**
     * 文章id
     */
    private Long articleId;
}
