package cn.lingjiatong.re.api.controller;

import cn.lingjiatong.re.common.ResultVO;
import cn.lingjiatong.re.service.sys.api.client.WebsiteConfigFeignClient;
import cn.lingjiatong.re.service.sys.api.dto.WebsiteConfigDTO;
import cn.lingjiatong.re.service.sys.api.vo.WebsiteConfigVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 首页接口controller层
 *
 * @author Ling, Jiatong
 * Date: 2022/9/17 21:31
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/index")
public class IndexController {
    @Autowired
    private WebsiteConfigFeignClient websiteConfigFeignClient;

    @GetMapping("/websiteConfig")
    public ResultVO<WebsiteConfigVO> findWebsiteConfig(WebsiteConfigDTO dto) {
        log.info(">>>>>>>>>>>>>");
        return websiteConfigFeignClient.findWebsiteConfig(dto);
    }
    
}
