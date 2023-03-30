package cn.lingjiatong.re.service.sys.api.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

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
    private String id;

    /**
     * 角色名
     */
    @Schema(description = "角色名")
    private String name;

    /**
     * 角色描述
     */
    @Schema(description = "角色描述")
    private String description;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /**
     * 最后修改时间
     */
    @Schema(description = "最后修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime modifyTime;

    /**
     * 角色的菜单树
     */
    @Schema(description = "角色的菜单树")
    private BackendRoleMenuTreeVO roleMenuTree;
}
