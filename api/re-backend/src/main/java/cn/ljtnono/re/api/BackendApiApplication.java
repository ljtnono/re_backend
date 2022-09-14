package cn.ljtnono.re.api;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;

/**
 * 后端页面调用api服务
 *
 * @author Ling, Jiatong
 * Date: 2021/10/20 11:07 下午
 */
@SpringCloudApplication
public class BackendApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackendApiApplication.class, args);
    }

}
