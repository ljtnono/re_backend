package cn.lingjiatong.re.api.backend.controller;

import cn.lingjiatong.re.common.ResultVO;
import cn.lingjiatong.re.common.annotation.CurrentUser;
import cn.lingjiatong.re.common.entity.User;
import cn.lingjiatong.re.service.article.api.client.BackendArticleFeignClient;
import cn.lingjiatong.re.service.article.api.dto.BackendDraftSaveOrUpdateDTO;
import cn.lingjiatong.re.service.article.api.vo.BackendDraftDetailVO;
import cn.lingjiatong.re.service.article.api.vo.BackendDraftListVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 后端管理系统Article模块controller层
 *
 * @author Ling, Jiatong
 * Date: 2022/12/27 16:27
 */
@Slf4j
@RestController
@RequestMapping("/article")
@Api(tags = "后端管理系统Article模块接口")
public class ArticleController {

    @Autowired
    private BackendArticleFeignClient backendArticleFeignClient;

    // ********************************新增类接口********************************
    // ********************************删除类接口********************************

    /**
     * 删除草稿
     *
     * @param currentUser 当前用户
     * @param draftId 草稿id
     * @return 通用消息返回对象
     */
    @DeleteMapping("/deleteDraft/{draftId}")
    @ApiOperation(value = "删除草稿", httpMethod = "DELETE")
    @PreAuthorize("hasAuthority('blog:article') && hasAuthority('blog:article:write')")
    public ResultVO<?> deleteDraft(@PathVariable("draftId") String draftId, @CurrentUser User currentUser) {
        log.info("==========删除草稿，参数：{}", draftId);
        return backendArticleFeignClient.deleteDraft(draftId, currentUser);
    }

    // ********************************修改类接口********************************

    /**
     * 保存或更新草稿
     *
     * @param dto 草稿保存或更新DTO对象
     * @param currentUser 当前用户
     * @return 通用消息返回对象
     */
    @PostMapping("/saveOrUpdateDraft")
    @ApiOperation(value = "保存或更新草稿", httpMethod = "POST")
    @PreAuthorize("hasAuthority('blog:article') && hasAuthority('blog:article:write')")
    public ResultVO<?> saveOrUpdateDraft(@RequestBody BackendDraftSaveOrUpdateDTO dto, @CurrentUser User currentUser) {
        log.info("==========保存或更新草稿，参数：{}，{}", dto.getDraftId(), dto.getTitle());
        return backendArticleFeignClient.saveOrUpdateDraft(dto, currentUser);
    }

    // ********************************查询类接口********************************

    /**
     * 后端获取草稿详情
     *
     * @param currentUser 当前用户
     * @param draftId 草稿id
     * @return 文章草稿详情VO对象
     */
    @GetMapping("/draft/{draftId}")
    @ApiOperation(value = "后端获取草稿详情", httpMethod = "GET")
    @PreAuthorize("hasAuthority('blog:article') || hasAuthority('blog:article:read')")
    public ResultVO<BackendDraftDetailVO> getDraftDetail(@PathVariable("draftId") String draftId, @CurrentUser User currentUser) {
        log.info("==========后端获取草稿详情，参数：{}", draftId);
        return backendArticleFeignClient.getDraftDetail(draftId, currentUser);
    }

    /**
     * 获取当前用户的草稿列表
     *
     * @param currentUser 当前用户
     * @return 文章草稿列表VO对象列表
     */
    @GetMapping("/draftList")
    @ApiOperation(value = "获取当前用户的草稿列表", httpMethod = "GET")
    @PreAuthorize("hasAuthority('blog:article') || hasAuthority('blog:article:read')")
    public ResultVO<List<BackendDraftListVO>> getDraftList(@CurrentUser User currentUser) {
        log.info("==========获取当前用户的草稿列表");
        return backendArticleFeignClient.getDraftList(currentUser);
    }
}
