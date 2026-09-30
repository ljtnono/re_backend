package cn.lingjiatong.re.service.sys.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

/**
 * 系统配置实体类
 *
 * @author Ling, Jiatong
 * Date: 2022/9/18 11:10
 */
@Data
@ToString(callSuper = true)
@TableName(value = "sys_config")
@Schema(description = "系统配置实体类")
public class SysConfig {

    /**
     * 主键id，自增
     */
    @TableId
    @Schema(description = "主键id")
    private Long id;

    /**
     * 配置描述
     */
    @Schema(description = "配置描述")
    private String description;

    /**
     * 配置项的key
     */
    @Schema(description = "配置项的key")
    private String key;

    /**
     * 配置项的值
     */
    @Schema(description = "配置项的值")
    private String value;
}
