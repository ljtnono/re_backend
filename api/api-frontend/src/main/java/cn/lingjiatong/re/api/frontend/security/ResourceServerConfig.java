package cn.lingjiatong.re.api.frontend.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

/**
 * oauth2 资源服务器配置
 *
 * @author Ling, Jiatong
 * Date: 2022/11/4 01:34
 */
@Slf4j
@Configuration
@EnableResourceServer
@EnableGlobalMethodSecurity(prePostEnabled = true, jsr250Enabled = true)
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Autowired
    private ReSecurityProperties reSecurityProperties;
    @Autowired
    private AuthenticationHandler authenticationHandler;
    @Autowired
    private ResourceTokenService resourceTokenService;


    @Override
    public void configure(HttpSecurity http) throws Exception {
        // 配置不使用 session
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        // 禁用缓存
        http.headers().cacheControl();
        // 设置安全策略
        http.headers().contentSecurityPolicy("default-src 'self'");
        // 放行 swagger ui (有整合swagger就放行这些请求吧)
        http
                .authorizeRequests()
                .antMatchers(reSecurityProperties.getPassTokenUrl().toArray(new String[]{}))
                .permitAll();

        // 其他请求均需要token才能访问
        http
                .authorizeRequests()
                .anyRequest()
                .authenticated();
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        // 配置如何解析token
        resources
                .authenticationEntryPoint(authenticationHandler)
                .accessDeniedHandler(authenticationHandler)
                .tokenServices(resourceTokenService);
    }

}
