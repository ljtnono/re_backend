package cn.lingjiatong.re.api.backend.controller;

import cn.lingjiatong.re.common.ResultVO;
import cn.lingjiatong.re.common.entity.User;
import cn.lingjiatong.re.service.article.api.client.BackendArticleFeignClient;
import cn.lingjiatong.re.service.article.api.dto.BackendDraftSaveOrUpdateDTO;
import cn.lingjiatong.re.service.article.api.vo.BackendDraftDetailVO;
import cn.lingjiatong.re.service.article.api.vo.BackendDraftListVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
public class ArticleController {

    @Autowired
    private BackendArticleFeignClient backendArticleFeignClient;

    // ********************************新增类接口********************************
    // ********************************删除类接口********************************
    // ********************************修改类接口********************************
    // ********************************查询类接口********************************

    /**
     * 后端获取草稿详情
     *
     * @param currentUser 当前用户
     * @param draftId 草稿id
     * @return 文章草稿详情VO对象
     */
    @GetMapping("/draft/{draftId}")
    public ResultVO<BackendDraftDetailVO> getDraftDetail(@PathVariable("draftId") String draftId, @RequestParam(value = "currentUser", required = false) User currentUser) {
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
    public ResultVO<List<BackendDraftListVO>> getDraftList(@RequestParam(required = false) User currentUser) {
        log.info("==========获取当前用户的草稿列表");
        return backendArticleFeignClient.getDraftList(currentUser);
    }

    /**
     * 保存或更新草稿
     *
     * @param dto 草稿保存或更新DTO对象
     * @param currentUser 当前用户
     * @return 通用消息返回对象
     */
    @PostMapping("/saveOrUpdateDraft")
    public ResultVO<?> saveOrUpdateDraft(@RequestBody BackendDraftSaveOrUpdateDTO dto, @RequestParam(required = false) User currentUser) {
        log.info("==========保存或更新草稿，参数：{}，{}", dto.getDraftId(), dto.getTitle());
        return backendArticleFeignClient.saveOrUpdateDraft(dto, currentUser);
    }

    /**
     * 删除草稿
     *
     * @param currentUser 当前用户
     * @param draftId 草稿id
     * @return 通用消息返回对象
     */
    @DeleteMapping("/deleteDraft/{draftId}")
    public ResultVO<?> deleteDraft(@PathVariable("draftId") String draftId, @RequestParam(required = false) User currentUser) {
        log.info("==========删除草稿，参数：{}", draftId);
        return backendArticleFeignClient.deleteDraft(draftId, currentUser);
    }
}
