package cn.lingjiatong.re.service.article.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 后端批量删除文章DTO对象
 *
 * @author Ling, Jiatong
 * Date: 2023/1/12 15:25
 */
@Data
@Schema(name = "BackendArticleDeleteBatchDTO", description = "后端批量删除文章DTO对象")
public class BackendArticleDeleteBatchDTO {

    /**
     * 文章id列表
     */
    @Schema(description = "文章id列表")
    private List<Long> articleIdList;

    /**
     * 是否物理删除 true 是 false 否
     */
    @Schema(description = "是否物理删除 true 是 false 否")
    private Boolean physics;

}
