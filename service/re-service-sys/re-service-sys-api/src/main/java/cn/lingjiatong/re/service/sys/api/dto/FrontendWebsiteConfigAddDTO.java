package cn.lingjiatong.re.service.sys.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 新增前端站点配置DTO对象
 *
 * @author Ling, Jiatong
 * Date: 2022/10/8 20:44
 */
@Data
@Schema(name = "FrontendWebsiteConfigAddDTO", description = "新增前端站点配置DTO对象")
public class FrontendWebsiteConfigAddDTO {

    /**
     * 配置描述
     */
    @Schema(description = "配置的描述，默认描述为无")
    private String description;

    /**
     * 配置的健
     */
    @Schema(description = "配置的健", required = true)
    private String key;

    /**
     * 配置的值
     */
    @Schema(description = "配置的值", required = true)
    private String value;
}
