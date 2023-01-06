package cn.lingjiatong.re.api.backend.config;

import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootVersion;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * spring bean配置类
 *
 * @author Ling, Jiatong
 * Date: 2023/1/6 10:03
 */
@Configuration
public class SpringBeanConfig {

    @Autowired
    private SwaggerProperties swaggerProperties;

    @Bean
    public Docket docket() {
        return new Docket(DocumentationType.OAS_30)
                // 是否开启
                .enable(swaggerProperties.getEnable())
                // 将api的元信息设置为包含在json ResourceListing响应中。
                .apiInfo(apiInfo())
                // 接口调试地址
                .host(swaggerProperties.getTryHost())
                .select()
                .apis(RequestHandlerSelectors.basePackage(swaggerProperties.getScanBasePackage()))
                .paths(PathSelectors.any())
                .build()
                .protocols(Set.of("https", "http"))
                // 授权信息设置，必要的header token等认证信息
                .securitySchemes(securitySchemes())
                // 授权信息全局应用
                .securityContexts(securityContexts());

    }

    /**
     * 创建该API的基本信息（这些基本信息会展现在文档页面中）
     * 访问地址：http://项目实际地址/swagger-ui.html
     *
     * @return ApiInfo
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title(swaggerProperties.getApplicationName())
                .description(swaggerProperties.getApplicationDescription())
                .contact(new Contact("lingjiatong", null, "935188400@qq.com"))
                .version("Application Version: " + swaggerProperties.getApplicationVersion() + ", Spring Boot Version: " + SpringBootVersion.getVersion())
                .build();
    }

    /**
     * 设置授权信息
     */
    private List<SecurityScheme> securitySchemes() {
        ApiKey apiKey = new ApiKey("Authorization", "token", In.HEADER.toValue());
        return Collections.singletonList(apiKey);
    }

    /**
     * 授权信息全局应用
     */
    private List<SecurityContext> securityContexts() {
        return Collections.singletonList(SecurityContext.builder()
                .securityReferences(Collections.singletonList(new SecurityReference("Authorization", new AuthorizationScope[]{new AuthorizationScope("global", "")})))
                .build());
    }

}
