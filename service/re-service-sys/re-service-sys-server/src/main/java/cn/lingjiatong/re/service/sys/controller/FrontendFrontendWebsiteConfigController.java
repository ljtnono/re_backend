package cn.lingjiatong.re.service.sys.controller;

import cn.lingjiatong.re.common.ResultVO;
import cn.lingjiatong.re.service.sys.api.client.FrontendWebsiteConfigFeignClient;
import cn.lingjiatong.re.service.sys.api.dto.FrontendWebsiteConfigDTO;
import cn.lingjiatong.re.service.sys.api.vo.FrontendWebsiteConfigVO;
import cn.lingjiatong.re.service.sys.service.FrontendWebsiteConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 前端站点设置controller层
 *
 * @author Ling, Jiatong
 * Date: 2022/9/17 18:51
 */
@Slf4j
@RestController
public class FrontendFrontendWebsiteConfigController implements FrontendWebsiteConfigFeignClient {

    @Autowired
    private FrontendWebsiteConfigService frontendWebsiteConfigService;

    @Override
    @GetMapping("/api/v1/frontendWebsiteConfig")
    public ResultVO<FrontendWebsiteConfigVO> findFrontendWebsiteConfig(FrontendWebsiteConfigDTO dto) {
        log.info("==========获取前端站点设置接口，参数：{}", dto);
        return ResultVO.success(frontendWebsiteConfigService.findFrontendWebsiteConfig(dto));
    }
}
