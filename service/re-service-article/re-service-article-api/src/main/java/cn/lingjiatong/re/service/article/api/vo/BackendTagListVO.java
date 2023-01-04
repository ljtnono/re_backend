package cn.lingjiatong.re.service.article.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 后端获取文章标签列表VO对象
 *
 * @author Ling, Jiatong
 * Date: 2023/1/4 17:30
 */
@Data
@ApiModel(description = "后端获取文章标签列表VO对象")
public class BackendTagListVO {

    /**
     * 标签id
     */
    @ApiModelProperty("标签id")
    private Long id;

    /**
     * 标签名称
     */
    @ApiModelProperty("标签名称")
    private String name;

}
