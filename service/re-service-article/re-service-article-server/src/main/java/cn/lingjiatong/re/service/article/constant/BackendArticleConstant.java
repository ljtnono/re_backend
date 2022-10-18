package cn.lingjiatong.re.service.article.constant;

import java.util.List;

/**
 * 后台文章模块常量池
 *
 * @author Ling, Jiatong
 * Date: 2022/10/18 21:44
 */
public interface BackendArticleConstant {

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
    Byte ARTICLE_CREATION_ZZ = 0;

    /**
     * 获取推荐所有值集合
     *
     * @return 推荐所有值集合
     */
    default List<Byte> recommendValues() {
        return List.of(ARTICLE_RECOMMEND, ARTICLE_NOT_RECOMMEND);
    }

    /**
     * 获取置顶所有值集合
     *
     * @return 置顶所有值集合
     */
    default List<Byte> topValues() {
        return List.of(ARTICLE_TOP, ARTICLE_NOT_TOP);
    }

    /**
     * 获取创作类型所有值集合
     *
     * @return 创作类型所有值集合
     */
    default List<Byte> creationTypeValues() {
        return List.of(ARTICLE_CREATION_YC, ARTICLE_CREATION_ZZ);
    }
}
