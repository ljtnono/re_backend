package cn.lingjiatong.re.service.sys.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Set;

/**
 * 后台编辑用户信息DTO对象
 *
 * @author Ling, Jiatong
 * Date: 2022/10/31 21:14
 */
@Data
@ApiModel(description = "后台编辑用户信息DTO对象")
public class BackendUserUpdateDTO {

    /**
     * 主键id
     */
    @ApiModelProperty(value = "用户id", required = true)
    private Long userId;

    /**
     * 用户名
     */
    @ApiModelProperty("用户名")
    private String username;

    /**
     * 密码
     */
    @ApiModelProperty("密码 只在管理员修改时传此参数")
    private String password;

    /**
     * 手机号
     */
    @ApiModelProperty("手机号")
    private String phone;

    /**
     * 邮箱
     */
    @ApiModelProperty("用户邮箱")
    private String email;

    /**
     * 用户角色列表
     */
    @ApiModelProperty("用户角色id列表")
    private Set<Long> roleIds;

}
