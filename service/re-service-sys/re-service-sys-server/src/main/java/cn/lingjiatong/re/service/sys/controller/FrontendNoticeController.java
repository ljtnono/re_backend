package cn.lingjiatong.re.service.sys.controller;

import cn.lingjiatong.re.common.ResultVO;
import cn.lingjiatong.re.service.sys.api.client.FrontendNoticeFeignClient;
import cn.lingjiatong.re.service.sys.api.vo.FrontendNoticeListVO;
import cn.lingjiatong.re.service.sys.service.FrontendNoticeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 前端通知消息controller层
 *
 * @author Ling, Jiatong
 * Date: 2022/10/15 14:20
 */
@Slf4j
@RestController
public class FrontendNoticeController implements FrontendNoticeFeignClient {

    @Autowired
    private FrontendNoticeService frontendNoticeService;

    @Override
    @GetMapping("/frontend/api/v1/notice/list")
    public ResultVO<List<FrontendNoticeListVO>> findFrontendNoticeList() {
        return ResultVO.success(frontendNoticeService.findFrontendNoticeList());
    }
}
