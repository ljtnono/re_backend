package cn.lingjiatong.re.service.article.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 后台更新文章分类DTO对象
 *
 * @author Ling, Jiatong
 */
@Data
@Schema(name = "BackendCategoryUpdateDTO", description = "后台更新文章分类DTO对象")
public class BackendCategoryUpdateDTO {

    /**
     * 分类id
     */
    @Schema(description = "分类id")
    private Long id;

    /**
     * 分类名称
     */
    @Schema(description = "分类名称")
    private String name;
}
