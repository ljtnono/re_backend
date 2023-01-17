package cn.lingjiatong.re.service.article.api.dto;

import cn.lingjiatong.re.common.BasePageDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 前端文章无限滚动列表DTO对象
 *
 * @author Ling, Jiatong
 * Date: 2023/1/17 11:11
 */
@Data
@Schema(name = "FrontendArticleScrollDTO", description = "前端文章无限滚动列表DTO对象")
public class FrontendArticleScrollDTO extends BasePageDTO {


    /**
     * 排序字段限定列表
     */
    @Schema(description = "排序字段限定列表", hidden = true)
    private List<String> orderFieldLimitList = List.of("modify_time", "view");
}
