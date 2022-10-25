package cn.lingjiatong.re.common.constant;

/**
 * 运行环境枚举
 *
 * @author Ling, Jiatong
 * Date: 2022/10/26 00:41
 */
public enum ProfileEnum {

    PRD("prd"),
    DEV("dev")
    ;

    private String name;

    ProfileEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
