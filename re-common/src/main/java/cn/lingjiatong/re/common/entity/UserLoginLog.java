package cn.lingjiatong.re.common.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户登录日志实体
 *
 * @author Ling, Jiatong
 * Date: 2023/3/15 14:50
 */
@Data
@TableName(value = "user_login_log", schema = "re_sys")
public class UserLoginLog {

    /**
     * 主键id
     */
    @TableId
    private Long id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 登录时间
     */
    private LocalDateTime loginTime;

    /**
     * 登录时的ip地址
     */
    private String ip;

    /**
     * 登录时的User-Agent
     */
    private String ua;

    /**
     * 记录创建时间
     */
    private LocalDateTime createTime;

    /**
     * 记录最后修改时间
     */
    private LocalDateTime modifyTime;

}
