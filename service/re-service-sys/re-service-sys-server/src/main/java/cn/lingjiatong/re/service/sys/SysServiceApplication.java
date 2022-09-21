package cn.lingjiatong.service.sys;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 系统设置微服务
 *
 * @author Ling, Jiatong
 * Date: 2022/9/17 17:41
 */
@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = {"cn.lingjiatong.service.sys"})
public class SysServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SysServiceApplication.class, args);
    }

}
