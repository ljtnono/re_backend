package cn.lingjiatong.re.service.sys.api.client;

import cn.lingjiatong.re.common.ResultVO;
import cn.lingjiatong.re.common.config.FeignBasicAuthRequestInterceptor;
import cn.lingjiatong.re.common.entity.User;
import cn.lingjiatong.re.service.sys.api.dto.BackendUserListDTO;
import cn.lingjiatong.re.service.sys.api.dto.BackendUserUpdateDTO;
import cn.lingjiatong.re.service.sys.api.dto.BackendUserUpdateDeleteStatusBatchDTO;
import cn.lingjiatong.re.service.sys.api.vo.BackendUserListVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 后端admin项目用户模块Feign客户端层
 *
 * @author Ling, Jiatong
 * Date: 2022/10/31 20:27
 */
@FeignClient(value = "re-service-sys-server", path = "/sys", contextId = "BackendUserFeignClient", configuration = {FeignBasicAuthRequestInterceptor.class})
public interface BackendUserFeignClient {

    // ********************************新增类接口********************************
    // ********************************删除类接口********************************
    // ********************************修改类接口********************************

    /**
     * 批量更改用户删除状态
     * 此方法只有管理员能调用
     *
     * @param dto 后台批量更改用户删除状态DTO对象
     * @return 通用消息返回对象
     */
    @PutMapping("/backend/api/v1/user/updateDeleteStatusBatch")
    ResultVO<?> updateUserDeleteStatusBatch(@RequestBody BackendUserUpdateDeleteStatusBatchDTO dto);

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
    ResultVO<?> updateUser(@RequestBody BackendUserUpdateDTO dto, User currentUser);

    // ********************************查询类接口********************************

    /**
     * 分页查询用户列表
     *
     * @param dto 后台获取用户列表DTO对象
     * @return 后台获取用户列表VO对象分页对象
     */
    @GetMapping("/backend/api/v1/user/list")
    ResultVO<IPage<BackendUserListVO>> findUserList(@SpringQueryMap BackendUserListDTO dto);

}
