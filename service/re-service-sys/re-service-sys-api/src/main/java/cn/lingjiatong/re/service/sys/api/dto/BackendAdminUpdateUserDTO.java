package cn.lingjiatong.re.service.sys.api.dto;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 后台管理员编辑用户信息DTO对象
 *
 * @author Ling, Jiatong
 * Date: 2022/10/31 21:14
 */
@Data
@Schema(name = "BackendAdminUpdateUserDTO", description = "后台管理员编辑用户信息DTO对象")
public class BackendAdminUpdateUserDTO {

    /**
     * 主键id
     */
    @Schema(description = "用户id", required = true)
    private Long userId;

    /**
     * 密码
     */
    @Schema(description = "密码 只在管理员修改时传此参数")
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String password;

    /**
     * 邮箱
     */
    @Schema(description = "用户邮箱")
    private String email;

    /**
     * 用户角色id
     */
    @Schema(description = "用户角色id")
    private Long roleId;

}
