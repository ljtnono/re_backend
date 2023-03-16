package cn.lingjiatong.re.service.sys.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@TableName(value = "sys_config", schema = "re_sys")
@ApiModel(description = "系统配置实体类")
public class SysConfig {

    /**
     * 主键id，自增
     */
    @TableId
    @ApiModelProperty("主键id")
    private Long id;

    /**
     * 配置描述
     */
    @ApiModelProperty("配置描述")
    private String description;

    /**
     * 配置项的key
     */
    @ApiModelProperty("配置项的key")
    private String key;

    /**
     * 配置项的值
     */
    @ApiModelProperty("配置项的值")
    private String value;
}
