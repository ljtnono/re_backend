package cn.lingjiatong.re.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 根元素博客网关
 *
 * @author Ling, Jiatong
 * Date: 2022/9/22 15:45
 */
@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = {"cn.lingjiatong.re.gateway"})
public class ReGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReGatewayApplication.class, args);
    }

}
