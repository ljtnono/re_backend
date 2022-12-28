package cn.lingjiatong.re.service.article.api.client;

import cn.lingjiatong.re.common.ResultVO;
import cn.lingjiatong.re.common.entity.User;
import cn.lingjiatong.re.service.article.api.dto.BackendDraftSaveOrUpdateDTO;
import cn.lingjiatong.re.service.article.api.vo.BackendDraftDetailVO;
import cn.lingjiatong.re.service.article.api.vo.BackendDraftListVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 后台文章模块feign客户端接口层
 *
 * @author Ling, Jiatong
 * Date: 2022/10/17 21:56
 */
@FeignClient(name = "re-service-article-server", path = "/article", contextId = "BackendArticleFeignClient")
public interface BackendArticleFeignClient {

    /**
     * 后端获取草稿详情
     *
     * @param currentUser 当前用户
     * @param draftId 草稿id
     * @return 文章草稿详情VO对象
     */
    @GetMapping("/backend/api/v1/article/draft/{draftId}")
    ResultVO<BackendDraftDetailVO> getDraftDetail(@PathVariable("draftId") String draftId, @RequestParam(value = "currentUser", required = false) User currentUser);

    /**
     * 后端获取当前用户的草稿列表
     *
     * @param currentUser 当前用户
     * @return 文章草稿列表VO对象列表
     */
    @GetMapping("/backend/api/v1/article/draftList")
    ResultVO<List<BackendDraftListVO>> getDraftList(@RequestParam(value = "currentUser", required = false) User currentUser);

    /**
     * 保存或者更新草稿
     *
     * @param dto 草稿保存或更新DTO对象
     * @param currentUser 当前用户
     * @return 通用消息返回对象
     */
    @PostMapping("/backend/api/v1/article/saveOrUpdateDraft")
    ResultVO<?> saveOrUpdateDraft(@RequestBody BackendDraftSaveOrUpdateDTO dto, @RequestParam(value = "currentUser", required = false) User currentUser);

    /**
     * 删除草稿
     *
     * @param currentUser 当前用户
     * @param draftId 草稿id
     * @return 通用消息返回对象
     */
    @DeleteMapping("/backend/api/v1/article/deleteDraft/{draftId}")
    ResultVO<?> deleteDraft(@PathVariable("draftId") String draftId, @RequestParam(value = "currentUser", required = false) User currentUser);
}
