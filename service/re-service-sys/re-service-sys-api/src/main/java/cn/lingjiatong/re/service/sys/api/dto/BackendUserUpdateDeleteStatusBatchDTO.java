package cn.lingjiatong.re.service.sys.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 后台批量更改用户删除状态DTO对象
 *
 * @author Ling, Jiatong
 * Date: 2022/10/31 21:09
 */
@Data
@Schema(name = "BackendUserUpdateDeleteStatusBatchDTO", description = "后台批量更改用户删除状态DTO对象")
public class BackendUserUpdateDeleteStatusBatchDTO {

    /**
     * 用户id列表
     */
    @Schema(description = "用户id列表")
    private List<Long> userIdList;

    /**
     * 删除状态
     * 0 正常
     * 1 已删除
     */
    @Schema(description = "删除状态 0 正常 1 已删除")
    private Byte deleteStatus;

}
