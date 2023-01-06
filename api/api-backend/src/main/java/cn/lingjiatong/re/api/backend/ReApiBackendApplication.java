package cn.lingjiatong.re.api.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import springfox.documentation.oas.annotations.EnableOpenApi;

/**
 * 后端接口应用启动器
 *
 * @author Ling, Jiatong
 * Date: 2022/11/4 17:46
 */
@EnableOpenApi
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"cn.lingjiatong.re.service.sys.api", "cn.lingjiatong.re.service.article.api"})
@SpringBootApplication(scanBasePackages = {"cn.lingjiatong.re.api.backend", "cn.lingjiatong.re.common"})
public class ReApiBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReApiBackendApplication.class, args);
    }

}
