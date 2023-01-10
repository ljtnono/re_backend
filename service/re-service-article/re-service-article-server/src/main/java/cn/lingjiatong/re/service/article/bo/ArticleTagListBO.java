package cn.lingjiatong.re.service.article.bo;

import lombok.Data;

/**
 * 获取文章标签列表BO对象
 *
 * @author Ling, Jiatong
 * Date: 2023/1/10 17:09
 */
@Data
public class ArticleTagListBO {

    /**
     * 文章id
     */
    private Long articleId;

    /**
     * 标签名字符串，逗号分割
     */
    private String tagNameStr;

}
