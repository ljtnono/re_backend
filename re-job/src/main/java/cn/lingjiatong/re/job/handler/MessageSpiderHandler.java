package cn.lingjiatong.re.job.handler;

import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 热搜新闻消息爬虫定时任务
 *
 * @author Ling, Jiatong
 * Date: 2022/10/11 22:12
 */
@Slf4j
@Component
public class MessageSpiderHandler {

    @XxlJob("newsMessageSpider")
    public void run() {
        log.info("=========================== demo =============================");
    }

}
