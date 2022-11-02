package cn.lingjiatong.re.gateway.config;

import cn.lingjiatong.re.common.constant.CommonConstant;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import java.security.KeyPair;

/**
 * spring bean配置
 *
 * @author Ling, Jiatong
 * Date: 2022/11/2 20:41
 */
@Configuration
public class SpringBeanConfig {

    /**
     * 从classpath下的密钥库中获取密钥对(公钥+私钥)
     */
    @Bean
    public KeyPair keyPair() {
        KeyStoreKeyFactory factory = new KeyStoreKeyFactory(new ClassPathResource(CommonConstant.TOKEN_SECRET_KEY_NAME), CommonConstant.TOKEN_SECRET_KEY_PASSWORD.toCharArray());
        KeyPair keyPair = factory.getKeyPair(CommonConstant.TOKEN_SERET_KEY_ALIAS, CommonConstant.TOKEN_SECRET_KEY_PASSWORD.toCharArray());
        return keyPair;
    }
}
