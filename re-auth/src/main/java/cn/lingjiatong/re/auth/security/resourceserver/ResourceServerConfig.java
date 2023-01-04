package cn.lingjiatong.re.auth.security.resourceserver;

import cn.lingjiatong.re.auth.security.AuthenticationHandler;
import cn.lingjiatong.re.auth.security.ReSecurityProperties;
import cn.lingjiatong.re.common.annotation.PassToken;
import cn.lingjiatong.re.common.util.SpringBeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.util.CollectionUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * 资源服务器配置
 *
 * @author Ling, Jiatong
 * Date: 2022/12/31 18:53
 */
@Configuration
@EnableResourceServer
@EnableGlobalMethodSecurity(prePostEnabled = true, jsr250Enabled = true)
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Autowired
    private ReSecurityProperties reSecurityProperties;
    @Autowired
    @Qualifier("authenticationHandler")
    private AuthenticationHandler authenticationHandler;
    @Autowired
    private ResourceTokenService resourceTokenService;

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
    public void configure(HttpSecurity http) throws Exception {
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
                .antMatchers(reSecurityProperties.getPassTokenUrl().toArray(new String[]{}))
                .permitAll()
                // 其他请求均需要token才能访问
                .and()
                .authorizeRequests()
                .anyRequest()
                .authenticated()
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(authenticationHandler)
                .accessDeniedHandler(authenticationHandler);

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