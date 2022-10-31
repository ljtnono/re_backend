package cn.lingjiatong.re.service.sys.api.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 后台获取用户列表VO对象
 *
 * @author Ling, Jiatong
 * Date: 2022/10/31 20:33
 */
@Data
@ApiModel(description = "后台获取用户列表VO对象")
public class BackendUserListVO {

    /**
     * 用户id
     */
    @ApiModelProperty("用户id")
    private Integer id;

    /**
     * 用户名
     */
    @ApiModelProperty("用户名")
    private String username;

    /**
     * 用户手机号
     */
    @ApiModelProperty("用户手机号")
    private String phone;

    /**
     * 用户邮箱
     */
    @ApiModelProperty("用户邮箱")
    private String email;

    /**
     * 用户邮箱
     */
    @ApiModelProperty("用户邮箱")
    private String avatarUrl;

    /**
     * 用户创建时间
     */
    @ApiModelProperty("用户创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    /**
     * 用户最后修改时间
     */
    @ApiModelProperty("用户最后修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime modifyTime;

    /**
     * 是否删除
     * 0 正常
     * 1 已删除
     */
    @ApiModelProperty("是否删除 0 正常 1 已删除")
    private Byte deleted;
}
