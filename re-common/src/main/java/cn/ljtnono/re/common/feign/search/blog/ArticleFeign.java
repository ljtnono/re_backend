package cn.ljtnono.re.common.feign.search.blog;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 博客文章搜索Feign接口
 *
 * @author Ling, Jiatong
 * Date: 2021/10/20 11:18 下午
 */
@FeignClient
@RequestMapping("/search/blog/article")
public interface ArticleFeign {



}
