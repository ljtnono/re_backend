package cn.lingjiatong.re.service.sys.api.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 后台获取角色列表VO对象
 *
 * @author Ling, Jiatong
 * Date: 2023/3/3 14:36
 */
@Data
@Schema(name = "BackendRoleListVO", description = "后台获取角色列表VO对象")
public class BackendRoleListVO {

    /**
     * id
     */
    @Schema(description = "角色id")
    private Long id;

    /**
     * 角色名
     */
    @Schema(description = "角色名")
    private String name;
}
