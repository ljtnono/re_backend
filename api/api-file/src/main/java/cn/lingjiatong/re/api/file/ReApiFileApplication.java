package cn.lingjiatong.re.api.file;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 后端接口应用启动器
 *
 * @author Ling, Jiatong
 * Date: 2022/11/4 17:46
 */
@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = {"cn.lingjiatong.re.api.file", "cn.lingjiatong.re.common"})
public class ReApiFileApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReApiFileApplication.class, args);
    }

}
