package cn.lingjiatong.re.service.article.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 文章、标签关联表
 *
 * @author Ling, Jiatong
 * Date: 2022/10/20 21:12
 */
@Data
@TableName("tr_article_tag")
public class TrArticleTag {

    /**
     * 主键
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
