package cn.lingjiatong.re.service.sys.api.client;

import cn.lingjiatong.re.common.ResultVO;
import cn.lingjiatong.re.common.config.FeignBasicAuthRequestInterceptor;
import cn.lingjiatong.re.common.entity.User;
import cn.lingjiatong.re.service.sys.api.vo.BackendRoleListVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.List;

/**
 * 后端角色模块feign客户端接口
 *
 * @author Ling, Jiatong
 * Date: 2023/3/3 14:35
 */
@FeignClient(value = "re-service-sys-server", path = "/sys", contextId = "BackendRoleFeignClient", configuration = {FeignBasicAuthRequestInterceptor.class})
public interface BackendRoleFeignClient {

    // ********************************新增类接口********************************
    // ********************************删除类接口********************************
    // ********************************修改类接口********************************
    // ********************************查询类接口********************************
    // ********************************私有函数********************************
    // ********************************公用函数********************************

    /**
     * 后台获取列表
     *
     * @param currentUser 当前用户
     * @return 后台角色列表VO对象列表
     */
    @GetMapping("/backend/api/v1/role/list")
    ResultVO<List<BackendRoleListVO>> findRoleList(@SpringQueryMap User currentUser);
}
