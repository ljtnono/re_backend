package cn.lingjiatong.re.service.article.api.client;

import cn.lingjiatong.re.common.ResultVO;
import cn.lingjiatong.re.common.config.FeignBasicAuthRequestInterceptor;
import cn.lingjiatong.re.service.article.api.dto.BackendCommentDeleteBatchDTO;
import cn.lingjiatong.re.service.article.api.dto.BackendCommentPageListDTO;
import cn.lingjiatong.re.service.article.api.dto.BackendCommentStatusUpdateDTO;
import cn.lingjiatong.re.service.article.api.vo.BackendCommentListVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 后台评论模块feign客户端接口层
 *
 * @author Ling, Jiatong
 */
@FeignClient(name = "re-service-article-server", path = "/article", contextId = "BackendCommentFeignClient", configuration = {FeignBasicAuthRequestInterceptor.class})
public interface BackendCommentFeignClient {

    // ********************************新增类接口********************************
    // ********************************删除类接口********************************

    /**
     * 后端批量删除评论
     *
     * @param dto 后台批量删除评论DTO对象
     * @return 通用消息返回对象
     */
    @DeleteMapping("/backend/api/v1/comment/deleteBatch")
    ResultVO<?> deleteCommentBatch(@RequestBody BackendCommentDeleteBatchDTO dto);

    // ********************************修改类接口********************************

    /**
     * 后端更新评论状态
     *
     * @param dto 后台更新评论状态DTO对象
     * @return 通用消息返回对象
     */
    @PutMapping("/backend/api/v1/comment/status")
    ResultVO<?> updateCommentStatus(@RequestBody BackendCommentStatusUpdateDTO dto);

    // ********************************查询类接口********************************

    /**
     * 后端分页获取评论列表
     *
     * @param pageNum         当前页数
     * @param pageSize        每页条数
     * @param searchCondition 搜索条件
     * @return 后台获取评论列表VO对象分页对象
     */
    @GetMapping("/backend/api/v1/comment/pageList")
    ResultVO<Page<BackendCommentListVO>> findCommentPageList(@RequestParam("pageNum") long pageNum,
                                                             @RequestParam("pageSize") long pageSize,
                                                             @RequestParam(value = "searchCondition", required = false) String searchCondition);

}
