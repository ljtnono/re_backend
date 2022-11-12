package cn.lingjiatong.re.common.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * 用户角色关联表
 *
 * @author Ling, Jiatong
 * Date: 2022/10/22 19:24
 */
@Data
public class TrUserRole {

    /**
     * 主键id，自增
     */
    @TableId
    private Long id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 角色id
     */
    private Long roleId;

}
