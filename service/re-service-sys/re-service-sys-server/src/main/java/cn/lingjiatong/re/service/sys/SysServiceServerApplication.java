package cn.lingjiatong.re.service.sys;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 系统设置微服务
 *
 * @author Ling, Jiatong
 * Date: 2022/9/17 17:41
 */
@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = {"cn.lingjiatong.re.service.sys", "cn.lingjiatong.re.common"})
@EnableFeignClients(basePackages = {"cn.lingjiatong.re.service.sys.api"})
@MapperScan(basePackages = {"cn.lingjiatong.re.service.sys.mapper"})
public class SysServiceServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SysServiceServerApplication.class, args);
    }

}
