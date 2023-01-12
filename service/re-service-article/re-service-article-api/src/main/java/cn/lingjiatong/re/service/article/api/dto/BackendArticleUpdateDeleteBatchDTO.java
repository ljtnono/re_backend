package cn.lingjiatong.re.service.article.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 后端批量更新文章删除状态DTO对象
 *
 * @author Ling, Jiatong
 * Date: 2023/1/12 18:41
 */
@Data
@Schema(name = "BackendArticleUpdateDeleteBatchDTO", description = "后端批量更新文章删除状态DTO对象")
public class BackendArticleUpdateDeleteBatchDTO {

    /**
     * 文章id列表
     */
    @Schema(description = "文章id列表")
    private List<Long> articleIdList;

    /**
     * 是否删除 0 正常 1 删除
     */
    @Schema(description = "是否删除 0 正常 1 删除")
    private Byte delete;

}
