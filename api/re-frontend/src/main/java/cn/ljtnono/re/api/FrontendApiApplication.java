package cn.ljtnono.re.api;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 *
 *
 * @author Ling, Jiatong
 */
@SpringCloudApplication
@EnableFeignClients
public class FrontendApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(FrontendApiApplication.class, args);
    }

}
