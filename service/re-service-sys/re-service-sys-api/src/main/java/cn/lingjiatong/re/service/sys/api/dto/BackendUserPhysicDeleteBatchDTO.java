package cn.lingjiatong.re.service.sys.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 后台批量物理删除用户DTO对象
 *
 * @author Ling, Jiatong
 * Date: 2023/3/3 11:35
 */
@Data
@Schema(name = "BackendUserPhysicDeleteBatchDTO", description = "后台批量物理删除用户DTO对象")
public class BackendUserPhysicDeleteBatchDTO {

    /**
     * 用户id列表
     */
    @Schema(description = "用户id列表")
    private List<Long> userIdList;
}
