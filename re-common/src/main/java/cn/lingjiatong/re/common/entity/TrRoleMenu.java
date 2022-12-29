package cn.lingjiatong.re.common.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 角色菜单表
 *
 * @author Ling, Jiatong
 * Date: 2022/12/29 22:22
 */
@Data
@TableName("tr_role_menu")
public class TrRoleMenu {

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
     * 菜单id
     */
    private Long menuId;

}
