package cn.lingjiatong.re.service.sys.util;

import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.util.ClientBuilder;
import io.kubernetes.client.util.credentials.AccessTokenAuthentication;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * k8s功能类
 *
 * @author Ling, Jiatong
 * Date: 2022/12/8 14:33
 */
@Slf4j
@Component
public class KubernetesUtil {

    private static ApiClient apiClient;

    /**
     * api server访问地址
     */
    public static final String API_SERVER_PATH = "https://192.168.8.131:6443";

    /**
     * api server访问token
     */
    public static final String TOKEN = "";

    static {
        apiClient = new ClientBuilder()
                .setBasePath(API_SERVER_PATH)
                .setVerifyingSsl(false)
                .setAuthentication(new AccessTokenAuthentication(TOKEN))
                .build();
        Configuration.setDefaultApiClient(apiClient);
    }
}
