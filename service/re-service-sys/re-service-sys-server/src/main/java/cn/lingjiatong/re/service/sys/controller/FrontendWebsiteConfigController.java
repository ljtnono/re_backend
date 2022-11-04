package cn.lingjiatong.re.service.sys.controller;

import cn.lingjiatong.re.common.ResultVO;
import cn.lingjiatong.re.service.sys.api.client.FrontendWebsiteConfigFeignClient;
import cn.lingjiatong.re.service.sys.api.dto.FrontendWebsiteConfigAddDTO;
import cn.lingjiatong.re.service.sys.api.dto.FrontendWebsiteConfigFindDTO;
import cn.lingjiatong.re.service.sys.api.vo.FrontendWebsiteConfigAddVO;
import cn.lingjiatong.re.service.sys.service.FrontendWebsiteConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 前端站点设置controller层
 *
 * @author Ling, Jiatong
 * Date: 2022/9/17 18:51
 */
@Slf4j
@RestController
public class FrontendWebsiteConfigController implements FrontendWebsiteConfigFeignClient {

    @Autowired
    private FrontendWebsiteConfigService frontendWebsiteConfigService;

    @Override
    @GetMapping("/api/v1/frontendWebsiteConfig")
    @PreAuthorize("hasAuthority('blog')")
    public ResultVO<FrontendWebsiteConfigAddVO> findFrontendWebsiteConfig(FrontendWebsiteConfigFindDTO dto) {
        return ResultVO.success(frontendWebsiteConfigService.findFrontendWebsiteConfig(dto));
    }

    @Override
    @PostMapping("/api/v1/frontendWebsiteConfig")
    public ResultVO<?> addFrontendWebsiteConfig(List<FrontendWebsiteConfigAddDTO> list) {
        frontendWebsiteConfigService.addFrontendWebsiteConfig(list);
        return ResultVO.success();
    }

    @Override
    @GetMapping("/api/v1/frontendSwiperImageList")
    public ResultVO<List<String>> getFrontendSwiperImageList() {
        return ResultVO.success(frontendWebsiteConfigService.getFrontendSwiperImageList());
    }

}
