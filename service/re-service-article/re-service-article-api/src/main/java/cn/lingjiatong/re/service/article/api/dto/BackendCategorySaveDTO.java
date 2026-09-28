package cn.lingjiatong.re.service.article.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 后台新增文章分类DTO对象
 *
 * @author Ling, Jiatong
 */
@Data
@Schema(name = "BackendCategorySaveDTO", description = "后台新增文章分类DTO对象")
public class BackendCategorySaveDTO {

    /**
     * 分类名称
     */
    @Schema(description = "分类名称")
    private String name;
}
