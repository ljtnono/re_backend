package cn.lingjiatong.re.service.article.api.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 前端分页获取文章置顶列表VO对象
 *
 * @author Ling, Jiatong
 * Date: 2023/1/17 15:02
 */
@Data
@Schema(name = "FrontendArticleTopListVO", description = "前端分页获取文章置顶列表VO对象")
public class FrontendArticleTopListVO {

    /**
     * id
     */
    @Schema(description = "id")
    private String id;

    /**
     * 标题
     */
    @Schema(description = "标题")
    private String title;

    /**
     * 浏览量
     */
    @Schema(description = "浏览量")
    private Long view;

    /**
     * 喜欢数
     */
    @Schema(description = "喜欢数")
    private Long favorite;

}
