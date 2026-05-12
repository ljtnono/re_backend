package cn.lingjiatong.re.api.backend.config;

import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.filter.SaServletFilter;
import cn.dev33.satoken.same.SaSameUtil;
import cn.dev33.satoken.util.SaResult;
import cn.lingjiatong.re.common.ResultVO;
import cn.lingjiatong.re.common.exception.ErrorEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Sa-Token 权限配置类
 *
 * 1. 注册 Same-Token 校验过滤器，防止外部直接绕过 Gateway 调用内部服务
 * 2. 全局异常处理，统一返回 ResultVO 格式
 *
 * @author Ling, Jiatong
 */
@Slf4j
@Configuration
public class SaTokenConfig {

    /**
     * 注册 Same-Token 校验过滤器
     *
     * 作用：校验请求是否来自 Gateway 转发（携带有效的 Same-Token）
     * 注意：Swagger 文档、Actuator 监控等公开接口已在 Gateway 白名单中，不会走到这里
     */
    @Bean
    public SaServletFilter getSaServletFilter() {
        return new SaServletFilter()
                .addInclude("/**")
                // 排除不需要校验 Same-Token 的路径（如本地测试、Swagger等）
                .addExclude(
                        "/swagger-ui/**",
                        "/swagger-ui.html",
                        "/v3/api-docs/**",
                        "/swagger-resources/**",
                        "/webjars/**",
                        "/actuator/**",
                        "/actuator",
                        "/favicon.ico",
                        "/error"
                )
                .setAuth(obj -> {
                    // 校验 Same-Token，确保请求来自 Gateway 转发
                    String token = SaHolder.getRequest().getHeader(SaSameUtil.SAME_TOKEN);
                    SaSameUtil.checkToken(token);
                })
                .setError(e -> {
                    log.error("Same-Token校验失败: {}", e.getMessage());
                    return ResultVO.error(ErrorEnum.PERMISSION_DENIED_ERROR);
                });
    }
}
