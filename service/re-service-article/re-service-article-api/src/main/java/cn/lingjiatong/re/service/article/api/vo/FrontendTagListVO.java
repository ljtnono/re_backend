package cn.lingjiatong.re.service.article.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 前端博客标签列表VO对象
 *
 * @author Ling, Jiatong
 * Date: 2022/10/16 12:15
 */
@Data
@ApiModel(description = "前端博客标签列表VO对象")
public class FrontendTagListVO {

    /**
     * 标签id
     */
    @ApiModelProperty("id")
    private String id;

    /**
     * 标签名
     */
    @ApiModelProperty("标签名")
    private String name;
}
