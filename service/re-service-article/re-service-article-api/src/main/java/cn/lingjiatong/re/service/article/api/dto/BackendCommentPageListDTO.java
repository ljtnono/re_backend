package cn.lingjiatong.re.service.article.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 后台分页获取评论列表DTO对象
 *
 * @author Ling, Jiatong
 */
@Data
@Schema(name = "BackendCommentPageListDTO", description = "后台分页获取评论列表DTO对象")
public class BackendCommentPageListDTO {

    /**
     * 当前页数
     */
    @Schema(description = "当前页数")
    private long pageNum = 1;

    /**
     * 每页条数
     */
    @Schema(description = "每页条数")
    private long pageSize = 10;

    /**
     * 搜索条件（匹配评论内容）
     */
    @Schema(description = "搜索条件（匹配评论内容）")
    private String searchCondition;
}
