package cn.lingjiatong.re.service.sys.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 更新前端站点配置DTO对象
 *
 * @author Ling, Jiatong
 * Date: 2022/10/8 20:44
 */
@Data
@ApiModel(description = "更新前端站点配置DTO对象")
public class FrontendWebsiteConfigUpdateDTO {

    /**
     * 配置描述
     */
    @ApiModelProperty("配置的描述，默认描述为无")
    private String description;

    /**
     * 配置的健
     */
    @ApiModelProperty(value = "配置的健", required = true)
    private String key;

    /**
     * 配置的值
     */
    @ApiModelProperty(value = "配置的值", required = true)
    private String value;
}
