package cn.lingjiatong.re.service.sys.bo;

import io.kubernetes.client.openapi.models.V1Container;
import io.kubernetes.client.openapi.models.V1Pod;
import io.kubernetes.client.openapi.models.V1PodSpec;
import lombok.Data;

/**
 * k8s pod信息BO对象
 *
 * @author Ling, Jiatong
 * Date: 2023/1/5 14:56
 */
@Data
public class K8sPodBO {

    private String uid;

    private String name;

    private String namespace;

    private String image;

    private String podIP;

    private String phrase;


    public K8sPodBO(V1Pod v1Pod) {
        this.uid = v1Pod.getMetadata().getUid();
        this.name = v1Pod.getMetadata().getName();
        this.namespace = v1Pod.getMetadata().getNamespace();
        this.podIP = v1Pod.getStatus().getPodIP();
        V1PodSpec spec = v1Pod.getSpec();
        V1Container container = spec.getContainers().get(0);
        this.phrase = v1Pod.getStatus().getPhase();
        this.image = container.getImage();
    }
}
