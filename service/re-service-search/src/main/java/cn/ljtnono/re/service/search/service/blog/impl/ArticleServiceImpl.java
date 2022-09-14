package cn.ljtnono.re.service.search.service.blog.impl;

import cn.ljtnono.re.service.search.service.blog.IArticleService;
import cn.ljtnono.re.common.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.stereotype.Service;

/**
 * 博客查询模块service实现
 *
 * @author Ling, Jiatong
 * Date: 2021/8/11 12:25 上午
 */
@Service
public class ArticleServiceImpl implements IArticleService {

    @Autowired
    private ElasticsearchRestTemplate template;

    //==================== 接口调用 ====================


    public ResultVO<?> searchArticle() {
        return new ResultVO<Object>();
    }


    //==================== 公共方法 ====================


    //==================== 私有方法 ====================


}
