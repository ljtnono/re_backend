package cn.lingjiatong.re.service.sys.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * k8s集群配置类
 *
 * @author Ling, Jiatong
 * Date: 2023/4/4 16:30
 */
@Data
@Component
@ConfigurationProperties(prefix = "dell.kubernetes")
public class KubernetesProperties {


    private List<KubernetesNode> nodeList;


    @Data
    public static final class KubernetesNode {

        private String hostName;

        private String ipAddr;

        private String sshUsername;

        private String sshPassword;

        private Integer sshPort;
    }
}
