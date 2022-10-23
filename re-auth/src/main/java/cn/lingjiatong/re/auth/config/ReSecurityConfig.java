package cn.lingjiatong.re.auth.config;

import cn.lingjiatong.re.auth.component.AuthenticationHandler;
import cn.lingjiatong.re.auth.component.TokenFilter;
import cn.lingjiatong.re.common.annotation.PassToken;
import cn.lingjiatong.re.common.util.SpringBeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
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
@EnableWebSecurity
@EnableGlobalMethodSecurity(jsr250Enabled = true, securedEnabled = true, prePostEnabled = true, proxyTargetClass = true)
public class ReSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userService;
    private final PasswordEncoder passwordEncoder;
    private final TokenFilter tokenFilter;
    private final AuthenticationHandler authenticationHandler;
    private final ReSecurityProperties reSecurityProperties;

    public ReSecurityConfig(ReSecurityProperties reSecurityProperties, PasswordEncoder passwordEncoder, TokenFilter tokenFilter, AuthenticationHandler authenticationHandler) {
        this.reSecurityProperties = reSecurityProperties;
        this.passwordEncoder = passwordEncoder;
        this.tokenFilter = tokenFilter;
        this.authenticationHandler = authenticationHandler;
    }

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
        // 解决不允许显示在iframe的问题
//        http.headers().frameOptions().deny();
        // 禁用缓存
        http.headers().cacheControl();
        // 设置安全策略
        http.headers().contentSecurityPolicy("default-src 'self'");
        // 配置拦截规则
        if (!CollectionUtils.isEmpty(reSecurityProperties.getPassTokenUrl())) {
            http.authorizeRequests()
                    .antMatchers(reSecurityProperties.getPassTokenUrl().toArray(new String[0]))
                    .permitAll();
        }
        http
                .authorizeRequests()
                .antMatchers(passTokenUrl.toArray(new String[0]))
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .exceptionHandling()
                .accessDeniedHandler(authenticationHandler)
                .authenticationEntryPoint(authenticationHandler);
        // AuthenticationEntryPoint 用来解决匿名用户访问无权限资源时的异常
        // AccessDeineHandler 用来解决认证过的用户访问无权限资源时的异常
        // 配置token拦截
        http.addFilterBefore(tokenFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userService)
                .passwordEncoder(passwordEncoder);
    }

}
