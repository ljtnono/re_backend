package cn.lingjiatong.re.service.sys.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 系统友情链接实体
 *
 * @author Ling, Jiatong
 * Date: 2022/10/11 00:26
 */
@Data
@TableName("sys_friend_link")
public class SysFriendLink {

    /**
     * 主键id
     */
    private Long id;

    /**
     * 友情链接名
     */
    private String name;

    /**
     * 链接地址
     */
    private String url;

    /**
     * 链接类型
     * 1 官方网站
     * 2 个人网站
     */
    private Byte type;

    /**
     * 链接主体
     */
    private String master;

    /**
     * 链接主体电子邮箱
     */
    private String masterEmail;

    /**
     * 网站图标标志地址
     */
    private String faviconUrl;

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
