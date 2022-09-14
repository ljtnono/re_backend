package cn.ljtnono.re.service.blog.controller;

import cn.ljtnono.re.service.blog.service.IArticleService;
import cn.ljtnono.re.common.ResultVO;
import cn.ljtnono.re.entity.dto.blog.article.PublishArticleDTO;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.security.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 博客模块文章子模块接口
 *
 * @author Ling, Jiatong
 * Date: 2021/9/5 11:44 下午
 */
@Slf4j
@RestController
@RequestMapping("/article")
public class ArticleController {

    @Autowired
    private IArticleService iArticleService;

    //==================== 新增类接口 ====================

    /**
     * 发布文章
     *
     * @param dto 发布博客文章DTO对象
     * @param user 当前登陆用户
     * @return 通用消息返回对象
     * @author Ling, Jiatong
     */
    @PostMapping("/publish")
    @ApiOperation(value = "发布文章", httpMethod = "POST")
    public ResultVO<?> publishArticle(@RequestBody PublishArticleDTO dto, User user) {
        log.info("发布文章，参数：{}", dto);
        return ResultVO.success();
    }


    //==================== 获取类接口 ====================


    /***
     * 获取博客文章详情
     *
     * @param articleId 博客文章id
     * @param user 当前登录对象
     * @return 通用消息返回对象
     * @author Ling, Jiatong
     */
    @GetMapping("/{articleId}")
    @ApiOperation(value = "获取博客文章详情", httpMethod = "GET")
    public ResultVO<?> getArticleDetail(@PathVariable Integer articleId, User user) {
        log.info("获取博客文章详情，参数：{}", articleId);
        return ResultVO.success();
    }

    //==================== 修改类接口 ====================

    //==================== 删除类接口 ====================

}
