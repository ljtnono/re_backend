package cn.lingjiatong.re.api;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 前端项目接口
 *
 * @author Ling, Jiatong
 * Date: 2022/9/17 17:41
 */
@SpringCloudApplication
@EnableFeignClients
public class FrontendApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(FrontendApiApplication.class, args);
    }

}
