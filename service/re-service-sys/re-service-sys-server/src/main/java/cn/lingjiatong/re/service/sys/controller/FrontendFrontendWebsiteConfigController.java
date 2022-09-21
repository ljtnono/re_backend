package cn.lingjiatong.re.service.sys.controller;

import cn.lingjiatong.re.common.ResultVO;
import cn.lingjiatong.re.service.sys.api.client.WebsiteConfigFeignClient;
import cn.lingjiatong.re.service.sys.api.dto.FrontendWebsiteConfigDTO;
import cn.lingjiatong.re.service.sys.api.vo.FrontendWebsiteConfigVO;
import cn.lingjiatong.re.service.sys.service.FrontendWebsiteConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 前端站点配置类接口实现
 *
 * @author Ling, Jiatong
 * Date: 2022/9/17 18:51
 */
@Slf4j
@RestController
public class FrontendWebsiteConfigController implements WebsiteConfigFeignClient {

    @Autowired
    private FrontendWebsiteConfigService frontendWebsiteConfigService;

    @Override
    @GetMapping("/api/v1/websiteConfig")
    public ResultVO<FrontendWebsiteConfigVO> findFrontendWebsiteConfig(FrontendWebsiteConfigDTO dto) {
        log.info("==========");
        return ResultVO.success(new FrontendWebsiteConfigVO());
    }
}
