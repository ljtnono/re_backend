package cn.lingjiatong.re.api.backend.controller;

import cn.lingjiatong.re.common.ResultVO;
import cn.lingjiatong.re.common.annotation.CurrentUser;
import cn.lingjiatong.re.common.entity.User;
import cn.lingjiatong.re.service.sys.api.client.BackendSystemMonitorFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Ling, Jiatong
 * Date: 2023/4/4 16:09
 */
@Slf4j
@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private BackendSystemMonitorFeignClient backendSystemMonitorFeignClient;

    // ********************************新增类接口********************************
    // ********************************删除类接口********************************
    // ********************************修改类接口********************************
    // ********************************查询类接口********************************
    // ********************************私有函数********************************
    // ********************************公用函数********************************

    @GetMapping("/test")
    public ResultVO<?> test(@RequestParam("ipAddr") String ipAddr, @CurrentUser User currentUser) {
        return backendSystemMonitorFeignClient.findK8sPodList("rootelement", currentUser);
    }
}
