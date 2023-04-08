package cn.lingjiatong.re.service.sys.api.dto;

import cn.lingjiatong.re.common.BasePageDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 后台获取菜单列表DTO对象
 *
 * @author Ling, Jiatong
 * Date: 4/8/23 9:47 PM
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name = "BackendMenuListDTO", description = "后台获取菜单列表DTO对象")
public class BackendMenuListDTO extends BasePageDTO {

    /**
     * 搜索条件
     */
    @Schema(description = "搜索条件")
    private String searchCondition;

    /**
     * 排序字段限定列表，由子类覆盖
     */
    @Schema(description = "排序字段限定列表", hidden = true)
    protected List<String> orderFieldLimitList = List.of("create_time", "modify_time");

}
