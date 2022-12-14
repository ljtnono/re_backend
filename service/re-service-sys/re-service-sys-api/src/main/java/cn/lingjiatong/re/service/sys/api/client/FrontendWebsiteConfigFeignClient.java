package cn.lingjiatong.re.service.sys.api.client;

import cn.lingjiatong.re.common.ResultVO;
import cn.lingjiatong.re.common.config.FeignBasicAuthRequestInterceptor;
import cn.lingjiatong.re.service.sys.api.dto.FrontendWebsiteConfigAddDTO;
import cn.lingjiatong.re.service.sys.api.dto.FrontendWebsiteConfigFindDTO;
import cn.lingjiatong.re.service.sys.api.vo.FrontendWebsiteConfigAddVO;
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
@FeignClient(name = "re-service-sys-server", path = "/sys", contextId = "FrontendWebsiteConfigFeignClient", configuration = {FeignBasicAuthRequestInterceptor.class})
public interface FrontendWebsiteConfigFeignClient {

    /**
     * 获取前端站点设置接口
     *
     * @param dto 获取前端站点配置DTO对象
     * @return 获取前端站点配置VO对象
     */
    @GetMapping("/frontend/api/v1/frontendWebsiteConfig")
    ResultVO<FrontendWebsiteConfigAddVO> findFrontendWebsiteConfig(@SpringQueryMap FrontendWebsiteConfigFindDTO dto);

    /**
     * 新增前端站点设置接口
     *
     * @param list 新增前端站点配置DTO对象列表
     * @return 新增前端站点配置VO对象
     */
    @PostMapping("/frontend/api/v1/frontendWebsiteConfig")
    ResultVO<?> addFrontendWebsiteConfig(@SpringQueryMap List<FrontendWebsiteConfigAddDTO> list);

    /**
     * 获取前端swiper图片列表
     *
     * @return 图片url列表
     */
    @GetMapping("/frontend/api/v1/frontendSwiperImageList")
    ResultVO<List<String>> getFrontendSwiperImageList();
}
