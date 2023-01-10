package cn.lingjiatong.re.service.article;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 博客文章相关模块微服务启动类
 *
 * @author Ling, Jiatong
 * Date: 2022/10/16 11:17
 */
@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = {"cn.lingjiatong.re.service.article", "cn.lingjiatong.re.common"})
@EnableFeignClients(basePackages = {"cn.lingjiatong.re.service.article.api", "cn.lingjiatong.re.service.sys.api"})
@MapperScan(basePackages = {"cn.lingjiatong.re.service.article.mapper"})
public class ReArticleServiceServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReArticleServiceServerApplication.class, args);
    }
}
