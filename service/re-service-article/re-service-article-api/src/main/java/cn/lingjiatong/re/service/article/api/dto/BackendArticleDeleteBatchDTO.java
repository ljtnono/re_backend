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
@Schema(name = "BackendArticlePhysicDeleteBatchDTO", description = "后端批量删除文章DTO对象")
public class BackendArticleDeleteBatchDTO {

    /**
     * 文章id列表
     */
    @Schema(description = "文章id列表")
    private List<Long> articleIdList;
}
