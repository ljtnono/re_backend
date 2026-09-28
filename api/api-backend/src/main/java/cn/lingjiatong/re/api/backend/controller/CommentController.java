package cn.lingjiatong.re.api.backend.controller;

import cn.lingjiatong.re.common.ResultVO;
import cn.lingjiatong.re.service.article.api.client.BackendCommentFeignClient;
import cn.lingjiatong.re.service.article.api.dto.BackendCommentDeleteBatchDTO;
import cn.lingjiatong.re.service.article.api.dto.BackendCommentStatusUpdateDTO;
import cn.lingjiatong.re.service.article.api.vo.BackendCommentListVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 后端管理系统Comment模块接口
 *
 * @author Ling, Jiatong
 */
@Slf4j
@RestController
@RequestMapping("/comment")
@Tag(name = "后端管理系统Comment模块接口")
public class CommentController {

    @Autowired
    private BackendCommentFeignClient backendCommentFeignClient;

    // ********************************新增类接口********************************
    // ********************************删除类接口********************************

    /**
     * 后端批量删除评论
     *
     * @param dto 后台批量删除评论DTO对象
     * @return 通用消息返回对象
     */
    @DeleteMapping("/deleteBatch")
    @Operation(summary = "后端批量删除评论", method = "DELETE")
    public ResultVO<?> deleteCommentBatch(@RequestBody BackendCommentDeleteBatchDTO dto) {
        log.info("==========后端批量删除评论，参数：{}", dto);
        return backendCommentFeignClient.deleteCommentBatch(dto);
    }

    // ********************************修改类接口********************************

    /**
     * 后端更新评论状态
     *
     * @param dto 后台更新评论状态DTO对象
     * @return 通用消息返回对象
     */
    @PutMapping("/status")
    @Operation(summary = "后端更新评论状态", method = "PUT")
    public ResultVO<?> updateCommentStatus(@RequestBody BackendCommentStatusUpdateDTO dto) {
        log.info("==========后端更新评论状态，参数：{}", dto);
        return backendCommentFeignClient.updateCommentStatus(dto);
    }

    // ********************************查询类接口********************************

    /**
     * 后端分页获取评论列表
     *
     * @param pageNum         当前页数
     * @param pageSize        每页条数
     * @param searchCondition 搜索条件
     * @return 后台获取评论列表VO对象分页对象
     */
    @GetMapping("/pageList")
    @Operation(summary = "后端分页获取评论列表", method = "GET")
    public ResultVO<Page<BackendCommentListVO>> findCommentPageList(@RequestParam("pageNum") long pageNum,
                                                                    @RequestParam("pageSize") long pageSize,
                                                                    @RequestParam(value = "searchCondition", required = false) String searchCondition) {
        log.info("==========后端分页获取评论列表，pageNum：{}，pageSize：{}，searchCondition：{}", pageNum, pageSize, searchCondition);
        return backendCommentFeignClient.findCommentPageList(pageNum, pageSize, searchCondition);
    }
}
