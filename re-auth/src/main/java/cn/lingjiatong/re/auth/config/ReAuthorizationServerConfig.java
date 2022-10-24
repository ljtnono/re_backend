package cn.lingjiatong.re.auth.config.oauth2;

import cn.lingjiatong.re.common.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import javax.sql.DataSource;
import java.security.KeyPair;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * oauth2认证服务器配置
 *
 * @author Ling, Jiatong
 * Date: 2022/10/23 22:19
 */
@Configuration
@EnableAuthorizationServer
public class ReAuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private DataSource dataSource;

    /**
     * 客户端信息配置
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        JdbcClientDetailsService jdbcClientDetailsService = new JdbcClientDetailsService(dataSource);
//        jdbcClientDetailsService.setFindClientDetailsSql(AuthConstants.FIND_CLIENT_DETAILS_SQL);
//        jdbcClientDetailsService.setSelectClientDetailsSql(AuthConstants.SELECT_CLIENT_DETAILS_SQL);
        clients.withClientDetails(jdbcClientDetailsService);
    }

    /**
     * 认证服务器是玩转token的，那么这里配置token令牌管理相关（token此时就是一个字符串，当下的token需要在服务器端存储，那么存储在哪里呢？都是在这里配置）
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        List<TokenEnhancer> tokenEnhancers = new ArrayList<>();
        tokenEnhancers.add(tokenEnhancer());
        tokenEnhancers.add(jwtAccessTokenConverter());
        tokenEnhancerChain.setTokenEnhancers(tokenEnhancers);

        endpoints.authenticationManager(authenticationManager)
                .accessTokenConverter(jwtAccessTokenConverter())
                .tokenEnhancer(tokenEnhancerChain)
                .userDetailsService(userDetailsService)
                // refresh_token有两种使用方式：重复使用(true)、非重复使用(false)，默认为true
                //      1.重复使用：access_token过期刷新时， refresh token过期时间未改变，仍以初次生成的时间为准
                //      2.非重复使用：access_token过期刷新时， refresh_token过期时间延续，在refresh_token有效期内刷新而无需失效再次登录
                .reuseRefreshTokens(false);
    }


    /**
     * 认证服务器最终是以api接口的方式对外提供服务（校验合法性并生成令牌、校验令牌等）
     * 那么，以api接口方式对外的话，就涉及到接口的访问权限，我们需要在这里进行必要的配置
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        // 相当于打开endpoints访问接口的开关，这样的话后期我们能够访问该接口
        security.allowFormAuthenticationForClients();
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
     * 从classpath下的密钥库中获取密钥对(公钥+私钥)
     */
    @Bean
    public KeyPair keyPair() {
        KeyStoreKeyFactory factory = new KeyStoreKeyFactory(new ClassPathResource("re.jks"), "ROOTELEMENT".toCharArray());
        KeyPair keyPair = factory.getKeyPair("re", "ROOTELEMENT".toCharArray());
        return keyPair;
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
            ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(map);
            return accessToken;
        };
    }
}
