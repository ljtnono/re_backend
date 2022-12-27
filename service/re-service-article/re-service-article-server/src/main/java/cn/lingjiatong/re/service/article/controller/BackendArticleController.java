package cn.lingjiatong.re.service.article.controller;

import cn.lingjiatong.re.common.ResultVO;
import cn.lingjiatong.re.common.entity.User;
import cn.lingjiatong.re.service.article.api.client.BackendArticleFeignClient;
import cn.lingjiatong.re.service.article.api.dto.BackendDraftSaveOrUpdateDTO;
import cn.lingjiatong.re.service.article.api.vo.BackendDraftListVO;
import cn.lingjiatong.re.service.article.service.BackendArticleService;
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

//    /**
//     * 后端保存文章接口
//     *
//     * @param dto 后台保存文章接口DTO对象
//     */
//    @PostMapping("/api/v1/saveArticle")
//    public ResultVO<?> saveArticle(@RequestBody BackendArticleSaveDTO dto) {
//        backendArticleService.saveArticle(dto);
//        return ResultVO.success();
//    }
//
//    /**
//     * 后端保存文章封面图接口
//     *
//     * @param title 文章标题
//     * @param multipartFile 图片文件
//     */
//    @PostMapping("/api/v1/saveArticleCoverImage")
//    public ResultVO<String> saveArticleCoverImage(String title, @RequestParam("file") MultipartFile multipartFile) {
//        return ResultVO.success(backendArticleService.saveArticleCoverImage(title, multipartFile));
//    }

    @Override
    @GetMapping("/backend/api/v1/article/draftList")
    public ResultVO<List<BackendDraftListVO>> getDraftList(@RequestParam(value = "currentUser", required = false) User currentUser) {
        return ResultVO.success(backendArticleService.getDraftList(currentUser));
    }

    @Override
    @PostMapping("/backend/api/v1/article/saveOrUpdateDraft")
    public ResultVO<?> saveOrUpdateDraft(@RequestBody BackendDraftSaveOrUpdateDTO dto, @RequestParam(value = "currentUser", required = false) User currentUser) {
        backendArticleService.saveOrUpdateDraft(dto, currentUser);
        return ResultVO.success();
    }

    @Override
    @DeleteMapping("/backend/api/v1/article/deleteDraft/{draftId}")
    public ResultVO<?> deleteDraft(@PathVariable("draftId") String draftId, @RequestParam(value = "currentUser", required = false) User currentUser) {
        backendArticleService.deleteDraft(draftId, currentUser);
        return ResultVO.success();
    }
}
