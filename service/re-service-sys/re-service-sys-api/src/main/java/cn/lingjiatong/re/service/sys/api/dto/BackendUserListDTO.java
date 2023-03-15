package cn.lingjiatong.re.service.sys.api.dto;

import cn.lingjiatong.re.common.BasePageDTO;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(name = "BackendUserListDTO", description = "后台获取用户列表DTO对象")
public class BackendUserListDTO extends BasePageDTO {

    /**
     * 查询条件
     */
    @Schema(description = "查询条件")
    private String searchCondition;




}
