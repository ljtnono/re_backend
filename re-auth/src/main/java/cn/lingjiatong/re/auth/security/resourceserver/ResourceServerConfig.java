package cn.lingjiatong.re.auth.security.resourceserver;

import cn.lingjiatong.re.auth.security.ReSecurityProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

/**
 * 资源服务器配置
 *
 * @author Ling, Jiatong
 * Date: 2022/12/31 18:53
 */
@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Autowired
    private ReSecurityProperties reSecurityProperties;

    // ********************************新增类接口********************************
    // ********************************删除类接口********************************
    // ********************************修改类接口********************************
    // ********************************查询类接口********************************

    @Override
    public void configure(HttpSecurity http) throws Exception {
        // 配置不使用 session
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        // 禁用缓存
        http.headers().cacheControl();
        // 设置安全策略
        http.headers().contentSecurityPolicy("default-src 'self'");
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

}
