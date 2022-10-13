package cn.lingjiatong.re.job.handler;

import cn.lingjiatong.re.common.util.DateUtil;
import cn.lingjiatong.re.job.crawler.BaiduImageSpiderProcessor;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 百度图片爬虫处理器
 *
 * @author Ling, Jiatong
 * Date: 2022/10/13 21:38
 */
@Slf4j
@Component
public class BaiduImageSpiderHandler {

    @Autowired
    private BaiduImageSpiderProcessor baiduImageSpiderProcessor;

    @XxlJob("baiduImageSpider")
    public void run() throws InterruptedException {
        String jobParam = XxlJobHelper.getJobParam();
        baiduImageSpiderProcessor.setSpiderParam(jobParam);
        log.info("==========开始执行百度图片爬虫，当前时间：{}", DateUtil.getNowString("yyyy-MM-dd HH:mm:ss"));
        baiduImageSpiderProcessor.process();
        log.info("==========百度图片爬虫执行完毕，当前时间：{}", DateUtil.getNowString("yyyy-MM-dd HH:mm:ss"));
    }

}
