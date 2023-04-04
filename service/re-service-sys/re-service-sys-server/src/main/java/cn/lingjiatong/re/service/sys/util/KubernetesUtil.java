package cn.lingjiatong.re.service.sys.util;

import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.V1NamespaceList;
import io.kubernetes.client.openapi.models.V1NodeList;
import io.kubernetes.client.openapi.models.V1PodList;
import lombok.extern.slf4j.Slf4j;

/**
 * k8s功能类
 *
 * @author Ling, Jiatong
 * Date: 2022/12/8 14:33
 */
@Slf4j
public class KubernetesUtil {

    private KubernetesUtil() {}

    public static KubernetesUtil getInstance() {
        return Holder.INSTANCE;
    }

    public static final class Holder {
        private static final KubernetesUtil INSTANCE = new KubernetesUtil();
    }

    /**
     * 获取k8s集群的命名空间列表
     *
     * @return 命名空间列表对象
     */
    public V1NamespaceList getNamespaceList() throws ApiException {
        CoreV1Api coreV1Api = new CoreV1Api();
        return coreV1Api.listNamespace("true", null, null, null, null, null, null, null, null, null);
    }

    /**
     * 获取k8s集群node列表
     *
     * @return k8s node列表对象
     */
    public V1NodeList getNodeList() throws ApiException {
        CoreV1Api coreV1Api = new CoreV1Api();
        return coreV1Api.listNode("true", null, null, null, null, null, null, null, null, null);
    }

    /**
     * 根据名称空间获取该名称空间下的pod信息列表
     *
     * @param namespace 名称空间
     * @return pod信息列表
     */
    public V1PodList getPodList(String namespace) throws ApiException {
        CoreV1Api coreV1Api = new CoreV1Api();
        return coreV1Api.listNamespacedPod(namespace, "true", null, null, null, null, null, null, null, null, null);
    }
}
