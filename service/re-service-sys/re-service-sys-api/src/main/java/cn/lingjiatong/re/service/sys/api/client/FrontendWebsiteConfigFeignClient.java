package cn.lingjiatong.re.service.sys.api.client;

import cn.lingjiatong.re.common.ResultVO;
import cn.lingjiatong.re.service.sys.api.dto.FrontendWebsiteConfigAddDTO;
import cn.lingjiatong.re.service.sys.api.dto.FrontendWebsiteConfigFindDTO;
import cn.lingjiatong.re.service.sys.api.vo.FrontendWebsiteConfigAddVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

/**
 * 前端站点设置feign接口层
 *
 * @author Ling, Jiatong
 * Date: 2022/9/17 18:49
 */
@FeignClient(name = "re-service-sys-server", path = "/sys")
public interface FrontendWebsiteConfigFeignClient {

    /**
     * 获取前端站点设置接口
     *
     * @param dto 获取前端站点配置DTO对象
     * @return 获取前端站点配置VO对象
     */
    @GetMapping("/api/v1/frontendWebsiteConfig")
    @ApiOperation(value = "获取前端站点设置接口", httpMethod = "GET")
    ResultVO<FrontendWebsiteConfigAddVO> findFrontendWebsiteConfig(@SpringQueryMap FrontendWebsiteConfigFindDTO dto);

    /**
     * 新增前端站点设置接口
     *
     * @param list 新增前端站点配置DTO对象列表
     * @return 新增前端站点配置VO对象
     */
    @PostMapping("/api/v1/frontendWebsiteConfig")
    @ApiOperation(value = "新增前端站点设置接口", httpMethod = "POST")
    ResultVO<?> addFrontendWebsiteConfig(@SpringQueryMap List<FrontendWebsiteConfigAddDTO> list);
}
