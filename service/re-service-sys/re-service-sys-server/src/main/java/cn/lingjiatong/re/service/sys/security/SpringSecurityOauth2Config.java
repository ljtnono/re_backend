package cn.lingjiatong.re.service.sys.security;

import cn.lingjiatong.re.common.constant.CommonConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import java.security.KeyPair;

/**
 * spring security oauth2 相关bean配置
 *
 * @author Ling, Jiatong
 * Date: 2022/11/5 09:27
 */
@Configuration
public class SpringSecurityOauth2Config {

    @Autowired
    private JwtAccessTokenConverter jwtAccessTokenConverter;

    /**
     * 从classpath下的密钥库中获取密钥对(公钥+私钥)
     */
    @Bean
    public KeyPair keyPair() {
        KeyStoreKeyFactory factory = new KeyStoreKeyFactory(new ClassPathResource(CommonConstant.TOKEN_SECRET_KEY_NAME), CommonConstant.TOKEN_SECRET_KEY_PASSWORD.toCharArray());
        KeyPair keyPair = factory.getKeyPair(CommonConstant.TOKEN_SERET_KEY_ALIAS, CommonConstant.TOKEN_SECRET_KEY_PASSWORD.toCharArray());
        return keyPair;
    }

    /**
     * 使用非对称加密算法对token签名
     */
    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setKeyPair(keyPair());
        return converter;
    }

    /**
     * jwt token存储模式
     */
    @Bean
    public JwtTokenStore jwtTokenStore(){
        return new JwtTokenStore(jwtAccessTokenConverter);
    }

}
