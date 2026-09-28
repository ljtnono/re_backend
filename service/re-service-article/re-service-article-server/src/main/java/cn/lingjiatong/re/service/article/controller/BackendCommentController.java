package cn.lingjiatong.re.service.article.controller;

import cn.lingjiatong.re.common.ResultVO;
import cn.lingjiatong.re.service.article.api.client.BackendCommentFeignClient;
import cn.lingjiatong.re.service.article.api.dto.BackendCommentDeleteBatchDTO;
import cn.lingjiatong.re.service.article.api.dto.BackendCommentPageListDTO;
import cn.lingjiatong.re.service.article.api.dto.BackendCommentStatusUpdateDTO;
import cn.lingjiatong.re.service.article.api.vo.BackendCommentListVO;
import cn.lingjiatong.re.service.article.service.BackendCommentService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 后台评论模块controller层
 *
 * @author Ling, Jiatong
 */
@RestController
public class BackendCommentController implements BackendCommentFeignClient {

    @Autowired
    private BackendCommentService backendCommentService;

    // ********************************新增类接口********************************
    // ********************************删除类接口********************************

    @Override
    @DeleteMapping("/backend/api/v1/comment/deleteBatch")
    public ResultVO<?> deleteCommentBatch(BackendCommentDeleteBatchDTO dto) {
        backendCommentService.deleteCommentBatch(dto);
        return ResultVO.success();
    }

    // ********************************修改类接口********************************

    @Override
    @PutMapping("/backend/api/v1/comment/status")
    public ResultVO<?> updateCommentStatus(BackendCommentStatusUpdateDTO dto) {
        backendCommentService.updateCommentStatus(dto);
        return ResultVO.success();
    }

    // ********************************查询类接口********************************

    @Override
    @GetMapping("/backend/api/v1/comment/pageList")
    public ResultVO<Page<BackendCommentListVO>> findCommentPageList(@RequestParam("pageNum") long pageNum,
                                                                    @RequestParam("pageSize") long pageSize,
                                                                    @RequestParam(value = "searchCondition", required = false) String searchCondition) {
        BackendCommentPageListDTO dto = new BackendCommentPageListDTO();
        dto.setPageNum(pageNum);
        dto.setPageSize(pageSize);
        dto.setSearchCondition(searchCondition);
        return ResultVO.success(backendCommentService.findCommentPageList(dto));
    }

    // ********************************私有函数********************************
    // ********************************公用函数********************************

}
