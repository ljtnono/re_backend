package cn.lingjiatong.re.api.backend.controller;

import cn.lingjiatong.re.common.ResultVO;
import cn.lingjiatong.re.common.annotation.CurrentUser;
import cn.lingjiatong.re.common.entity.User;
import cn.lingjiatong.re.service.article.api.client.BackendCategoryFeignClient;
import cn.lingjiatong.re.service.article.api.vo.BackendCategoryListVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
@Api(tags = "后端管理系统Category模块接口")
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
    @ApiOperation(value = "后端获取文章分类列表", httpMethod = "GET")
    @PreAuthorize("hasAuthority('blog:article') || hasAuthority('blog:article:read')")
    public ResultVO<List<BackendCategoryListVO>> findCategoryList(@CurrentUser User currentUser) {
        log.info("==========后端获取文章分类列表");
        return backendCategoryFeignClient.findCategoryList(currentUser);
    }
}
