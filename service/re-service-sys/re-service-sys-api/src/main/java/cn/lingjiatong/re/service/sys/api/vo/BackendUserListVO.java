package cn.lingjiatong.re.service.sys.api.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 后台获取用户列表VO对象
 *
 * @author Ling, Jiatong
 * Date: 2022/10/31 20:33
 */
@Data
@Schema(name = "BackendUserListVO", description = "后台获取用户列表VO对象")
public class BackendUserListVO {

    /**
     * 用户id
     */
    @Schema(description = "用户id")
    private String id;

    /**
     * 用户名
     */
    @Schema(description = "用户名")
    private String username;

    /**
     * 角色
     */
    @Schema(description = "角色")
    private String role;

    /**
     * 用户手机号
     */
    @Schema(description = "用户手机号")
    private String phone;

    /**
     * 用户邮箱
     */
    @Schema(description = "用户邮箱")
    private String email;

    /**
     * 用户邮箱
     */
    @Schema(description = "用户邮箱")
    private String avatarUrl;

    /**
     * 用户创建时间
     */
    @Schema(description = "用户创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    /**
     * 最近登录时间
     */
    @Schema(description = "最近登录时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime lastLoginTime;

    /**
     * ip地址
     */
    @Schema(description = "ip地址")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private String ip;

    /**
     * 是否删除
     *
     * 0 正常
     * 1 已删除
     */
    @Schema(description = "是否删除 0 正常 1 已删除")
    private Byte deleted;
}
