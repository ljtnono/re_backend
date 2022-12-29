package cn.lingjiatong.re.job;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 定时任务启动类
 *
 * @author Ling, Jiatong
 * Date: 2022/10/11 22:07
 */
@EnableDiscoveryClient
@SpringBootApplication
public class ReJobApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReJobApplication.class, args);
    }

}
