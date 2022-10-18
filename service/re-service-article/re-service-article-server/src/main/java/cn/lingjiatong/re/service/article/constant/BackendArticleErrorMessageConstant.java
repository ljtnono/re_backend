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
    // 文章标题不能超过100个字符
    String ARTICLE_TITLE_FORMAT_ERROR_MESSAGE = "文章标题不能超过100个字符";
    // 文章简介不能超过500个字符
    String ARTICLE_SUMMARY_FORMAT_ERROR_MESSAGE = "";



}
