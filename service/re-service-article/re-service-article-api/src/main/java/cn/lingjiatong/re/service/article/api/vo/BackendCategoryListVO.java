package cn.lingjiatong.re.service.article.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 后端获取文章分类列表VO对象
 *
 * @author Ling, Jiatong
 * Date: 2023/1/4 16:18
 */
@Data
@ApiModel(description = "后端获取文章分类列表VO对象")
public class BackendCategoryListVO {

    /**
     * 分类id
     */
    @ApiModelProperty("分类id")
    private Long id;

    /**
     * 分类名称
     */
    @ApiModelProperty("分类名称")
    private String name;

}
