package cn.lingjiatong.re.service.sys.controller;

import cn.lingjiatong.re.common.ResultVO;
import cn.lingjiatong.re.common.entity.User;
import cn.lingjiatong.re.service.sys.api.client.BackendUserFeignClient;
import cn.lingjiatong.re.service.sys.api.dto.*;
import cn.lingjiatong.re.service.sys.api.vo.BackendUserListVO;
import cn.lingjiatong.re.service.sys.service.BackendUserService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 后台管理系统User模块接口
 *
 * @author Ling, Jiatong
 * Date: 2022/10/30 09:30
 */
@RestController
public class BackendUserController implements BackendUserFeignClient {

    @Autowired
    private BackendUserService backendUserService;

    // ********************************新增类接口********************************


    @Override
    @PostMapping("/backend/api/v1/user/save")
    public ResultVO<?> saveUser(@RequestBody BackendUserSaveDTO dto, User currentUser) {
        backendUserService.saveUser(dto, currentUser);
        return ResultVO.success();
    }

    // ********************************删除类接口********************************

    @Override
    @DeleteMapping("/backend/api/v1/user/deleteBatch")
    public ResultVO<?> deleteUserBatch(@RequestBody BackendUserPhysicDeleteBatchDTO dto, User currentUser) {
        backendUserService.physicDeleteUserBatch(dto, currentUser);
        return ResultVO.success();
    }

    // ********************************修改类接口********************************

    @Override
    @PutMapping("/backend/api/v1/user/updateDeleteStatusBatch")
    public ResultVO<?> updateUserDeleteStatusBatch(@RequestBody BackendUserUpdateDeleteStatusBatchDTO dto, User currentUser) {
        backendUserService.updateUserDeleteStatusBatch(dto, currentUser);
        return ResultVO.success();
    }

    @Override
    @PutMapping("/backend/api/v1/user/updateUser")
    public ResultVO<?> adminUpdateUser(BackendUserUpdateDTO dto) {
        backendUserService.adminUpdateUser(dto);
        return ResultVO.success();
    }

    @Override
    @PutMapping("/backend/api/v1/user")
    public ResultVO<?> updateUser(@RequestBody BackendUserUpdateDTO dto, User currentUser) {
        backendUserService.updateUser(dto, currentUser);
        return ResultVO.success();
    }

    // ********************************查询类接口********************************

    @Override
    @GetMapping("/backend/api/v1/user/list")
    public ResultVO<Page<BackendUserListVO>> findUserList(BackendUserListDTO dto, User currentUser) {
        return ResultVO.success(backendUserService.findUserList(dto, currentUser));
    }

    @Override
    @GetMapping("/backend/api/v1/user/testUsernameDuplicate")
    public ResultVO<Boolean> testUsernameDuplicate(@RequestParam("username") String username, User currentUser) {
        // 由于这里openfeign会将用户名封装为map,这里的username参数获取到会变为两个用逗号隔开的值，值是一模一样的，任意取其中一个都行
        username = username.split(",")[0];
        return ResultVO.success(backendUserService.testUsernameDuplicate(username, currentUser));
    }

    @Override
    @GetMapping("/backend/api/v1/user/testEmailDuplicate")
    public ResultVO<Boolean> testEmailDuplicate(@RequestParam("email") String email, User currentUser) {
        // 由于这里openfeign会将用户名封装为map,这里的email参数获取到会变为两个用逗号隔开的值，值是一模一样的，任意取其中一个都行
        email = email.split(",")[0];
        return ResultVO.success(backendUserService.testEmailDuplicate(email, currentUser));
    }

    // ********************************其他微服务调用********************************

    @Override
    @GetMapping("/backend/api/v1/user/findUserByUserIdList")
    public ResultVO<List<BackendUserListVO>> findUserListByUserIdList(@RequestParam("userIdList") List<Long> userIdList) {
        return ResultVO.success(backendUserService.findUserListByUserIdList(userIdList));
    }

}
