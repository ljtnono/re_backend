package cn.lingjiatong.re.service.sys.api.dto.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 后台菜单权限对象
 *
 * @author Ling, Jiatong
 * Date: 2023/5/9 11:41
 */
@Data
@Schema(name = "BackendMenuPermission", description = "后台菜单权限对象")
public class BackendMenuPermission {

    /**
     * 菜单权限名称
     */
    @Schema(description = "菜单权限名称")
    private String name;

    /**
     * 菜单权限表达式
     */
    @Schema(description = "菜单权限表达式")
    private String expression;

}
