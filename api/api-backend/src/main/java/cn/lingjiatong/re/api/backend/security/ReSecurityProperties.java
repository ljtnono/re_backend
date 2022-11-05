package cn.lingjiatong.re.api.backend.security;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * security相关配置
 *
 * @author Ling, Jiatong
 * Date: 2022/10/23 00:05
 */
@Data
@Component
@ConfigurationProperties(prefix = "security")
public class ReSecurityProperties {

    /**
     * 不需要token的请求路径
     */
    private List<String> passTokenUrl;
}
