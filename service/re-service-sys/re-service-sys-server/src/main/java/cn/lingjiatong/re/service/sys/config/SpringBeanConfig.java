package cn.lingjiatong.re.service.sys.config;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * spring bean配置类
 *
 * @author Ling, Jiatong
 * Date: 2022/9/19 16:59
 */
@Configuration
@EnableSwagger2
public class SpringBeanConfig {

    @Bean
    @Lazy
    public MybatisPlusInterceptor paginationInterceptor() {
        MybatisPlusInterceptor mybatisPlusInterceptor = new MybatisPlusInterceptor();
        PaginationInnerInterceptor paginationInnerInterceptor = new PaginationInnerInterceptor();
        mybatisPlusInterceptor.addInnerInterceptor(paginationInnerInterceptor);
        return mybatisPlusInterceptor;
    }

}
