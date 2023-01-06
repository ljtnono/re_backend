package cn.lingjiatong.re.service.article.api.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 后端获取文章分类列表VO对象
 *
 * @author Ling, Jiatong
 * Date: 2023/1/4 16:18
 */
@Data
@Schema(name = "BackendCategoryListVO", description = "后端获取文章分类列表VO对象")
public class BackendCategoryListVO {

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
