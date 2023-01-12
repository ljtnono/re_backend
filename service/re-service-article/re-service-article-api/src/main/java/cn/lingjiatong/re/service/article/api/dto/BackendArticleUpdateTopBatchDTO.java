package cn.lingjiatong.re.service.article.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 后端批量更新文章置顶状态DTO对象
 *
 * @author Ling, Jiatong
 * Date: 2023/1/12 15:08
 */
@Data
@Schema(name = "BackendArticleUpdateTopBatchDTO", description = "后端批量更新文章置顶状态DTO对象")
public class BackendArticleUpdateTopBatchDTO {

    /**
     * 文章id列表
     */
    @Schema(description = "文章id列表")
    private List<Long> articleIdList;

    /**
     * 是否置顶 0 不置顶 1 置顶
     */
    @Schema(description = "是否置顶 0 不置顶 1 置顶")
    private Byte top;
}
