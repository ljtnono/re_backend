package cn.lingjiatong.re.service.sys.constant;

/**
 * 系统通知类型枚举
 *
 * @author Ling, Jiatong
 * Date: 2022/10/15 14:51
 */
public enum SysNoticeTypeEnum {

    // 系统通知重要性最大
    SYS_NOTICE((byte) 1, "系统通知"),
    // 常规通知
    NORMAL_NOTICE((byte) 2, "常规通知"),
    // 日常新闻通知
    NEWS_NOTICE((byte) 3, "新闻通知")
    ;

    /**
     * 通知类型值
     */
    private Byte code;

    /**
     * 通知类型名
     */
    private String name;

    SysNoticeTypeEnum(Byte code, String name) {
        this.code = code;
        this.name = name;
    }

    public Byte getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
