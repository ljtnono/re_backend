package cn.lingjiatong.re.service.article.api.client;

import cn.lingjiatong.re.common.ResultVO;
import cn.lingjiatong.re.common.config.FeignBasicAuthRequestInterceptor;
import cn.lingjiatong.re.common.entity.User;
import cn.lingjiatong.re.service.article.api.dto.BackendArticleListDTO;
import cn.lingjiatong.re.service.article.api.dto.BackendArticlePublishDTO;
import cn.lingjiatong.re.service.article.api.dto.BackendDraftSaveOrUpdateDTO;
import cn.lingjiatong.re.service.article.api.vo.BackendArticleListVO;
import cn.lingjiatong.re.service.article.api.vo.BackendDraftDetailVO;
import cn.lingjiatong.re.service.article.api.vo.BackendDraftListVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 后台文章模块feign客户端接口层
 *
 * @author Ling, Jiatong
 * Date: 2022/10/17 21:56
 */
@FeignClient(name = "re-service-article-server", path = "/article", contextId = "BackendArticleFeignClient", configuration = {FeignBasicAuthRequestInterceptor.class})
public interface BackendArticleFeignClient {

    // ********************************新增类接口********************************

    /**
     * 发布文章
     *
     * @param dto 后台文章发布接口DTO对象
     * @param currentUser 当前用户
     * @return 通用消息返回对象
     */
    @PostMapping("/backend/api/v1/article/publishArticle")
    ResultVO<?> publishArticle(@RequestBody BackendArticlePublishDTO dto, @SpringQueryMap User currentUser);

    // ********************************删除类接口********************************

    /**
     * 删除草稿
     *
     * @param currentUser 当前用户
     * @param draftId 草稿id
     * @return 通用消息返回对象
     */
    @DeleteMapping("/backend/api/v1/article/deleteDraft/{draftId}")
    ResultVO<?> deleteDraft(@PathVariable("draftId") String draftId, @SpringQueryMap User currentUser);

    // ********************************修改类接口********************************

    /**
     * 保存或者更新草稿
     *
     * @param dto 草稿保存或更新DTO对象
     * @param currentUser 当前用户
     * @return 通用消息返回对象
     */
    @PostMapping("/backend/api/v1/article/saveOrUpdateDraft")
    ResultVO<?> saveOrUpdateDraft(@RequestBody BackendDraftSaveOrUpdateDTO dto, @SpringQueryMap User currentUser);


    // ********************************查询类接口********************************

    /**
     * 后端获取草稿详情
     *
     * @param currentUser 当前用户
     * @param draftId 草稿id
     * @return 文章草稿详情VO对象
     */
    @GetMapping("/backend/api/v1/article/draft/{draftId}")
    ResultVO<BackendDraftDetailVO> getDraftDetail(@PathVariable("draftId") String draftId, @SpringQueryMap User currentUser);

    /**
     * 后端获取当前用户的草稿列表
     *
     * @param currentUser 当前用户
     * @return 文章草稿列表VO对象列表
     */
    @GetMapping("/backend/api/v1/article/draftList")
    ResultVO<List<BackendDraftListVO>> getDraftList(@SpringQueryMap User currentUser);

    /**
     * 分页获取文章列表
     *
     * @param dto 后端获取文章列表DTO对象
     * @param currentUser 当前用户
     * @return 后端获取文章列表VO对象分页对象
     */
    @GetMapping("/backend/api/v1/article/list")
    ResultVO<Page<BackendArticleListVO>> findArticleList(@SpringQueryMap BackendArticleListDTO dto, @SpringQueryMap User currentUser);

}
