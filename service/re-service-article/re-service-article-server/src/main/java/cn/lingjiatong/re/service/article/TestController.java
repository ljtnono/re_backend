package cn.lingjiatong.re.service.article;

import cn.lingjiatong.re.common.annotation.PassToken;
import cn.lingjiatong.re.service.article.service.ScheduleArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Ling, Jiatong
 * Date: 2023/1/18 14:29
 */
@RestController
public class TestController {
    // ********************************新增类接口********************************
    // ********************************删除类接口********************************
    // ********************************修改类接口********************************
    // ********************************查询类接口********************************
    // ********************************私有函数********************************
    // ********************************公用函数********************************

    @Autowired
    private ScheduleArticleService scheduleArticleService;

    @GetMapping("/test")
    @PassToken
    public void test() {
        scheduleArticleService.syncArticleToES();
    }
}
