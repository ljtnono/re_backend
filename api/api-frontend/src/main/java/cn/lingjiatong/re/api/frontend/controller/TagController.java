package cn.lingjiatong.re.api.frontend.controller;

import cn.lingjiatong.re.common.ResultVO;
import cn.lingjiatong.re.service.article.api.client.FrontendTagFeignClient;
import cn.lingjiatong.re.service.article.api.vo.FrontendTagListVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 前端博客标签模块接口
 *
 * @author Ling, Jiatong
 * Date: 2023/1/18 01:19
 */
@Slf4j
@RestController
@RequestMapping("/tag")
@Tag(name = "前端博客标签模块接口")
public class TagController {

    @Autowired
    private FrontendTagFeignClient frontendTagFeignClient;

    // ********************************新增类接口********************************
    // ********************************删除类接口********************************
    // ********************************修改类接口********************************
    // ********************************查询类接口********************************

    /**
     * 获取前端热门标签列表
     *
     * @return 前端博客标签列表VO对象列表
     */
    @GetMapping("/hotTagList")
    @Operation(summary = "获取前端热门标签列表", method = "GET")
    public ResultVO<List<FrontendTagListVO>> findFrontendHotTagList() {
        log.info("==========获取前端热门标签列表");
        return frontendTagFeignClient.findFrontendHotTagList();
    }

}
