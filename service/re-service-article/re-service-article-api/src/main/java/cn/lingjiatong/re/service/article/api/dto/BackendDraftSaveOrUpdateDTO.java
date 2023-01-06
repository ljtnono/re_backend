package cn.lingjiatong.re.service.article.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 草稿保存或更新DTO对象
 *
 * @author Ling, Jiatong
 * Date: 2022/12/27 15:44
 */
@Data
@Schema(name = "BackendDraftSaveOrUpdateDTO", description = "草稿保存或更新DTO对象")
public class BackendDraftSaveOrUpdateDTO {

    /**
     * 草稿id
     */
    @Schema(description = "草稿id")
    private String draftId;

    /**
     * 草稿标题
     */
    @Schema(description = "标题")
    private String title;

    /**
     * 草稿markdown内容
     */
    @Schema(description = "草稿markdown内容")
    private String markdownContent;
}
