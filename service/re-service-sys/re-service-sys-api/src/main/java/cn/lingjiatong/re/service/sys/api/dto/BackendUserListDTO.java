package cn.lingjiatong.re.service.sys.api.dto;

import cn.lingjiatong.re.common.BasePageDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 后台获取用户列表DTO对象
 *
 * @author Ling, Jiatong
 * Date: 2022/10/31 20:31
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "后台获取用户列表DTO对象")
public class BackendUserListDTO extends BasePageDTO {

    /**
     * 查询条件
     */
    @ApiModelProperty("查询条件")
    private String searchCondition;

}
