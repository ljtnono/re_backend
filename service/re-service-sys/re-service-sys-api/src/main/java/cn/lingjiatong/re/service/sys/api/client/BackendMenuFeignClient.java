package cn.lingjiatong.re.service.sys.api.client;

import cn.lingjiatong.re.common.ResultVO;
import cn.lingjiatong.re.common.config.FeignBasicAuthRequestInterceptor;
import cn.lingjiatong.re.common.entity.User;
import cn.lingjiatong.re.service.sys.api.vo.BackendMenuTreeVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * 后台菜单模块feign客户端接口
 *
 * @author Ling, Jiatong
 * Date: 3/26/23 10:20 PM
 */
@FeignClient(value = "re-service-sys-server", path = "/sys", contextId = "BackendMenuFeignClient", configuration = {FeignBasicAuthRequestInterceptor.class})
public interface BackendMenuFeignClient {

    // ********************************新增类接口********************************
    // ********************************删除类接口********************************
    // ********************************修改类接口********************************
    // ********************************查询类接口********************************

    /**
     * 获取后台所有的菜单树
     *
     * @param currentUser 当前登陆用户
     * @return 后台获取菜单树VO对象列表
     */
    @GetMapping("/backend/api/v1/backendMenuTree")
    ResultVO<List<BackendMenuTreeVO>> findBackendMenuTree(@SpringQueryMap User currentUser);

}
