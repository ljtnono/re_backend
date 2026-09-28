package cn.lingjiatong.re.service.article.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Set;

/**
 * 后台批量删除评论DTO对象
 *
 * @author Ling, Jiatong
 */
@Data
@Schema(name = "BackendCommentDeleteBatchDTO", description = "后台批量删除评论DTO对象")
public class BackendCommentDeleteBatchDTO {

    /**
     * 评论id集合
     */
    @Schema(description = "评论id集合")
    private Set<Long> commentIdSet;
}
