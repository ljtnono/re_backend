package cn.lingjiatong.re.job.handler;

import cn.lingjiatong.re.common.util.DateUtil;
import cn.lingjiatong.re.service.sys.api.client.ScheduleUserLoginLogFeignClient;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 用户登陆日志定期清理定时任务
 *
 * @author Ling, Jiatong
 * Date: 2022/10/11 22:12
 */
@Slf4j
@Component
public class UserLoginLogDeleteHandler {

    @Autowired
    private ScheduleUserLoginLogFeignClient scheduleUserLoginLogFeignClient;

    @XxlJob("userLoginLogDeleteSchedule")
    public void run() {
        log.info("==========开始清理过期的用户登陆日志，当前时间：{}", DateUtil.getNowString("yyyy-MM-dd HH:mm:ss"));
        scheduleUserLoginLogFeignClient.deleteUserLoginLogSchedule();
        log.info("==========头条热榜爬虫执行完毕，当前时间：{}", DateUtil.getNowString("yyyy-MM-dd HH:mm:ss"));
    }
}
