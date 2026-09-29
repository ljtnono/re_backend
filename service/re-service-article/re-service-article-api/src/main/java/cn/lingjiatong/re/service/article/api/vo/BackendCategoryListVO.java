package cn.lingjiatong.re.service.article.api.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

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

    /**
     * 分类总浏览量
     */
    @Schema(description = "分类总浏览量")
    private Long view;

    /**
     * 分类总喜欢数
     */
    @Schema(description = "分类总喜欢数")
    private Long favorite;

    /**
     * 分类下文章数量
     */
    @Schema(description = "分类下文章数量")
    private Long articleCount;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    /**
     * 最后修改时间
     */
    @Schema(description = "最后修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime modifyTime;

}
