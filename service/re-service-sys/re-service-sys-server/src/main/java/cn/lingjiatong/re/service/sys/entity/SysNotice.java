package cn.lingjiatong.re.service.sys.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 系统通知实体类
 *
 * @author Ling, Jiatong
 * Date: 2022/10/15 14:06
 */
@Data
@TableName("sys_notice")
public class SysNotice {

    /**
     * 主键id
     */
    @TableId
    private Long id;

    /**
     * 通知标题
     */
    private String title;

    /**
     * 通知链接
     * 如果不存在则为空
     */
    private String link;

    /**
     * 通知类型
     *
     * 1 系统通知（重要） 2 常规通知 3 日常新闻
     */
    private Byte type;

    /**
     * 新闻类通知状态
     * 0 普通新闻
     * 1 热榜
     * 2 新闻
     */
    private Byte newsState;

    /**
     * 新闻日期
     */
    private LocalDate newsDate;

    /**
     * 消息开始时间
     */
    private LocalDateTime startTime;

    /**
     * 消息结束时间
     */
    private LocalDateTime endTime;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 最后修改时间
     */
    private LocalDateTime modifyTime;

    /**
     * 操作用户
     */
    private String optUser;

}
