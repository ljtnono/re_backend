package cn.lingjiatong.re.service.sys.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 自定义swagger配置类
 *
 * @author Ling, Jiatong
 * Date: 2023/1/6 11:04
 */
@Data
@Component
@ConfigurationProperties(prefix = "swagger")
public class SwaggerProperties {

    /**
     * 项目应用名
     */
    private String applicationName;

    /**
     * 项目版本信息
     */
    private String applicationVersion;

    /**
     * 项目描述信息
     */
    private String applicationDescription;

}
