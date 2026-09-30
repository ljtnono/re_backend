package cn.lingjiatong.re.service.article.service;

import cn.lingjiatong.re.service.article.api.dto.BackendCommentDeleteBatchDTO;
import cn.lingjiatong.re.service.article.api.dto.BackendCommentPageListDTO;
import cn.lingjiatong.re.service.article.api.dto.BackendCommentStatusUpdateDTO;
import cn.lingjiatong.re.service.article.api.vo.BackendCommentListVO;
import cn.lingjiatong.re.service.article.entity.ArtalkComment;
import cn.lingjiatong.re.service.article.mapper.ArtalkCommentMapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * 后台评论模块service层
 *
 * @author Ling, Jiatong
 */
@Slf4j
@Service
public class BackendCommentService {

    @Resource
    private ArtalkCommentMapper atkCommentMapper;

    // ********************************新增类接口********************************
    // ********************************删除类接口********************************

    /**
     * 后台批量删除评论（软删除）
     *
     * @param dto 后台批量删除评论DTO对象
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteCommentBatch(BackendCommentDeleteBatchDTO dto) {
        Set<Long> commentIdSet = dto.getCommentIdSet();
        if (CollectionUtils.isEmpty(commentIdSet)) {
            return;
        }
        atkCommentMapper.update(null, new LambdaUpdateWrapper<ArtalkComment>()
                .set(ArtalkComment::getDeletedAt, LocalDateTime.now())
                .set(ArtalkComment::getUpdatedAt, LocalDateTime.now())
                .in(ArtalkComment::getId, commentIdSet));
    }

    // ********************************修改类接口********************************

    /**
     * 后台更新评论状态（审核/置顶/折叠）
     *
     * @param dto 后台更新评论状态DTO对象
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateCommentStatus(BackendCommentStatusUpdateDTO dto) {
        Set<Long> commentIdSet = dto.getCommentIdSet();
        if (CollectionUtils.isEmpty(commentIdSet)) {
            return;
        }
        LambdaUpdateWrapper<ArtalkComment> wrapper = new LambdaUpdateWrapper<ArtalkComment>()
                .set(ArtalkComment::getUpdatedAt, LocalDateTime.now())
                .in(ArtalkComment::getId, commentIdSet);
        if (dto.getPending() != null) {
            wrapper.set(ArtalkComment::getPending, dto.getPending());
        }
        if (dto.getPinned() != null) {
            wrapper.set(ArtalkComment::getPinned, dto.getPinned());
        }
        if (dto.getCollapsed() != null) {
            wrapper.set(ArtalkComment::getCollapsed, dto.getCollapsed());
        }
        atkCommentMapper.update(null, wrapper);
    }

    // ********************************查询类接口********************************

    /**
     * 后台分页获取评论列表
     *
     * @param dto 后台分页获取评论列表DTO对象
     * @return 后台获取评论列表VO对象分页对象
     */
    public Page<BackendCommentListVO> findCommentPageList(BackendCommentPageListDTO dto) {
        return atkCommentMapper.findCommentPageList(new Page<>(dto.getPageNum(), dto.getPageSize()), dto.getSearchCondition());
    }

    // ********************************私有函数********************************
    // ********************************公共函数********************************

}
