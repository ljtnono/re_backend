package cn.lingjiatong.re.service.article.api.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 前端博客标签列表VO对象
 *
 * @author Ling, Jiatong
 * Date: 2022/10/16 12:15
 */
@Data
@Schema(name = "FrontendTagListVO", description = "前端博客标签列表VO对象")
public class FrontendTagListVO {

    /**
     * 标签id
     */
    @Schema(description = "id")
    private String id;

    /**
     * 标签名
     */
    @Schema(description = "标签名")
    private String name;
    
    /**
     * 标签的文章数量
     */
    @Schema(description = "标签的文章数量")
    private Long articleCount;
}
