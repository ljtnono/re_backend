package cn.lingjiatong.re.job.enumeration;

/**
 * 头条热榜state枚举
 *
 * @author Ling, Jiatong
 * Date: 2022/10/12 21:42
 */
public enum ToutiaoRbEnum {
    NORMAL((byte) 0),
    HOT((byte) 1),
    NEW((byte) 2),

    ;
    private Byte code;

    ToutiaoRbEnum(Byte code) {
        this.code = code;
    }

    public Byte getCode() {
        return code;
    }
}
