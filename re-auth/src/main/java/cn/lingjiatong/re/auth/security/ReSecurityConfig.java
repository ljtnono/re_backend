package cn.lingjiatong.re.auth.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

/**
 * spring security 配置
 *
 * @author Ling, Jiatong
 * Date: 2020/7/7 20:36 下午
 */
@Configuration
@EnableWebSecurity
public class ReSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    @Qualifier("authenticationHandler")
    private AuthenticationHandler authenticationHandler;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 配置不使用 session
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        // 禁用缓存
        http.headers().cacheControl();
        // 设置安全策略
        http.headers().contentSecurityPolicy("default-src 'self';img-src 'self' data:;style-src 'self' 'unsafe-inline';");

        // 全部放行，使用ResourceServer的安全过滤器链进行处理
        http.authorizeRequests()
                .anyRequest()
                .permitAll()
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(authenticationHandler)
                .accessDeniedHandler(authenticationHandler);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

}
