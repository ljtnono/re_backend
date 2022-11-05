package cn.lingjiatong.re.service.sys.controller;

import cn.lingjiatong.re.common.ResultVO;
import cn.lingjiatong.re.service.sys.api.client.FrontendFriendLinkFeignClient;
import cn.lingjiatong.re.service.sys.api.vo.FrontendFriendLinkListVO;
import cn.lingjiatong.re.service.sys.service.FrontendFriendLinkService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 前端友情链接controller层
 *
 * @author Ling, Jiatong
 * Date: 2022/10/15 23:29
 */
@Slf4j
@RestController
public class FrontendFriendLinkController implements FrontendFriendLinkFeignClient {

    @Autowired
    private FrontendFriendLinkService frontendFriendLinkService;

    @Override
    @GetMapping("/frontend/api/v1/friendLink/list")
    public ResultVO<List<FrontendFriendLinkListVO>> findFrontendNoticeList() {
        return ResultVO.success(frontendFriendLinkService.findFrontendFriendLinkList());
    }
}
