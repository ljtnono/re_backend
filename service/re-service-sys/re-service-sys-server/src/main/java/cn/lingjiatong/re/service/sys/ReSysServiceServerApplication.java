package cn.lingjiatong.re.service.sys;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import springfox.documentation.oas.annotations.EnableOpenApi;

/**
 * 系统相关模块微服务
 *
 * @author Ling, Jiatong
 * Date: 2022/9/17 17:41
 */
@EnableOpenApi
@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = {"cn.lingjiatong.re.service.sys", "cn.lingjiatong.re.common"})
@EnableFeignClients(basePackages = {"cn.lingjiatong.re.service.sys.api"})
@MapperScan(basePackages = {"cn.lingjiatong.re.service.sys.mapper"})
public class ReSysServiceServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReSysServiceServerApplication.class, args);
    }

}
