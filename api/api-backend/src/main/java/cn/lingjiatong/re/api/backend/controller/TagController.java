package cn.lingjiatong.re.api.backend.controller;

import cn.lingjiatong.re.common.ResultVO;
import cn.lingjiatong.re.common.annotation.CurrentUser;
import cn.lingjiatong.re.common.entity.User;
import cn.lingjiatong.re.service.article.api.client.BackendTagFeignClient;
import cn.lingjiatong.re.service.article.api.vo.BackendTagListVO;
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
 * 后端管理系统Tag模块接口
 *
 * @author Ling, Jiatong
 * Date: 2023/1/4 17:16
 */
@Slf4j
@RestController
@RequestMapping("/tag")
@Api(tags = "后端管理系统Tag模块接口")
public class TagController {

    @Autowired
    private BackendTagFeignClient backendTagFeignClient;

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
    @ApiOperation(value = "后端获取文章标签列表", httpMethod = "GET")
    @PreAuthorize("hasAuthority('blog:article') || hasAuthority('blog:article:read')")
    public ResultVO<List<BackendTagListVO>> findTagList(@CurrentUser User currentUser) {
        log.info("==========后端获取文章标签列表");
        return backendTagFeignClient.findTagList(currentUser);
    }

}
