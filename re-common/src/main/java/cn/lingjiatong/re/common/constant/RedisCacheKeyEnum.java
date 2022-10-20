package cn.lingjiatong.re.common.constant;

/**
 * redis缓存健枚举
 *
 * @author Ling, Jiatong
 * Date: 2022/10/17 22:21
 */
public enum RedisCacheKeyEnum {

    // 文章草稿redis缓存key，这里timestap为时间戳
    ARTICLE_DRAFT("draft:draftTitle:timestamp:uuid"),

    ;
    // ********************************文章相关********************************


    private final String value;

    public String getValue() {
        return value;
    }

    RedisCacheKeyEnum(String value) {
        this.value = value;
    }
}