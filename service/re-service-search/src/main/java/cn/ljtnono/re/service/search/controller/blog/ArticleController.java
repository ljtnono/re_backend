package cn.ljtnono.re.service.search.controller.blog;

import cn.ljtnono.re.common.ResultVO;
import cn.ljtnono.re.service.search.service.blog.IArticleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 博客文章查询模块接口
 *
 * @author Ling, Jiatong
 * Date: 2021/8/10 12:19 上午
 */
@Slf4j
@RestController
@RequestMapping("/search/blog/article")
public class ArticleController {

    @Resource
    private IArticleService iArticleService;


    @GetMapping("/")
    public ResultVO<?> searchArticle() {
        return ResultVO.success();
    }
}
