package cn.lingjiatong.re.service.sys.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 获取前端站点配置DTO对象
 *
 * @author Ling, Jiatong
 * Date: 2022/9/17 18:59
 */
@Data
@Schema(name = "FrontendWebsiteConfigFindDTO", description = "获取前端站点配置DTO对象")
public class FrontendWebsiteConfigFindDTO {

    /**
     * 获取类型
     * 获取所有前端站点配置 1
     * 获取部分前端站点配置 2
     */
    @Schema(description = "获取类型 获取所有前端站点配置 1 获取部分前端站点配置 2")
    private Integer acquireType;

    /**
     * 获取站点配置的Key列表
     */
    @Schema(description = "获取前端站点配置的Key列表 当获取部分前端配置时需要此参数")
    private List<String> acquireKeyList;
}
