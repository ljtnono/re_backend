package cn.lingjiatong.re.service.article.constant;

import java.util.List;
import java.util.regex.Pattern;

/**
 * 后台文章模块常量池
 *
 * @author Ling, Jiatong
 * Date: 2022/10/18 21:44
 */
public interface BackendArticleConstant {

    // 默认封面图片
    String DEFAULT_COVER_URL = "http://f.lingjiatong.cn:30090/rootelement/sys/default_article_cover.gif";
    // 校验标题的正则表达式
    Pattern TITLE_REGEX = Pattern.compile("^[\\u4e00-\\u9fa5\\S\\s,.，。‘’“”'()（）]{4,100}$");
    // 校验简介的正则表达式
    Pattern SUMMARY_REGEX = Pattern.compile("^[\\u4e00-\\u9fa5\\S\\s,.，。‘’“”'()（）]{0,200}$");
    // 校验标签名的正则表达式
    Pattern TAG_NAME_REGEX = Pattern.compile("^[\\u4e00-\\u9fa5\\S]{2,20}$");

    // 推荐
    Byte ARTICLE_RECOMMEND = 1;
    // 不推荐
    Byte ARTICLE_NOT_RECOMMEND = 0;
    // 置顶
    Byte ARTICLE_TOP = 1;
    // 不置顶
    Byte ARTICLE_NOT_TOP = 0;
    // 原创
    Byte ARTICLE_CREATION_YC = 1;
    // 转载
    Byte ARTICLE_CREATION_ZZ = 2;

    /**
     * 获取推荐所有值集合
     *
     * @return 推荐所有值集合
     */
    static List<Byte> recommendValues() {
        return List.of(ARTICLE_RECOMMEND, ARTICLE_NOT_RECOMMEND);
    }

    /**
     * 获取置顶所有值集合
     *
     * @return 置顶所有值集合
     */
    static List<Byte> topValues() {
        return List.of(ARTICLE_TOP, ARTICLE_NOT_TOP);
    }

    /**
     * 获取创作类型所有值集合
     *
     * @return 创作类型所有值集合
     */
    static List<Byte> creationTypeValues() {
        return List.of(ARTICLE_CREATION_YC, ARTICLE_CREATION_ZZ);
    }


}
