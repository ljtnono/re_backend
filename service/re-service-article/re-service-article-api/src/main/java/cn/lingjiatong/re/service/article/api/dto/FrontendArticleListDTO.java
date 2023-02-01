package cn.lingjiatong.re.service.article.api.dto;

import cn.lingjiatong.re.common.BasePageDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 前端分页获取文章列表DTO对象
 *
 * @author Ling, Jiatong
 * Date: 2023/2/1 14:51
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name = "FrontendArticleListDTO", description = "前端分页获取文章列表DTO对象")
public class FrontendArticleListDTO extends BasePageDTO {

    /**
     * 文章分类id
     */
    @Schema(description = "文章分类id")
    private Long categoryId;

    /**
     * 标签id
     */
    @Schema(description = "标签id")
    private Long tagId;

    /**
     * 排序字段限定列表，由子类覆盖
     */
    @Schema(description = "排序字段限定列表", hidden = true)
    protected List<String> orderFieldLimitList = List.of("modify_time");
}
