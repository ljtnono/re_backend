package cn.lingjiatong.re.service.sys.api.dto;

import cn.lingjiatong.re.common.BasePageDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 后台分页获取角色列表DTO对象
 *
 * @author Ling, JiatongCommonConstant
 * Date: 3/25/23 12:17 AM
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name = "BackendRolePageListDTO", description = "后台分页获取角色列表DTO对象")
public class BackendRolePageListDTO extends BasePageDTO {

    /**
     * 查询条件
     */
    @Schema(description = "查询条件")
    private String searchCondition;

    /**
     * 排序字段限定列表，由子类覆盖
     */
    @Schema(description = "排序字段限定列表", hidden = true)
    protected List<String> orderFieldLimitList = List.of("create_time", "modify_time");

}
