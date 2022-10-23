package cn.lingjiatong.re.auth;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * re-auth启动器
 *
 * @author Ling, Jiatong
 * Date: 2022/10/22 18:09
 */
@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = {"cn.lingjiatong.re.auth", "cn.lingjiatong.re.common"})
@MapperScan(basePackages = {"cn.lingjiatong.re.common.mapper"})
public class ReAuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReAuthApplication.class, args);
    }

}
