package cn.lingjiatong.re.service.article.mapper;

import cn.lingjiatong.re.service.article.api.vo.BackendCommentListVO;
import cn.lingjiatong.re.service.article.entity.AtkComment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 评论mapper层
 *
 * @author Ling, Jiatong
 */
@Mapper
public interface AtkCommentMapper extends BaseMapper<AtkComment> {

    /**
     * 后台分页获取评论列表
     *
     * @param page            分页对象
     * @param searchCondition 搜索条件
     * @return 后台获取评论列表VO对象分页对象
     */
    Page<BackendCommentListVO> findCommentPageList(Page<BackendCommentListVO> page, @Param("searchCondition") String searchCondition);
}
