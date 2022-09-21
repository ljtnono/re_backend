package cn.lingjiatong.re.service.sys.api.client;

import cn.lingjiatong.re.common.ResultVO;
import cn.lingjiatong.re.service.sys.api.dto.FrontendWebsiteConfigDTO;
import cn.lingjiatong.re.service.sys.api.vo.FrontendWebsiteConfigVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 前端站点设置feign接口层
 *
 * @author Ling, Jiatong
 * Date: 2022/9/17 18:49
 */
@FeignClient(name = "re-service-sys", path = "/sys")
public interface FrontendWebsiteConfigFeignClient {

    /**
     * 获取前端站点设置接口
     *
     * @param dto 获取前端站点配置DTO对象
     * @return 获取前端站点配置VO对象
     */
    @GetMapping("/api/v1/frontendWebsiteConfig")
    @ApiOperation(value = "获取站点设置接口", httpMethod = "GET")
    ResultVO<FrontendWebsiteConfigVO> findFrontendWebsiteConfig(@SpringQueryMap FrontendWebsiteConfigDTO dto);
}
