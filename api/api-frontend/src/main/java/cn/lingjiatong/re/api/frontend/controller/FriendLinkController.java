package cn.lingjiatong.re.api.frontend.controller;

import cn.lingjiatong.re.common.ResultVO;
import cn.lingjiatong.re.service.sys.api.client.FrontendFriendLinkFeignClient;
import cn.lingjiatong.re.service.sys.api.vo.FrontendFriendLinkListVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 前端博客友情链接模块接口
 *
 * @author Ling, Jiatong
 * Date: 2022/9/17 21:31
 */
@Slf4j
@RestController
@RequestMapping("/friendLink")
@Tag(name = "前端友情链接模块接口")
public class FriendLinkController {

    @Autowired
    private FrontendFriendLinkFeignClient frontendFriendLinkFeignClient;

    // ********************************新增类接口********************************
    // ********************************删除类接口********************************
    // ********************************修改类接口********************************
    // ********************************查询类接口********************************

    /**
     * 获取前端友情链接列表
     *
     * @return 前端友情链接列表VO对象列表
     */
    @GetMapping("/list")
    @Operation(summary = "获取前端友情链接列表", method = "GET")
    public ResultVO<List<FrontendFriendLinkListVO>> findFriendLinkList() {
        log.info("==========获取前端友情链接列表");
        return frontendFriendLinkFeignClient.findFrontendNoticeList();
    }
}
