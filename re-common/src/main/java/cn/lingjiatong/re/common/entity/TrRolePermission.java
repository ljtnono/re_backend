package cn.lingjiatong.re.common.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 角色权限关联表
 *
 * @author Ling, Jiatong
 * Date: 2022/10/22 19:25
 */
@Data
@TableName(value = "tr_role_permission", schema = "re_sys")
public class TrRolePermission {

    /**
     * 主键id
     */
    @TableId
    private Long id;

    /**
     * 角色id
     */
    private Long roleId;

    /**
     * 权限id
     */
    private Long permissionId;

}
