package cn.lingjiatong.re.job.handler;

import cn.lingjiatong.re.common.util.DateUtil;
import cn.lingjiatong.re.job.crawler.ToutiaoHotProcessor;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Spider;

/**
 * 热搜新闻消息爬虫定时任务
 *
 * @author Ling, Jiatong
 * Date: 2022/10/11 22:12
 */
@Slf4j
@Component
public class ToutiaoHotSpiderHandler {

    @Autowired
    private ToutiaoHotProcessor toutiaoHotProcessor;

    @XxlJob("toutiaoHotSpider")
    public void run() {
        log.info("==========开始执行头条热榜爬虫，当前时间：{}", DateUtil.getNowString("yyyy-MM-dd HH:mm:ss"));
        Spider.create(toutiaoHotProcessor)
                .addUrl(ToutiaoHotProcessor.URL)
                .run();
        log.info("==========头条热榜爬虫执行完毕，当前时间：{}", DateUtil.getNowString("yyyy-MM-dd HH:mm:ss"));
    }

}
