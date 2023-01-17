package cn.lingjiatong.re.common.constant;

/**
 * redission分布式锁健枚举
 *
 * @author Ling, Jiatong
 * Date: 2023/1/17 21:37
 */
public enum RedissionLockKeyEnum {

    // ********************************文章相关********************************

    // es文章同步分布式锁
    ES_ARTICLE_SYNC_LOCK("re:redission:lock:es_article_sync_lock")

    ;

    private final String value;

    public String getValue() {
        return value;
    }

    RedissionLockKeyEnum(String value) {
        this.value = value;
    }

}
