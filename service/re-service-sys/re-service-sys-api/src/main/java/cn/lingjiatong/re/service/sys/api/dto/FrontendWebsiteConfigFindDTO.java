package cn.lingjiatong.re.service.sys.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 获取前端站点配置DTO对象
 *
 * @author Ling, Jiatong
 * Date: 2022/9/17 18:59
 */
@Data
@ApiModel(description = "获取前端站点配置DTO对象")
public class FrontendWebsiteConfigFindDTO {

    /**
     * 获取类型
     * 获取所有前端站点配置 1
     * 获取部分前端站点配置 2
     */
    @ApiModelProperty(value = "获取类型 获取所有前端站点配置 1 获取部分前端站点配置 2")
    private Integer acquireType;

    /**
     * 获取站点配置的Key列表
     */
    @ApiModelProperty("获取前端站点配置的Key列表 当获取部分前端配置时需要此参数")
    private List<String> acquireKeyList;
}
