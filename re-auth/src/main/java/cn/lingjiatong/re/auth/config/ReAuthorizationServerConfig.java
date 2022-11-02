package cn.lingjiatong.re.auth.config;

import cn.lingjiatong.re.auth.component.CustomClientCredentialsTokenEndpointFilter;
import cn.lingjiatong.re.auth.component.OAuth2WebResponseExceptionTranslator;
import cn.lingjiatong.re.auth.component.VerifyCodeTokenGranter;
import cn.lingjiatong.re.common.constant.CommonConstant;
import cn.lingjiatong.re.common.entity.User;
import cn.lingjiatong.re.common.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.CompositeTokenGranter;
import org.springframework.security.oauth2.provider.TokenGranter;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.sql.DataSource;
import java.security.KeyPair;
import java.util.*;

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
    @Qualifier("userService")
    private UserDetailsService userDetailsService;
    @Autowired
    private DataSource dataSource;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private OAuth2WebResponseExceptionTranslator oAuth2WebResponseExceptionTranslator;
    @Autowired
    private JwtAccessTokenConverter jwtAccessTokenConverter;
    @Autowired
    @Qualifier("customOAuth2AuthenticationEntryPoint")
    private AuthenticationEntryPoint authenticationEntryPoint;

    @Value("${spring.profiles.active}")
    private String profile;


    /**
     * 客户端信息配置
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        JdbcClientDetailsService jdbcClientDetailsService = new JdbcClientDetailsService(dataSource);
        clients.withClientDetails(jdbcClientDetailsService);
    }

    /**
     * 认证服务器是玩转token的，那么这里配置token令牌管理相关（token此时就是一个字符串，当下的token需要在服务器端存储，那么存储在哪里呢？都是在这里配置）
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        List<TokenEnhancer> tokenEnhancers = new ArrayList<>();
        tokenEnhancers.add(tokenEnhancer());
        tokenEnhancers.add(jwtAccessTokenConverter);
        tokenEnhancerChain.setTokenEnhancers(tokenEnhancers);

        // 获取内置的授权类型
        List<TokenGranter> granterList = new ArrayList<>(Collections.singletonList(endpoints.getTokenGranter()));
        // 新增自定义的验证码授权类型
        granterList.add(new VerifyCodeTokenGranter(endpoints.getTokenServices(), endpoints.getClientDetailsService(),
                endpoints.getOAuth2RequestFactory(), authenticationManager, redisUtil, profile));
        CompositeTokenGranter compositeTokenGranter = new CompositeTokenGranter(granterList);

        endpoints.authenticationManager(authenticationManager)
                .accessTokenConverter(jwtAccessTokenConverter)
                .userDetailsService(userDetailsService)
                .tokenEnhancer(tokenEnhancerChain)
                .tokenStore(jwtTokenStore())
                .allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST)
                .reuseRefreshTokens(false)
                .exceptionTranslator(oAuth2WebResponseExceptionTranslator)
                .tokenGranter(compositeTokenGranter);
    }

    /**
     * 认证服务器最终是以api接口的方式对外提供服务（校验合法性并生成令牌、校验令牌等）
     * 那么，以api接口方式对外的话，就涉及到接口的访问权限，我们需要在这里进行必要的配置
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) {
        // 相当于打开endpoints访问接口的开关，这样的话后期我们能够访问该接口
        CustomClientCredentialsTokenEndpointFilter endpointFilter = new CustomClientCredentialsTokenEndpointFilter(security);
        endpointFilter.setAuthenticationEntryPoint(authenticationEntryPoint);
        endpointFilter.setAuthenticationManager(authenticationManager);
        endpointFilter.afterPropertiesSet();
        security.allowFormAuthenticationForClients();
        security.addTokenEndpointAuthenticationFilter(endpointFilter);
        // 注意：security不需要在调用allowFormAuthenticationForClients方法
        security.tokenKeyAccess("permitAll()")
                .checkTokenAccess("permitAll()");
    }

    /**
     * jwt token存储模式
     */
    @Bean
    public JwtTokenStore jwtTokenStore(){
        return new JwtTokenStore(jwtAccessTokenConverter);
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
        KeyStoreKeyFactory factory = new KeyStoreKeyFactory(new ClassPathResource(CommonConstant.TOKEN_SECRET_KEY_NAME), CommonConstant.TOKEN_SECRET_KEY_PASSWORD.toCharArray());
        KeyPair keyPair = factory.getKeyPair(CommonConstant.TOKEN_SERET_KEY_ALIAS, CommonConstant.TOKEN_SECRET_KEY_PASSWORD.toCharArray());
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
            map.put("email", user.getEmail());
            map.put("phone", user.getPhone());
            ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(map);
            return accessToken;
        };
    }

}
