package cn.lingjiatong.re.service.article.controller;

import cn.lingjiatong.re.common.ResultVO;
import cn.lingjiatong.re.service.article.api.client.ScheduleArticleFeignClient;
import cn.lingjiatong.re.service.article.service.ScheduleArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 定时任务文章模块controller层
 *
 * @author Ling, Jiatong
 * Date: 2023/1/17 20:22
 */
@RestController
public class ScheduleArticleController implements ScheduleArticleFeignClient {

    @Autowired
    private ScheduleArticleService scheduleArticleService;

    // ********************************新增类接口********************************
    // ********************************删除类接口********************************
    // ********************************修改类接口********************************
    // ********************************查询类接口********************************

    @Override
    @GetMapping("/schedule/api/v1/article/syncArticleToES")
    public ResultVO<Integer> syncArticleToES() {
       return scheduleArticleService.syncArticleToES();
    }

}
