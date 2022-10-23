package cn.lingjiatong.re.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * 角色权限关联表
 *
 * @author Ling, Jiatong
 * Date: 2022/10/22 19:25
 */
@Data
public class TrRolePermission {

    /**
     * 主键id，自增
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 角色id
     */
    private Integer roleId;

    /**
     * 用户id
     */
    private Integer userId;

}
