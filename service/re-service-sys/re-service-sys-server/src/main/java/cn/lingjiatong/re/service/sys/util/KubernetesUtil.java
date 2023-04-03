package cn.lingjiatong.re.service.sys.util;

import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.V1ContainerStatus;
import io.kubernetes.client.openapi.models.V1NamespaceList;
import io.kubernetes.client.openapi.models.V1Pod;
import io.kubernetes.client.openapi.models.V1PodList;
import io.kubernetes.client.util.ClientBuilder;
import io.kubernetes.client.util.credentials.AccessTokenAuthentication;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Collectors;

/**
 * k8s功能类
 *
 * @author Ling, Jiatong
 * Date: 2022/12/8 14:33
 */
@Slf4j
@Component
public class KubernetesUtil {

//    @Value("${k8s.endpoint}")
    private String endpoint = "https://www.lingjiatong.cn:6443";
//    @Value("${k8s.token}")
    private String token = "eyJhbGciOiJSUzI1NiIsImtpZCI6IlUxX0lfTkJIXzkwajk0M256TzdkVTRCaUV2UEhMb0l2azJhandrNjVzSnMifQ.eyJpc3MiOiJrdWJlcm5ldGVzL3NlcnZpY2VhY2NvdW50Iiwia3ViZXJuZXRlcy5pby9zZXJ2aWNlYWNjb3VudC9uYW1lc3BhY2UiOiJyb290ZWxlbWVudCIsImt1YmVybmV0ZXMuaW8vc2VydmljZWFjY291bnQvc2VjcmV0Lm5hbWUiOiJqYXZhLWNsaWVudC1zZXJ2aWNlLWFjY291bnQtdG9rZW4tcXA3NTUiLCJrdWJlcm5ldGVzLmlvL3NlcnZpY2VhY2NvdW50L3NlcnZpY2UtYWNjb3VudC5uYW1lIjoiamF2YS1jbGllbnQtc2VydmljZS1hY2NvdW50Iiwia3ViZXJuZXRlcy5pby9zZXJ2aWNlYWNjb3VudC9zZXJ2aWNlLWFjY291bnQudWlkIjoiZmJmNzgxMmQtY2UzZi00ZDM2LWI1NjctNDM0YjMwZmUzYzJhIiwic3ViIjoic3lzdGVtOnNlcnZpY2VhY2NvdW50OnJvb3RlbGVtZW50OmphdmEtY2xpZW50LXNlcnZpY2UtYWNjb3VudCJ9.haY0p189dZc0XrGHhuTECEBzeOtkhYRQYcqgJo-4qxAnFAqiTAoMOE1Kv3BXQTvNrsH9-Vej09Ba-p59K-9bVAyoMKogpqa0Se9n7bNKBfObcAxnm3PrpP2UtQOhaaSWdywGG7ATgwnsH7hjPUyQNHCuaLh4Wq2EzuDY8j_mXtm_BQqH5UlhqBniOXPf850yWRu6CyzVhM3ENyMQQXbZxyYCSf0OoxyqKLQUza3OAQEBD5bWkYMAfHIgRzbzbCFT8sjG8Xz7OETS56P1gUPQKBAvf0uzxQSrAK6scMKSwB8E6L7zzAaKDkNAb20ZfC5x6qUFb8Gqfk_dG0JheNHwOg";

    /**
     * k8s的初始化
     *
     * @return client
     */
    public ApiClient getConnection() {
        ApiClient client = new ClientBuilder()
                .setBasePath(endpoint)
                .setVerifyingSsl(false)
                .setAuthentication(new AccessTokenAuthentication(token))
                .build();
        Configuration.setDefaultApiClient(client);
        return client;
    }

    /**
     * 获取k8s集群的命名空间列表
     *
     * @return 命名空间列表对象
     * @throws ApiException
     */
    public V1NamespaceList getNamespaceList() throws ApiException {
        CoreV1Api apiInstance = new CoreV1Api();
        V1NamespaceList namespaceList = apiInstance.listNamespace("true", null, null, null, null, null, null, null, null, null);
        return namespaceList;
    }

    /**
     * 根据名称空间获取该名称空间下的pod信息列表
     *
     * @param namespace 名称空间
     * @return pod信息列表
     */
    public void getPodList(String namespace) throws ApiException {
        CoreV1Api apiInstance = new CoreV1Api();
        V1PodList v1PodList = apiInstance.listNamespacedPod(namespace, "true", null, null, null, null, null, null, null, null, null);
        List<V1Pod> items = v1PodList.getItems();
        System.out.printf("%-50s%-10s%-20s%-10s%-20s%-20s%-20s%-20s%-20s%n",
                "NAME", "READY", "STATUS", "RESTARTS", "AGE", "IP", "NODE", "NOMINATED NODE", "READINESS GATES");
        for (V1Pod item: items) {
            String name = item.getMetadata().getName();
            List<V1ContainerStatus> containerStatuses = item.getStatus().getContainerStatuses();
            int readyCount = 0;
            int totalCount = containerStatuses.size();
            for (V1ContainerStatus containerStatus : containerStatuses) {
                if (containerStatus.getReady()) {
                    readyCount++;
                }
            }
            String ready = readyCount + "/" + totalCount;
            String status = item.getStatus().getPhase();

            OffsetDateTime lastRestartTime = item.getStatus().getStartTime();
            Duration lastRestartTimeduration = Duration.between(lastRestartTime, OffsetDateTime.now());
            StringBuilder restartStringBuilder = new StringBuilder(String.valueOf(item.getStatus().getContainerStatuses().get(0).getRestartCount()));
            if (lastRestartTimeduration.toMinutes() < 60) {
                restartStringBuilder.append(" (").append(lastRestartTimeduration.toMinutes()).append("m ago)");
            } else if (lastRestartTimeduration.toHours() < 24) {
                restartStringBuilder.append(" (").append(lastRestartTimeduration.toHours()).append("h ago)");
            } else {
                restartStringBuilder.append(" (").append(lastRestartTimeduration.toDays()).append("d ago)");
            }
            String restarts = restartStringBuilder.toString();


            OffsetDateTime podStartTime = item.getStatus().getStartTime();
            Duration duration = Duration.between(podStartTime, OffsetDateTime.now(ZoneOffset.UTC));
            long totalSeconds = duration.getSeconds();
            long days = totalSeconds / (60 * 60 * 24);
            long hours = (totalSeconds % (60 * 60 * 24)) / (60 * 60);
            String age = days + "d" + hours + "h";
            String ip = item.getStatus().getPodIP();
            String node = item.getSpec().getNodeName();
            String nominatedNodeName = item.getStatus().getNominatedNodeName();
            String readinessGates = item.getSpec().getReadinessGates() == null ? null :
                    String.join(",", item.getSpec().getReadinessGates()
                            .stream()
                            .map(rg -> rg.getConditionType()).collect(Collectors.toList()));
            System.out.printf("%-50s%-10s%-20s%-10s%-20s%-20s%-20s%-20s%-20s%n", name, ready, status, restarts, age, ip, node, nominatedNodeName, readinessGates);

        }
    }


    public static void main(String[] args) throws ApiException {
        KubernetesUtil kubernetesUtil = new KubernetesUtil();
        ApiClient connection = kubernetesUtil.getConnection();

        kubernetesUtil.getPodList("common");
    }
}
