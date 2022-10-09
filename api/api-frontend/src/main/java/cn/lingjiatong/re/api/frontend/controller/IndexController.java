package cn.lingjiatong.re.api.frontend.controller;

import cn.lingjiatong.re.common.ResultVO;
import cn.lingjiatong.re.service.sys.api.client.FrontendWebsiteConfigFeignClient;
import cn.lingjiatong.re.service.sys.api.dto.FrontendWebsiteConfigFindDTO;
import cn.lingjiatong.re.service.sys.api.vo.FrontendWebsiteConfigAddVO;
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
@RequestMapping("/index")
public class IndexController {
    @Autowired
    private FrontendWebsiteConfigFeignClient frontendWebsiteConfigFeignClient;

    /**
     * 获取前端站点设置
     *
     * @param dto 获取前端站点配置DTO对象
     * @return 获取前端站点配置VO对象
     */
    @GetMapping("/frontendWebsiteConfig")
    public ResultVO<FrontendWebsiteConfigAddVO> findWebsiteConfig(FrontendWebsiteConfigFindDTO dto) {
        log.info("==========获取前端站点设置接口，参数：{}", dto);
        return frontendWebsiteConfigFeignClient.findFrontendWebsiteConfig(dto);
    }

}
