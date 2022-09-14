package cn.ljtnono.re.service.blog;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;

/**
 * 博客模块启动类
 *
 * @author Ling, Jiatong
 * Date: 2021/9/5 12:09 上午
 */
@SpringCloudApplication
public class BlogServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(BlogServiceApplication.class, args);
    }

}
