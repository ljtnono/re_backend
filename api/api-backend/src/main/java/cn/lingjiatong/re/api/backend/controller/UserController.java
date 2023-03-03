package cn.lingjiatong.re.api.backend.controller;

import cn.lingjiatong.re.common.ResultVO;
import cn.lingjiatong.re.common.annotation.CurrentUser;
import cn.lingjiatong.re.common.entity.User;
import cn.lingjiatong.re.service.sys.api.client.BackendUserFeignClient;
import cn.lingjiatong.re.service.sys.api.dto.BackendUserListDTO;
import cn.lingjiatong.re.service.sys.api.vo.BackendUserListVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 后端管理系统User模块接口
 *
 * @author Ling, Jiatong
 * Date: 2023/2/26 17:03
 */
@Slf4j
@RestController
@RequestMapping("/user")
@Tag(name = "后端管理系统User模块接口")
public class UserController {

    @Autowired
    private BackendUserFeignClient backendUserFeignClient;

    // ********************************新增类接口********************************
    // ********************************删除类接口********************************
    // ********************************修改类接口********************************
    // ********************************查询类接口********************************

    /**
     * 后端获取文章标签列表
     *
     * @param currentUser 当前登录用户
     * @return 后端获取文章标签列表VO对象列表
     */
    @GetMapping("/list")
    @Operation(summary = "后端获取用户列表", method = "GET")
    @PreAuthorize("hasAuthority('system:user') || hasAuthority('system:user:read')")
    public ResultVO<Page<BackendUserListVO>> findTagList(BackendUserListDTO dto, @Parameter(hidden = true) @CurrentUser User currentUser) {
        log.info("==========后端获取文章标签列表");
        return backendUserFeignClient.findUserList(dto, currentUser);
    }

    // ********************************私有函数********************************
    // ********************************公用函数********************************
}
