package cn.lingjiatong.re.service.article.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Set;

/**
 * 后台批量删除文章分类DTO对象
 *
 * @author Ling, Jiatong
 */
@Data
@Schema(name = "BackendCategoryDeleteBatchDTO", description = "后台批量删除文章分类DTO对象")
public class BackendCategoryDeleteBatchDTO {

    /**
     * 分类id集合
     */
    @Schema(description = "分类id集合")
    private Set<Long> categoryIdSet;
}
