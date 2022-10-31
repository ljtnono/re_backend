package cn.lingjiatong.re.common;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 分页查询基类
 *
 * @author Ling, Jiatong
 * Date: 2022/10/31 20:46
 */
@Data
public class BasePageDTO {

    /**
     * 当前页
     */
    @ApiModelProperty(value = "分页查询页数 不传默认为1", example = "1", dataType = "Integer")
    private Integer pageNum = 1;

    /**
     * 每页条数
     */
    @ApiModelProperty(value = "每页条数 不传默认为10", example = "10", dataType = "Integer")
    private Integer pageSize = 10;
}
