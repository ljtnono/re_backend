package cn.lingjiatong.re.auth.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
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
@EnableWebSecurity
public class ReSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private ReSecurityProperties reSecurityProperties;
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
        http.headers().contentSecurityPolicy("default-src 'self'");

        http
                .authorizeRequests()
                // 放行所有oauth2端点、公钥接口
                .antMatchers(reSecurityProperties.getPassTokenUrl().toArray(new String[]{}))
                .permitAll()
                .and()
                .formLogin()
                .permitAll()
                .and()
                .authorizeRequests()
                .anyRequest()
                // 其他接口都需要认证
                .authenticated()
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
