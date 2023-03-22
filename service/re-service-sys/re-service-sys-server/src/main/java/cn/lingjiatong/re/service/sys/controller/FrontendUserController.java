package cn.lingjiatong.re.service.sys.controller;

import cn.lingjiatong.re.common.ResultVO;
import cn.lingjiatong.re.common.entity.User;
import cn.lingjiatong.re.service.sys.api.client.FrontendUserFeignClient;
import cn.lingjiatong.re.service.sys.api.vo.FrontendUserListVO;
import cn.lingjiatong.re.service.sys.service.FrontendUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 前端User模块接口
 * 
 * @author Ling, Jiatong
 * Date: 2023/1/17 10:41
 */
@RestController
public class FrontendUserController implements FrontendUserFeignClient {

    @Autowired
    private FrontendUserService frontendUserService;


    // ********************************新增类接口********************************
    // ********************************删除类接口********************************
    // ********************************修改类接口********************************
    // ********************************查询类接口********************************

    @Override
    public ResultVO<List<FrontendUserListVO>> findUserListByUserIdList(@RequestParam("userIdList") List<Long> userIdList) {
        return ResultVO.success(frontendUserService.findUserListByUserIdList(userIdList, User::getId, User::getUsername));
    }

}
