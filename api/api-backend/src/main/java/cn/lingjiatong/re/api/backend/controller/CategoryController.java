package cn.lingjiatong.re.api.backend.controller;

import cn.lingjiatong.re.common.ResultVO;
import cn.lingjiatong.re.common.annotation.CurrentUser;
import cn.lingjiatong.re.common.entity.User;
import cn.lingjiatong.re.service.article.api.client.BackendCategoryFeignClient;
import cn.lingjiatong.re.service.article.api.vo.BackendCategoryListVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 后端管理系统Category模块接口
 *
 * @author Ling, Jiatong
 * Date: 2023/1/4 16:55
 */
@Slf4j
@RestController
@RequestMapping("/category")
@Tag(name = "后端管理系统Category模块接口")
public class CategoryController {

    @Autowired
    private BackendCategoryFeignClient backendCategoryFeignClient;

    // ********************************新增类接口********************************
    // ********************************删除类接口********************************
    // ********************************修改类接口********************************
    // ********************************查询类接口********************************

    /**
     * 后端获取文章分类列表
     *
     * @param currentUser 当前登录用户
     * @return 后端获取文章分类列表VO对象列表
     */
    @GetMapping("/list")
    @Operation(summary = "后端获取文章分类列表", method = "GET")
    @PreAuthorize("isAuthenticated()")
    public ResultVO<List<BackendCategoryListVO>> findCategoryList(@Parameter(hidden = true) @CurrentUser User currentUser) {
        log.info("==========后端获取文章分类列表");
        return backendCategoryFeignClient.findCategoryList(currentUser);
    }
}
