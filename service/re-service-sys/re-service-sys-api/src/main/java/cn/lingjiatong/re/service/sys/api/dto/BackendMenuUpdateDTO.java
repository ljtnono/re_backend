package cn.lingjiatong.re.service.sys.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 后台更新菜单DTO对象
 *
 * @author Ling, Jiatong
 * Date: 2023/5/5 16:42
 */
@Data
@Schema(name = "BackendMenuUpdateDTO", description = "后台更新菜单DTO对象")
public class BackendMenuUpdateDTO {

    private Long menuId;

    private String name;

}
