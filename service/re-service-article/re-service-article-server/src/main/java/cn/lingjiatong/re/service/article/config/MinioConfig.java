package cn.lingjiatong.re.service.article.config;

import cn.lingjiatong.re.common.util.MinioUtil;
import io.minio.MinioClient;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * minio配置类
 *
 * @author Ling, Jiatong
 * Date: 2022/10/18 20:27
 */
@Data
@Configuration
public class MinioConfig {

    /**
     * 访问地址
     */
    @Value("${minio.endpoint}")
    private String endpoint;

    /**
     * accessKey类似于用户ID，用于唯一标识你的账户
     */
    @Value("${minio.accessKey}")
    private String accessKey;

    /**
     * secretKey是你账户的密码
     */
    @Value("${minio.secretKey}")
    private String secretKey;

    /**
     * 默认存储桶
     */
    @Value("${minio.bucketName}")
    private String bucketName;

    @Bean
    public MinioClient minioClient() {
        MinioClient minioClient = MinioClient.builder()
                .endpoint(endpoint)
                .credentials(accessKey, secretKey)
                .build();
        return minioClient;
    }

    @Bean
    public MinioUtil minioUtil(@Autowired MinioClient minioClient) {
        return new MinioUtil(minioClient);
    }
}
