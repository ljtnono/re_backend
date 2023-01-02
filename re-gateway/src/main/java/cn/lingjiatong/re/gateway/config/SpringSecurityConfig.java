package cn.lingjiatong.re.gateway.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

/**
 * 安全策略配置，网关层面全部放行，使用网关的过滤器进行请求拦截
 *
 * @author Ling, Jiatong
 * Date: 2022/11/2 22:45
 */
@EnableWebFluxSecurity
public class SpringSecurityConfig  {

    @Autowired
    private AuthenticationHandler authenticationHandler;

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity httpSecurity) {
        httpSecurity.csrf().disable();
        httpSecurity.headers().cache().disable();
        httpSecurity.headers().contentSecurityPolicy("default-src 'self'");

        // 全部放行，全部使用gateway的过滤器进行处理
        httpSecurity
                .authorizeExchange()
                .pathMatchers("/**")
                .permitAll()
                .and()
                .exceptionHandling()
                .accessDeniedHandler(authenticationHandler)
                .authenticationEntryPoint(authenticationHandler);
        return httpSecurity.build();
    }
}
