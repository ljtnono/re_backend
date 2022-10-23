package cn.lingjiatong.re.auth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * mvc相关配置类
 *
 * @author Ling, Jiatong
 * Date: 2020/7/10
 */
@Configuration
@EnableWebMvc
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private LoginUserAnnotationResolver loginUserAnnotationResolver;

    /**
     * 配置controller方法参数处理器
     *
     * @param resolvers 方法参数处理器集合
     */
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(loginUserAnnotationResolver);
    }

}
