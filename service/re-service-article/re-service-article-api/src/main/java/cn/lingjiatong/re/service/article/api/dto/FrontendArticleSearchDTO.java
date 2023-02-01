package cn.lingjiatong.re.service.article.api.dto;

import cn.lingjiatong.re.common.BasePageDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 前端文章搜索DTO对象
 *
 * @author Ling, Jiatong
 * Date: 2023/2/1 09:57
 */
@Data
@Schema(name = "FrontendArticleSearchDTO", description = "前端文章搜索DTO对象")
public class FrontendArticleSearchDTO extends BasePageDTO {

    /**
     * 搜索关键词
     */
    @Schema(description = "搜索关键词")
    private String searchCondition;

    // es数据查询，没有
}
