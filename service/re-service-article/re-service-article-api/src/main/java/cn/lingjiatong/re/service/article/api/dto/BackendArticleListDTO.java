package cn.lingjiatong.re.service.article.api.dto;

import cn.lingjiatong.re.common.BasePageDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 后端获取文章列表DTO对象
 *
 * @author Ling, Jiatong
 * Date: 2023/1/9 19:44
 */
@Data
@Schema(name = "BackendArticleListDTO", description = "后端获取文章列表DTO对象")
public class BackendArticleListDTO extends BasePageDTO {


    /**
     * 搜索条件，根据标题模糊搜索
     */
    @Schema(description = "搜索条件，根据标题模糊搜索")
    private String searchCondition;

    /**
     * 分类筛选条件
     */
    @Schema(description = "分类筛选条件")
    private String category;

    /**
     * 推荐筛选条件
     */
    @Schema(description = "推荐筛选条件")
    private Byte recommend;

    /**
     * 置顶筛选条件
     */
    @Schema(description = "置顶筛选条件")
    private Byte top;

    /**
     * 排序字段限定列表
     */
    @Schema(description = "排序字段限定列表", hidden = true)
    protected List<String> orderFieldLimitList = List.of("view", "favorite");

}
