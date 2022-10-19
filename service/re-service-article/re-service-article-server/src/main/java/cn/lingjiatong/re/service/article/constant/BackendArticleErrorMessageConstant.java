package cn.lingjiatong.re.service.article.constant;

/**
 * 后台article模块错误消息常量池
 *
 * @author Ling, Jiatong
 * Date: 2022/10/17 22:20
 */
public interface BackendArticleErrorMessageConstant {

    // 草稿标题不能为空
    String DRAFT_TITLE_EMPTY_ERROR_MESSAGE = "草稿标题不能为空";
    // 文章标题不能为空
    String ARTICLE_TITLE_EMPTY_ERROR_MESSAGE = "文章标题不能为空";
    // 文章标题为4-100个字符
    String ARTICLE_TITLE_FORMAT_ERROR_MESSAGE = "文章标题为4-100个字符";
    // 文章简介不能超过200个字符
    String ARTICLE_SUMMARY_FORMAT_ERROR_MESSAGE = "文章简介不能超过200个字符";
    // 文章内容不能为空
    String ARTICLE_MARKDOWN_CONTENT_EMPTY_ERROR_MESSAGE = "文章内容不能为空";
    // 文章类型不能为空
    String ARTICLE_CATEGORY_NULL_ERROR_MESSAGE = "请选择发布文章的类型";
    // 文章分类不存在
    String ARTICLE_CATEGORY_NOT_EXIST_ERROR_MESSAGE = "文章分类不存在";
    // 当文章是转载类型时，请填写转载信息
    String NO_TRANSPORT_INFO_ERROR_MESSAGE = "转载文章需附加转载说明";


}
