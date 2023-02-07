package cn.lingjiatong.re.service.sys.api.client;

import cn.lingjiatong.re.common.ResultVO;
import cn.lingjiatong.re.common.config.FeignBasicAuthRequestInterceptor;
import cn.lingjiatong.re.service.sys.api.vo.FrontendMenuVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * 前端菜单模块feign接口
 *
 * @author Ling, Jiatong
 * Date: 2023/2/7 09:20
 */
@FeignClient(name = "re-service-sys-server", path = "/sys", contextId = "FrontendMenuFeignClient", configuration = {FeignBasicAuthRequestInterceptor.class})
public interface FrontendMenuFeignClient {
    // ********************************新增类接口********************************
    // ********************************删除类接口********************************
    // ********************************修改类接口********************************
    // ********************************查询类接口********************************

    /**
     * 获取前端菜单
     *
     * @return 前端获取菜单VO对象
     */
    @GetMapping("/frontend/api/v1/menu")
    ResultVO<List<FrontendMenuVO>> findFrontendMenu();
}
