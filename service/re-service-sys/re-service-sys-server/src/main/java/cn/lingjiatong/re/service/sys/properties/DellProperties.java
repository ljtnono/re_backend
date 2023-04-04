package cn.lingjiatong.re.service.sys.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * dell服务器相关配置类
 *
 * @author Ling, Jiatong
 * Date: 2023/4/4 16:25
 */
@Data
@Component
@ConfigurationProperties(prefix = "dell")
public class DellProperties {

    private KubernetesProperties kubernetesProperties;

}
