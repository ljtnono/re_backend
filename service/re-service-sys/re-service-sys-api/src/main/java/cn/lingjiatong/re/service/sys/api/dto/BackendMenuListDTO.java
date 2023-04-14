package cn.lingjiatong.re.service.sys.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 后台获取菜单列表DTO对象
 *
 * @author Ling, Jiatong
 * Date: 4/8/23 9:47 PM
 */
@Data
@Schema(name = "BackendMenuListDTO", description = "后台获取菜单列表DTO对象")
public class BackendMenuListDTO {

    /**
     * 搜索条件
     */
    @Schema(description = "搜索条件")
    private String searchCondition;
}
