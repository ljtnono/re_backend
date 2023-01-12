package cn.lingjiatong.re.service.article.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 后端批量更新文章推荐状态DTO对象
 *
 * @author Ling, Jiatong
 * Date: 2023/1/12 15:08
 */
@Data
@Schema(name = "BackendArticleUpdateRecommendBatchDTO", description = "后端批量更新文章推荐状态DTO对象")
public class BackendArticleUpdateRecommendBatchDTO {

    /**
     * 文章id列表
     */
    @Schema(description = "文章id列表")
    private List<Long> articleIdList;

    /**
     * 是否推荐 0 不推荐 1 推荐
     */
    @Schema(description = "是否推荐 0 不推荐 1 推荐")
    private Byte recommend;
}
