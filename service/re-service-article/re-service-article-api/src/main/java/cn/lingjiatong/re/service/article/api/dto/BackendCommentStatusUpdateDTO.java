package cn.lingjiatong.re.service.article.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Set;

/**
 * 后台更新评论状态DTO对象
 *
 * @author Ling, Jiatong
 */
@Data
@Schema(name = "BackendCommentStatusUpdateDTO", description = "后台更新评论状态DTO对象")
public class BackendCommentStatusUpdateDTO {

    /**
     * 评论id集合
     */
    @Schema(description = "评论id集合")
    private Set<Long> commentIdSet;

    /**
     * 是否待审核，null表示不修改
     */
    @Schema(description = "是否待审核")
    private Boolean pending;

    /**
     * 是否置顶，null表示不修改
     */
    @Schema(description = "是否置顶")
    private Boolean pinned;

    /**
     * 是否折叠，null表示不修改
     */
    @Schema(description = "是否折叠")
    private Boolean collapsed;
}
