package cn.lingjiatong.re.service.article.config;

import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.filter.SaServletFilter;
import cn.dev33.satoken.same.SaSameUtil;
import cn.lingjiatong.re.common.ResultVO;
import cn.lingjiatong.re.common.exception.ErrorEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Sa-Token 权限配置类
 *
 * @author Ling, Jiatong
 */
@Slf4j
@Configuration
public class SaTokenConfig {

    @Bean
    public SaServletFilter getSaServletFilter() {
        return new SaServletFilter()
                .addInclude("/**")
                .addExclude(
                        "/swagger-ui/**",
                        "/swagger-ui.html",
                        "/v3/api-docs/**",
                        "/swagger-resources/**",
                        "/webjars/**",
                        "/actuator/**",
                        "/actuator",
                        "/favicon.ico",
                        "/error",
                        "/**"
                )
                .setAuth(obj -> {
                    String token = SaHolder.getRequest().getHeader(SaSameUtil.SAME_TOKEN);
                    SaSameUtil.checkToken(token);
                })
                .setError(e -> {
                    log.error("Same-Token校验失败: {}", e.getMessage());
                    return ResultVO.error(ErrorEnum.PERMISSION_DENIED_ERROR);
                });
    }
}
