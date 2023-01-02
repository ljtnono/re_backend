package cn.lingjiatong.re.service.article.api.client;

import cn.lingjiatong.re.common.ResultVO;
import cn.lingjiatong.re.service.article.api.vo.FrontendTagListVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * 前端文章标签相关feign接口层
 *
 * @author Ling, Jiatong
 * Date: 2022/10/16 12:00
 */
@FeignClient(name = "re-service-article-server", path = "/article", contextId = "FrontendTagFeignClient")
public interface FrontendTagFeignClient {

    // ********************************新增类接口********************************
    // ********************************删除类接口********************************
    // ********************************修改类接口********************************
    // ********************************查询类接口********************************

    /**
     * 获取前端热门标签列表
     *
     * @return 前端博客标签列表VO对象列表
     */
    @GetMapping("/frontend/api/v1/tag/hotTagList")
    @ApiOperation(value = "获取前端热门标签列表", httpMethod = "GET")
    ResultVO<List<FrontendTagListVO>> findFrontendHotTagList();

}
