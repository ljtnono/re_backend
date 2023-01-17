package cn.lingjiatong.re.common.constant;

/**
 * 分布式任务状态枚举
 *
 * @author Ling, Jiatong
 * Date: 2023/1/17 22:57
 */
public enum DistributedTaskStatusEnum {

    // 获取锁成功
    LOCK_SUCCESS(1, "获取锁成功"),
    // 获取锁失败
    LOCK_FAILED(2, "获取锁失败"),
    // 运行中
    RUNNING(3, "运行中"),
    // 运行结束
    FINISHED(4, "运行结束"),
    // 运行出现错误结束
    ERROR_FINISHED(5, "运行出现错误结束"),

    ;

    private final Integer code;

    private final String description;

    public Integer getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    DistributedTaskStatusEnum(Integer code, String description) {
        this.code = code;
        this.description = description;
    }
}
