package cn.lingjiatong.re.api.backend.config;

import cn.lingjiatong.re.common.constant.CommonConstant;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootVersion;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
    public OpenAPI docket() {
        Components components = new Components();
        components.addSecuritySchemes(CommonConstant.TOKE_HTTP_HEADER, new SecurityScheme().name(CommonConstant.TOKE_HTTP_HEADER).type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT"));
        return new OpenAPI()
                .components(components)
                .addSecurityItem(new SecurityRequirement().addList(CommonConstant.TOKE_HTTP_HEADER))
                .info(info());
    }

    /**
     * 创建该API的基本信息（这些基本信息会展现在文档页面中）
     * 访问地址：http://项目实际地址/swagger-ui.html
     *
     * @return Info
     */
    private Info info() {
        Contact contact = new Contact();
        contact.setName("lingjiatong");
        contact.setEmail("935188400@qq.com");
        return new Info()
                .title(swaggerProperties.getApplicationName())
                .description(swaggerProperties.getApplicationDescription())
                .contact(contact)
                .version("Application Version: " + swaggerProperties.getApplicationVersion() + ", Spring Boot Version: " + SpringBootVersion.getVersion());
    }

}
