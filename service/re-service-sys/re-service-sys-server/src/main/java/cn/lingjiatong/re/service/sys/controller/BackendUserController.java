package cn.lingjiatong.re.service.sys.controller;

import cn.lingjiatong.re.common.ResultVO;
import cn.lingjiatong.re.common.entity.User;
import cn.lingjiatong.re.service.sys.api.client.BackendUserFeignClient;
import cn.lingjiatong.re.service.sys.api.dto.BackendUserListDTO;
import cn.lingjiatong.re.service.sys.api.dto.BackendUserSaveDTO;
import cn.lingjiatong.re.service.sys.api.dto.BackendUserUpdateDTO;
import cn.lingjiatong.re.service.sys.api.dto.BackendUserUpdateDeleteStatusBatchDTO;
import cn.lingjiatong.re.service.sys.api.vo.BackendUserListVO;
import cn.lingjiatong.re.service.sys.service.BackendUserService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
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

    // ********************************其他微服务调用********************************

    @Override
    @GetMapping("/backend/api/v1/user/findUserByUserIdList")
    public ResultVO<List<BackendUserListVO>> findUserListByUserIdList(@RequestParam("userIdList") List<Long> userIdList) {
        return ResultVO.success(backendUserService.findUserListByUserIdList(userIdList));
    }

}
