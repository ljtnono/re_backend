package cn.lingjiatong.re.service.article.controller;

import cn.lingjiatong.re.common.ResultVO;
import cn.lingjiatong.re.common.entity.User;
import cn.lingjiatong.re.common.util.SaUserUtils;
import cn.lingjiatong.re.service.article.api.client.BackendArticleFeignClient;
import cn.lingjiatong.re.service.article.api.dto.*;
import cn.lingjiatong.re.service.article.api.vo.BackendArticleListVO;
import cn.lingjiatong.re.service.article.api.vo.BackendDraftDetailVO;
import cn.lingjiatong.re.service.article.api.vo.BackendDraftListVO;
import cn.lingjiatong.re.service.article.service.BackendArticleService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 后台文章模块controller层
 *
 * @author Ling, Jiatong
 * Date: 2022/10/17 21:56
 */
@RestController
public class BackendArticleController implements BackendArticleFeignClient {

    @Autowired
    private BackendArticleService backendArticleService;

    // ********************************新增类接口********************************

    @Override
    @PostMapping("/backend/api/v1/article/publishArticle")
    public ResultVO<?> publishArticle(@RequestBody BackendArticlePublishDTO dto) {
        User currentUser = SaUserUtils.getCurrentUser();
        backendArticleService.publishArticle(dto, currentUser);
        return ResultVO.success();
    }

    // ********************************删除类接口********************************

    @Override
    @DeleteMapping("/backend/api/v1/article/deleteDraft/{draftId}")
    public ResultVO<?> deleteDraft(@PathVariable("draftId") String draftId) {
        User currentUser = SaUserUtils.getCurrentUser();
        backendArticleService.deleteDraft(draftId, currentUser);
        return ResultVO.success();
    }

    @Override
    @DeleteMapping("/backend/api/v1/article/deleteBatch")
    public ResultVO<?> deleteArticleBatch(@RequestBody BackendArticleDeleteBatchDTO dto) {
        User currentUser = SaUserUtils.getCurrentUser();
        backendArticleService.deleteArticleBatch(dto, currentUser);
        return ResultVO.success();
    }

    // ********************************修改类接口********************************

    @Override
    @PostMapping("/backend/api/v1/article/saveOrUpdateDraft")
    public ResultVO<?> saveOrUpdateDraft(@RequestBody BackendDraftSaveOrUpdateDTO dto) {
        User currentUser = SaUserUtils.getCurrentUser();
        backendArticleService.saveOrUpdateDraft(dto, currentUser);
        return ResultVO.success();
    }

    @Override
    @PutMapping("/backend/api/v1/article/updateRecommendBatch")
    public ResultVO<?> updateArticleRecommendBatch(@RequestBody BackendArticleUpdateRecommendBatchDTO dto) {
        User currentUser = SaUserUtils.getCurrentUser();
        backendArticleService.updateArticleRecommendBatch(dto, currentUser);
        return ResultVO.success();
    }

    @Override
    @PutMapping("/backend/api/v1/article/updateTopBatch")
    public ResultVO<?> updateArticleTopBatch(@RequestBody BackendArticleUpdateTopBatchDTO dto) {
        User currentUser = SaUserUtils.getCurrentUser();
        backendArticleService.updateArticleTopBatch(dto, currentUser);
        return ResultVO.success();
    }

    @Override
    @PutMapping("/backend/api/v1/article/updateDeleteBatch")
    public ResultVO<?> updateArticleDeleteBatch(BackendArticleUpdateDeleteBatchDTO dto) {
        User currentUser = SaUserUtils.getCurrentUser();
        backendArticleService.updateArticleDeleteBatch(dto, currentUser);
        return ResultVO.success();
    }

    // ********************************查询类接口********************************

    @Override
    @GetMapping("/backend/api/v1/article/draft/{draftId}")
    public ResultVO<BackendDraftDetailVO> getDraftDetail(@PathVariable("draftId") String draftId) {
        User currentUser = SaUserUtils.getCurrentUser();
        return ResultVO.success(backendArticleService.getDraftDetail(draftId, currentUser));
    }

    @Override
    @GetMapping("/backend/api/v1/article/draftList")
    public ResultVO<List<BackendDraftListVO>> getDraftList() {
        User currentUser = SaUserUtils.getCurrentUser();
        return ResultVO.success(backendArticleService.getDraftList(currentUser));
    }


    @Override
    @GetMapping("/backend/api/v1/article/list")
    public ResultVO<Page<BackendArticleListVO>> findArticleList(BackendArticleListDTO dto) {
        return ResultVO.success(backendArticleService.findArticleList(dto));
    }

}
