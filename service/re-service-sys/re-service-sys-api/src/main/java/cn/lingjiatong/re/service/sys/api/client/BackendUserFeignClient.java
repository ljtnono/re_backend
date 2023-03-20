package cn.lingjiatong.re.service.sys.api.client;

import cn.lingjiatong.re.common.ResultVO;
import cn.lingjiatong.re.common.config.FeignBasicAuthRequestInterceptor;
import cn.lingjiatong.re.common.entity.User;
import cn.lingjiatong.re.service.sys.api.dto.BackendUserListDTO;
import cn.lingjiatong.re.service.sys.api.dto.BackendUserSaveDTO;
import cn.lingjiatong.re.service.sys.api.dto.BackendUserUpdateDTO;
import cn.lingjiatong.re.service.sys.api.dto.BackendUserUpdateDeleteStatusBatchDTO;
import cn.lingjiatong.re.service.sys.api.vo.BackendUserListVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 后端admin项目用户模块Feign客户端层
 *
 * @author Ling, Jiatong
 * Date: 2022/10/31 20:27
 */
@FeignClient(value = "re-service-sys-server", path = "/sys", contextId = "BackendUserFeignClient", configuration = {FeignBasicAuthRequestInterceptor.class})
public interface BackendUserFeignClient {

    // ********************************新增类接口********************************

    /**
     * 后台保存用户信息
     *
     * @param dto 后台保存用户DTO对象
     * @param currentUser 当前登录用户
     * @return 通用消息返回对象
     */
    @PostMapping("/backend/api/v1/user/save")
    ResultVO<?> saveUser(@RequestBody BackendUserSaveDTO dto, @SpringQueryMap User currentUser);


    // ********************************删除类接口********************************
    // ********************************修改类接口********************************

    /**
     * 批量更改用户删除状态
     * 此方法只有管理员能调用
     *
     * @param dto 后台批量更改用户删除状态DTO对象
     * @param currentUser 当前用户
     * @return 通用消息返回对象
     */
    @PutMapping("/backend/api/v1/user/updateDeleteStatusBatch")
    ResultVO<?> updateUserDeleteStatusBatch(@SpringQueryMap BackendUserUpdateDeleteStatusBatchDTO dto, @SpringQueryMap User currentUser);

    /**
     * 更改用户信息-管理员
     * 此方法只有管理员能够调用
     *
     * @param dto 后台编辑用户信息DTO对象
     * @return 通用消息返回对象
     */
    @PutMapping("/backend/api/v1/user/updateUser")
    ResultVO<?> adminUpdateUser(@RequestBody BackendUserUpdateDTO dto);

    /**
     * 更改用户信息-个人
     *
     * @param dto 后台编辑用户信息DTO对象
     * @param currentUser 当前登录用户
     * @return 通用消息返回对象
     */
    @PutMapping("/backend/api/v1/user")
    ResultVO<?> updateUser(@RequestBody BackendUserUpdateDTO dto, @SpringQueryMap User currentUser);

    // ********************************查询类接口********************************

    /**
     * 分页查询用户列表
     *
     * @param dto 后台获取用户列表DTO对象
     * @return 后台获取用户列表VO对象分页对象
     */
    @GetMapping("/backend/api/v1/user/list")
    ResultVO<Page<BackendUserListVO>> findUserList(@SpringQueryMap BackendUserListDTO dto, @SpringQueryMap User currentUser);

    /**
     * 校验用户名是否重复
     *
     * @param username 用户名
     * @param currentUser 当前用户
     * @return 重复返回true，不重复返回false
     */
    @GetMapping("/backend/api/v1/user/testUsernameDuplicate")
    ResultVO<Boolean> testUsernameDuplicate(@RequestParam("username") String username, @SpringQueryMap User currentUser);

    // ********************************其他微服务调用********************************

    /**
     * 根据用户id列表获取用户列表
     *
     * @param userIdList 用户id列表
     * @return 后台获取用户列表VO对象列表
     */
    @GetMapping("/backend/api/v1/user/findUserByUserIdList")
    ResultVO<List<BackendUserListVO>> findUserListByUserIdList(@RequestParam("userIdList") List<Long> userIdList);
}
