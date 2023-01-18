package cn.lingjiatong.re.job.handler;

import cn.lingjiatong.re.common.constant.DistributedTaskStatusEnum;
import cn.lingjiatong.re.common.util.DateUtil;
import cn.lingjiatong.re.service.article.api.client.ScheduleArticleFeignClient;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * es文章同步定时任务处理器
 *
 * @author Ling, Jiatong
 * Date: 2023/1/17 19:57
 */
@Slf4j
@Component
public class ESArticleSyncHandler {
    @Autowired
    private ScheduleArticleFeignClient scheduleArticleFeignClient;

    @XxlJob("esArticleSync")
    public void run() {
        log.info("==========开始同步文章数据到es，当前时间：{}", DateUtil.getNowString("yyyy-MM-dd HH:mm:ss"));
        Integer code = scheduleArticleFeignClient.syncArticleToES().getData();
        if (DistributedTaskStatusEnum.LOCK_FAILED.getCode().equals(code)) {
            // 获取锁失败
            log.info("==========获取锁失败，本次定时任务结束，当前时间：{}", DateUtil.getNowString("yyyy-MM-dd HH:mm:ss"));
        } else if (DistributedTaskStatusEnum.ERROR_FINISHED.getCode().equals(code)) {
            // 运行出现异常
            log.info("==========运行出现异常，本次定时任务结束，当前时间：{}", DateUtil.getNowString("yyyy-MM-dd HH:mm:ss"));
        } else if (DistributedTaskStatusEnum.FINISHED.getCode().equals(code)) {
            log.info("==========结束同步文章数据到es，当前时间：{}", DateUtil.getNowString("yyyy-MM-dd HH:mm:ss"));
        }
    }

}
