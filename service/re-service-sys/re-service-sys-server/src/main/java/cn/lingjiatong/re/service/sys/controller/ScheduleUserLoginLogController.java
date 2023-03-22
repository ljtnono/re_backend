package cn.lingjiatong.re.service.sys.controller;

import cn.lingjiatong.re.common.annotation.PassToken;
import cn.lingjiatong.re.service.sys.api.client.ScheduleUserLoginLogFeignClient;
import cn.lingjiatong.re.service.sys.service.UserLoginLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户登陆日志controller层
 *
 * @author Ling, Jiatong
 * Date: 3/22/23 10:43 PM
 */
@RestController
public class ScheduleUserLoginLogController implements ScheduleUserLoginLogFeignClient {

    @Autowired
    private UserLoginLogService userLoginLogService;

    @Override
    @GetMapping("/schedule/api/v1/article/syncArticleToES")
    @PassToken
    public void deleteUserLoginLogSchedule() {
        userLoginLogService.deleteUserLoginLogSchedule();
    }

    // ********************************新增类接口********************************
    // ********************************删除类接口********************************
    // ********************************修改类接口********************************
    // ********************************查询类接口********************************

}
