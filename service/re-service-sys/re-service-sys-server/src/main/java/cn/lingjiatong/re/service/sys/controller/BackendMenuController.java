package cn.lingjiatong.re.service.sys.controller;

import cn.lingjiatong.re.common.ResultVO;
import cn.lingjiatong.re.common.entity.User;
import cn.lingjiatong.re.service.sys.api.client.BackendMenuFeignClient;
import cn.lingjiatong.re.service.sys.api.dto.BackendMenuListDTO;
import cn.lingjiatong.re.service.sys.api.dto.BackendMenuSaveDTO;
import cn.lingjiatong.re.service.sys.api.vo.BackendMenuListVO;
import cn.lingjiatong.re.service.sys.api.vo.BackendMenuTreeVO;
import cn.lingjiatong.re.service.sys.service.BackendMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 后台获取菜单树controller层
 *
 * @author Ling, Jiatong
 * Date: 3/26/23 10:22 PM
 */
@RestController
public class BackendMenuController implements BackendMenuFeignClient {

    @Autowired
    private BackendMenuService backendMenuService;

    // ********************************新增类接口********************************

    @Override
    @PostMapping("/backend/api/v1/menu/save")
    public ResultVO<?> saveMenu(@RequestBody BackendMenuSaveDTO dto, User currentUser) {
        backendMenuService.saveMenu(dto, currentUser);
        return ResultVO.success();
    }

    // ********************************删除类接口********************************
    // ********************************修改类接口********************************
    // ********************************查询类接口********************************


    @Override
    @GetMapping("/backend/api/v1/menu/list")
    public ResultVO<List<BackendMenuListVO>> findMenuList(BackendMenuListDTO dto, User currentUser) {
        return ResultVO.success(backendMenuService.findMenuList(dto, currentUser));
    }

    @Override
    @GetMapping("/backend/api/v1/menu/tree")
    public ResultVO<List<BackendMenuTreeVO>> findBackendMenuTree(User currentUser) {
        return ResultVO.success(backendMenuService.findBackendMenuTree(currentUser));
    }

}
