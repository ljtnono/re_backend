package cn.lingjiatong.re.service.sys.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Set;

/**
 * 后台角色批量删除DTO对象
 *
 * @author Ling, Jiatong
 * Date: 3/29/23 12:04 AM
 */
@Data
@Schema(name = "BackendRoleDeleteBatchDTO", description = "后台角色批量删除DTO对象")
public class BackendRoleDeleteBatchDTO {

    /**
     * 角色id列表
     */
    private Set<Long> roleIdSet;
}
