package cn.lingjiatong.re.api.frontend.controller;

import cn.lingjiatong.re.common.ResultVO;
import cn.lingjiatong.re.service.sys.api.client.FrontendNoticeFeignClient;
import cn.lingjiatong.re.service.sys.api.vo.FrontendNoticeListVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 前端博客通知消息接口
 *
 * @author Ling, Jiatong
 * Date: 2023/2/2 16:35
 */
@Slf4j
@RestController
@RequestMapping("/notice")
@Tag(name = "前端通知消息模块接口")
public class NoticeController {

    @Autowired
    private FrontendNoticeFeignClient frontendNoticeFeignClient;

    // ********************************新增类接口********************************
    // ********************************删除类接口********************************
    // ********************************修改类接口********************************
    // ********************************查询类接口********************************

    /**
     * 获取前端消息通知列表
     *
     * @return 前端通知列表VO对象列表
     */
    @GetMapping("/list")
    @Operation(summary = "获取前端消息通知列表", method = "GET")
    public ResultVO<List<FrontendNoticeListVO>> findNoticeList() {
        log.info("==========获取前端通知消息列表");
        return frontendNoticeFeignClient.findFrontendNoticeList();
    }
}
