package cn.lingjiatong.re.service.article.api.dto;

import cn.lingjiatong.re.common.BasePageDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 前端推荐文章列表DTO对象
 *
 * @author Ling, Jiatong
 * Date: 2023/1/18 01:25
 */
@Data
@Schema(name = "FrontendArticleRecommendListDTO", description = "前端推荐文章列表DTO对象")
public class FrontendArticleRecommendListDTO extends BasePageDTO {

    /**
     * 排序字段限定列表
     */
    @Schema(description = "排序字段限定列表", hidden = true)
    private List<String> orderFieldLimitList = List.of("modify_time", "favorite");
}
