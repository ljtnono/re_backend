package cn.lingjiatong.re.auth.security.resourceserver;

import cn.lingjiatong.re.auth.security.ReSecurityProperties;
import cn.lingjiatong.re.common.constant.CommonConstant;
import cn.lingjiatong.re.common.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import java.security.KeyPair;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * spring security oauth2 相关bean配置
 *
 * @author Ling, Jiatong
 * Date: 2022/11/5 09:27
 */
@Configuration
public class SpringSecurityOauth2Config {

    @Autowired
    private ReSecurityProperties reSecurityProperties;
    @Autowired
    private JwtAccessTokenConverter jwtAccessTokenConverter;

    /**
     * 从classpath下的密钥库中获取密钥对(公钥+私钥)
     */
    @Bean
    public KeyPair keyPair() {
        KeyStoreKeyFactory factory = new KeyStoreKeyFactory(new ClassPathResource(CommonConstant.TOKEN_SECRET_KEY_NAME), CommonConstant.TOKEN_SECRET_KEY_PASSWORD.toCharArray());
        KeyPair keyPair = factory.getKeyPair(CommonConstant.TOKEN_SECRET_KEY_ALIAS, CommonConstant.TOKEN_SECRET_KEY_PASSWORD.toCharArray());
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

    /**
     * JWT内容增强
     */
    @Bean
    public TokenEnhancer tokenEnhancer() {
        return (accessToken, authentication) -> {
            Map<String, Object> map = new HashMap<>(2);
            User user = (User) authentication.getUserAuthentication().getPrincipal();
            map.put("userId", user.getId());
            map.put("username", user.getUsername());
            map.put("email", user.getEmail());
            map.put("phone", user.getPhone());
            ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(map);
            // 根据配置文件设置过期时间
            Integer expirationHour = reSecurityProperties.getTokenExpireTime();
            long l = System.currentTimeMillis() + 3600 * 1000 * expirationHour;
            ((DefaultOAuth2AccessToken) accessToken).setExpiration(new Date(l));
            return accessToken;
        };
    }

}
