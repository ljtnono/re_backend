package cn.lingjiatong.re.service.sys.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 后台用户更新头像DTO对象
 *
 * @author Ling, Jiatong
 */
@Data
@Schema(name = "BackendUserUpdateAvatarDTO", description = "后台用户更新头像DTO对象")
public class BackendUserUpdateAvatarDTO {

    /**
     * 头像文件地址
     */
    @Schema(description = "头像文件地址")
    private String avatarUrl;
}
