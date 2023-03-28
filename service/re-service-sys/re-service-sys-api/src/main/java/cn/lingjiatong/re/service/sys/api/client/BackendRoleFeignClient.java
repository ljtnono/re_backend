package cn.lingjiatong.re.service.sys.api.client;

import cn.lingjiatong.re.common.ResultVO;
import cn.lingjiatong.re.common.config.FeignBasicAuthRequestInterceptor;
import cn.lingjiatong.re.common.entity.User;
import cn.lingjiatong.re.service.sys.api.dto.BackendRolePageListDTO;
import cn.lingjiatong.re.service.sys.api.dto.BackendRoleSaveDTO;
import cn.lingjiatong.re.service.sys.api.vo.BackendRoleListVO;
import cn.lingjiatong.re.service.sys.api.vo.BackendRoleMenuTreeVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

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

    /**
     * 保存角色
     *
     * @param dto 后台保存角色DTO对象
     * @param currentUser 当前登陆用户
     * @return 通用消息返回对象
     */
    @PostMapping("/backend/api/v1/role/save")
    ResultVO<?> saveRole(@RequestBody BackendRoleSaveDTO dto, @SpringQueryMap User currentUser);

    // ********************************删除类接口********************************
    // ********************************修改类接口********************************
    // ********************************查询类接口********************************

    /**
     * 后台获取角色列表
     *
     * @param currentUser 当前用户
     * @return 后台角色列表VO对象列表
     */
    @GetMapping("/backend/api/v1/role/list")
    ResultVO<List<BackendRoleListVO>> findRoleList(@SpringQueryMap User currentUser);


    /**
     * 后台分页获取角色列表
     *
     * @param dto 后台分页获取角色列表DTO对象
     * @param currentUser 当前登陆用户
     * @return 后台获取角色列表VO对象分页对象
     */
    @GetMapping("/backend/api/v1/role/pageList")
    ResultVO<Page<BackendRoleListVO>> findRolePageList(@SpringQueryMap BackendRolePageListDTO dto, @SpringQueryMap User currentUser);

    /**
     * 后台获取角色的菜单树
     *
     * @param roleId 角色id
     * @param currentUser 当前登陆角色
     * @return 角色菜单树VO对象
     */
    @GetMapping("/backend/api/v1/role/menuTree/{roleId}")
    ResultVO<BackendRoleMenuTreeVO> findRoleMenuTree(@PathVariable("roleId") Long roleId, @SpringQueryMap User currentUser);

    // ********************************私有函数********************************
    // ********************************公用函数********************************

}
