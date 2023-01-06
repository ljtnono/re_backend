package cn.lingjiatong.re.service.sys.api.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Map;

/**
 * 获取前端站点配置VO对象
 *
 * @author Ling, Jiatong
 * Date: 2022/9/17 18:59
 */
@Data
@Schema(name = "FrontendWebsiteConfigAddVO", description = "获取前端站点配置VO对象")
public class FrontendWebsiteConfigAddVO {

    /**
     * 站点配置列表
     */
    @Schema(description = "站点配置列表")
    private Map<String, String> values;
}
