package cn.lingjiatong.re.service.article.api.client;

import cn.lingjiatong.re.common.ResultVO;
import cn.lingjiatong.re.common.config.FeignBasicAuthRequestInterceptor;
import cn.lingjiatong.re.service.article.api.dto.FrontendArticleScrollDTO;
import cn.lingjiatong.re.service.article.api.dto.FrontendArticleTopListDTO;
import cn.lingjiatong.re.service.article.api.vo.FrontendArticleScrollVO;
import cn.lingjiatong.re.service.article.api.vo.FrontendArticleTopListVO;
import cn.lingjiatong.re.service.article.api.vo.FrontendArticleVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 前端文章相关feign接口层
 *
 * @author Ling, Jiatong
 * Date: 2022/10/11 00:22
 */
@FeignClient(name = "re-service-article-server", path = "/article", contextId = "FrontendArticleFeignClient", configuration = {FeignBasicAuthRequestInterceptor.class})
public interface FrontendArticleFeignClient {

    // ********************************新增类接口********************************
    // ********************************删除类接口********************************
    // ********************************修改类接口********************************
    // ********************************查询类接口********************************

    /**
     * 前端滚动获取文章列表
     *
     * @param dto 前端文章无限滚动列表DTO对象
     * @return 前端文章无限滚动列表VO对象分页对象
     */
    @GetMapping("/frontend/api/v1/article/scroll")
    ResultVO<Page<FrontendArticleScrollVO>> findArticleScroll(@SpringQueryMap FrontendArticleScrollDTO dto);

    /**
     * 获取单个文章信息
     *
     * @param articleId 文章id
     * @return 单个文章信息VO对象
     */
    @GetMapping("/frontend/api/v1/article/{articleId}")
    ResultVO<FrontendArticleVO> findArticleById(@PathVariable("articleId") Long articleId);

    /**
     * 前端分页获取文章置顶列表
     *
     * @param dto 前端分页获取文章置顶列表DTO对象
     * @return 前端分页获取文章置顶列表VO对象分页对象
     */
    @GetMapping("/frontend/api/v1/article/topList")
    ResultVO<Page<FrontendArticleTopListVO>> findArticleTopList(@SpringQueryMap FrontendArticleTopListDTO dto);


}
