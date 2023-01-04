package cn.lingjiatong.re.service.article.api.client;

import cn.lingjiatong.re.common.ResultVO;
import cn.lingjiatong.re.common.config.FeignBasicAuthRequestInterceptor;
import cn.lingjiatong.re.common.entity.User;
import cn.lingjiatong.re.service.article.api.vo.BackendTagListVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * 后台文章标签模块feign客户端接口层
 *
 * @author Ling, Jiatong
 * Date: 2023/1/2 20:12
 */
@FeignClient(name = "re-service-article-server", path = "/article", contextId = "BackendTagFeignClient", configuration = {FeignBasicAuthRequestInterceptor.class})
public interface BackendTagFeignClient {

    // ********************************新增类接口********************************
    // ********************************删除类接口********************************
    // ********************************修改类接口********************************
    // ********************************查询类接口********************************

    /**
     * 后端获取文章标签列表
     *
     * @param currentUser 当前登录用户
     * @return 后端获取文章标签列表VO对象列表
     */
    @GetMapping("/backend/api/v1/tag/list")
    ResultVO<List<BackendTagListVO>> findTagList(@SpringQueryMap User currentUser);
}
