package cn.lingjiatong.re.job;

import cn.lingjiatong.re.job.crawler.ToutiaoHotProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import us.codecraft.webmagic.Spider;

/**
 * @author Ling, Jiatong
 * Date: 2022/10/12 22:09
 */
@RestController
public class TestController {

    @Autowired
    private ToutiaoHotProcessor toutiaoHotProcessor;

    @GetMapping("/test")
    public void test() {
        Spider.create(toutiaoHotProcessor)
                .addUrl(ToutiaoHotProcessor.URL)
                .run();
    }

}
