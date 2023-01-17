package cn.lingjiatong.re.service.article.api.dto;

import cn.lingjiatong.re.common.BasePageDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 前端分页获取文章置顶列表DTO对象
 *
 * @author Ling, Jiatong
 * Date: 2023/1/17 15:02
 */
@Data
@Schema(name = "FrontendArticleTopListDTO", description = "前端分页获取文章置顶列表DTO对象")
public class FrontendArticleTopListDTO extends BasePageDTO {

    /**
     * 排序字段限定列表
     */
    @Schema(description = "排序字段限定列表", hidden = true)
    protected List<String> orderFieldLimitList = List.of("create_time");
}
