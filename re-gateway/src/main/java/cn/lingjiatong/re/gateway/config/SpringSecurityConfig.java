package cn.lingjiatong.re.gateway.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

import java.util.Arrays;

/**
 * 安全策略配置，网关层面全部放行，使用网关的过滤器进行请求拦截
 *
 * @author Ling, Jiatong
 * Date: 2022/11/2 22:45
 */
@EnableWebFluxSecurity
public class SpringSecurityConfig  {

    @Autowired
    private ReSecurityProperties reSecurityProperties;

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity httpSecurity) {
        httpSecurity.csrf().disable();
        httpSecurity.headers().cache().disable();
        httpSecurity.headers().contentSecurityPolicy("default-src 'self'");
        // 只放行来自于前后端
        httpSecurity
                .authorizeExchange()
                .pathMatchers(reSecurityProperties.getPassTokenUrl().toArray(new String[]{}))
                .permitAll()
                .and()
                .authorizeExchange()
                .anyExchange()
                .denyAll();
        return httpSecurity.build();
    }
}
