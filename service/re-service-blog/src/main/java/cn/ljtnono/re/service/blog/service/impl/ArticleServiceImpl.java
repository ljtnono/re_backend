package cn.ljtnono.re.service.blog.service.impl;

import cn.ljtnono.re.service.blog.mapper.ArticleMapper;
import cn.ljtnono.re.service.blog.service.IArticleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 博客模块文章子模块service层
 *
 * @author Ling, Jiatong
 * Date: 2021/9/5 11:56 下午
 */
@Slf4j
@Service
public class ArticleServiceImpl implements IArticleService {

    @Resource
    private ArticleMapper articleMapper;

    //==================== 接口调用 ====================

    public void publicArticle() {

    }


    //==================== 私有函数 ====================

    //==================== 公共函数 ====================

    //==================== 其他函数 ====================


}
