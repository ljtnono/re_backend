package cn.lingjiatong.re.api.frontend.controller;

import cn.lingjiatong.re.common.ResultVO;
import cn.lingjiatong.re.service.sys.api.client.FrontendMenuFeignClient;
import cn.lingjiatong.re.service.sys.api.vo.FrontendMenuVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 前端菜单模块接口
 *
 * @author Ling, Jiatong
 * Date: 2023/2/2 16:59
 */
@Slf4j
@RestController
@RequestMapping("/menu")
@Tag(name = "前端菜单模块接口")
public class MenuController {

    @Autowired
    private FrontendMenuFeignClient frontendMenuFeignClient;

    // ********************************新增类接口********************************
    // ********************************删除类接口********************************
    // ********************************修改类接口********************************
    // ********************************查询类接口********************************

    /**
     * 前端获取菜单
     *
     * @return 前端获取菜单VO对象
     */
    @GetMapping
    @Operation(summary = "前端获取菜单", method = "GET")
    public ResultVO<List<FrontendMenuVO>> getMenu() {
        log.info("==========前端获取菜单");
        return frontendMenuFeignClient.findFrontendMenu();
    }
}
