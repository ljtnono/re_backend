package cn.lingjiatong.re.job.crawler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * 文章封面图片爬虫
 *
 * @author Ling, Jiatong
 * Date: 2022/10/12 22:37
 */
@Slf4j
@Component
public class ArticleCoverImageProcessor implements PageProcessor {

    // 抓取网站的相关配置，包括编码、抓取间隔、重试次数等
    private Site site = Site.me().setRetryTimes(3).setSleepTime(1000);

    //
    public static final String URL = "";

    @Override
    public void process(Page page) {

    }

    @Override
    public Site getSite() {
        return site;
    }

}
