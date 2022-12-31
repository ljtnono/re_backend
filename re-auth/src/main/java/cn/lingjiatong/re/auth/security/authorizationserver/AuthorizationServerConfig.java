package cn.lingjiatong.re.auth.security.authorizationserver;

import cn.lingjiatong.re.common.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
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
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.sql.DataSource;
import java.security.KeyPair;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * oauth2认证服务器配置
 *
 * @author Ling, Jiatong
 * Date: 2022/10/23 22:19
 */
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {
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
    @Qualifier("customOAuth2AuthenticationEntryPoint")
    private AuthenticationEntryPoint authenticationEntryPoint;
    @Autowired
    private TokenEnhancer tokenEnhancer;
    @Autowired
    private KeyPair keyPair;
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
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setKeyPair(keyPair);
        tokenEnhancers.add(tokenEnhancer);
        tokenEnhancers.add(converter);
        tokenEnhancerChain.setTokenEnhancers(tokenEnhancers);

        // 获取内置的授权类型
        List<TokenGranter> granterList = new ArrayList<>(Collections.singletonList(endpoints.getTokenGranter()));
        // 新增自定义的验证码授权类型
        granterList.add(new VerifyCodeTokenGranter(endpoints.getTokenServices(), endpoints.getClientDetailsService(),
                endpoints.getOAuth2RequestFactory(), authenticationManager, redisUtil, profile));
        CompositeTokenGranter compositeTokenGranter = new CompositeTokenGranter(granterList);

        endpoints.authenticationManager(authenticationManager)
                .accessTokenConverter(converter)
                .userDetailsService(userDetailsService)
                .tokenEnhancer(tokenEnhancerChain)
                .tokenStore(new JwtTokenStore(converter))
                .pathMapping("/oauth/token", "/user/login")
                .allowedTokenEndpointRequestMethods(HttpMethod.POST)
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
                .checkTokenAccess("isAuthenticated()");
    }

}
