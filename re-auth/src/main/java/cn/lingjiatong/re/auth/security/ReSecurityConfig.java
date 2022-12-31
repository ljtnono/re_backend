package cn.lingjiatong.re.auth.security;

import cn.lingjiatong.re.common.annotation.PassToken;
import cn.lingjiatong.re.common.util.SpringBeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.util.CollectionUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

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
    private ReSecurityProperties reSecurityProperties;
    @Autowired
    @Qualifier("authenticationHandler")
    private AuthenticationHandler authenticationHandler;

    public Set<String> getPassTokenUrl() {
        Set<String> passTokenSet = new TreeSet<>();
        RequestMappingHandlerMapping handlerMapping = SpringBeanUtil.getBean("requestMappingHandlerMapping", RequestMappingHandlerMapping.class);
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = handlerMapping.getHandlerMethods();
        for (Map.Entry<RequestMappingInfo, HandlerMethod> entry : handlerMethods.entrySet()) {
            HandlerMethod value = entry.getValue();
            boolean b = value.hasMethodAnnotation(PassToken.class);
            if (b) {
                passTokenSet.addAll(entry.getKey().getPatternsCondition().getPatterns());
            }
        }
        return passTokenSet;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        Set<String> passTokenUrl = getPassTokenUrl();
        // 配置不使用 session
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        // 禁用缓存
        http.headers().cacheControl();
        // 设置安全策略
        http.headers().contentSecurityPolicy("default-src 'self'");

        // 配置拦截规则
        if (!CollectionUtils.isEmpty(passTokenUrl)) {
            http.authorizeRequests()
                    .antMatchers(passTokenUrl.toArray(new String[0]))
                    .permitAll();
        }

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
