package cn.lingjiatong.re.api.frontend.controller;

import cn.lingjiatong.re.common.ResultVO;
import cn.lingjiatong.re.service.sys.api.client.FrontendNoticeFeignClient;
import cn.lingjiatong.re.service.sys.api.client.FrontendWebsiteConfigFeignClient;
import cn.lingjiatong.re.service.sys.api.dto.FrontendWebsiteConfigFindDTO;
import cn.lingjiatong.re.service.sys.api.vo.FrontendNoticeListVO;
import cn.lingjiatong.re.service.sys.api.vo.FrontendWebsiteConfigAddVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 首页接口controller层
 *
 * @author Ling, Jiatong
 * Date: 2022/9/17 21:31
 */
@Slf4j
@RestController
@RequestMapping("/sy")
public class SyController {

    @Autowired
    private FrontendWebsiteConfigFeignClient frontendWebsiteConfigFeignClient;
    @Autowired
    private FrontendNoticeFeignClient frontendNoticeFeignClient;

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

    /**
     * 获取前端消息通知列表
     *
     * @return 前端通知列表VO对象列表
     */
    @GetMapping("/noticeList")
    public ResultVO<List<FrontendNoticeListVO>> findNoticeList() {
        log.info("==========获取前端通知消息列表");
        return frontendNoticeFeignClient.findFrontendNoticeList();
    }

}
