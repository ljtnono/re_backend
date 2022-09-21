package cn.lingjiatong.re.api.frontend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 前端项目接口
 *
 * @author Ling, Jiatong
 * Date: 2022/9/17 17:41
 */
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"cn.lingjiatong.re.service.sys.api"})
@SpringBootApplication(scanBasePackages = {"cn.lingjiatong.re.api.frontend", "cn.lingjiatong.re.common"})
public class FrontendApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(FrontendApiApplication.class, args);
    }

}
