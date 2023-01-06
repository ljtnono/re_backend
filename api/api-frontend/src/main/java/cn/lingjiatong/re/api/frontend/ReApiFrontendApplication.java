package cn.lingjiatong.re.api.frontend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import springfox.documentation.oas.annotations.EnableOpenApi;

/**
 * 前端项目接口
 *
 * @author Ling, Jiatong
 * Date: 2022/9/17 17:41
 */
@EnableOpenApi
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"cn.lingjiatong.re.service.sys.api", "cn.lingjiatong.re.service.article.api"})
@SpringBootApplication(scanBasePackages = {"cn.lingjiatong.re.api.frontend", "cn.lingjiatong.re.common"})
public class ReApiFrontendApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReApiFrontendApplication.class, args);
    }

}
