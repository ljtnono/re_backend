package cn.lingjiatong.re.service.sys.controller;

import cn.lingjiatong.re.common.ResultVO;
import cn.lingjiatong.re.service.sys.api.client.FrontendMenuFeignClient;
import cn.lingjiatong.re.service.sys.api.vo.FrontendMenuVO;
import cn.lingjiatong.re.service.sys.service.FrontendMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 前端菜单模块controller层
 *
 * @author Ling, Jiatong
 * Date: 2023/2/7 09:44
 */
@RestController
public class FrontendMenuController implements FrontendMenuFeignClient {

    @Autowired
    private FrontendMenuService frontendMenuService;

    // ********************************新增类接口********************************
    // ********************************删除类接口********************************
    // ********************************修改类接口********************************
    // ********************************查询类接口********************************

    @Override
    @GetMapping("/frontend/api/v1/menu")
    public ResultVO<List<FrontendMenuVO>> findFrontendMenu() {
        return ResultVO.success(frontendMenuService.findFrontendMenu());
    }
}
