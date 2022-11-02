package cn.lingjiatong.re.gateway.config;

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

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity httpSecurity) {
        httpSecurity.csrf().disable();
        httpSecurity.headers().cache().disable();
        httpSecurity.headers().contentSecurityPolicy("default-src 'self'");
        httpSecurity.authorizeExchange().anyExchange().permitAll();
        return httpSecurity.build();
    }

    // ********************************新增类接口********************************
    // ********************************删除类接口********************************
    // ********************************修改类接口********************************
    // ********************************查询类接口********************************
    // ********************************私有函数********************************
    // ********************************公用函数********************************
}
