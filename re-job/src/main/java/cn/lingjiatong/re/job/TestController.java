package cn.lingjiatong.re.job;

import cn.lingjiatong.re.job.bo.BaiduImageSpiderSearchConditionBO;
import cn.lingjiatong.re.job.crawler.BaiduImageSpiderProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Ling, Jiatong
 * Date: 2022/10/15 03:13
 */
@RestController
public class TestController {

    @Autowired
    private BaiduImageSpiderProcessor baiduImageSpiderProcessor;

    @GetMapping("/test")
    public void test() throws InterruptedException {
        baiduImageSpiderProcessor.setSpiderParam(BaiduImageSpiderSearchConditionBO.ONLY_SEARCH_CONDITION_PREFIX + "日向雏田");
        baiduImageSpiderProcessor.process();
    }
}
