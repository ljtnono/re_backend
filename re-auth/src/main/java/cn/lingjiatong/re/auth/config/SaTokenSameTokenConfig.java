package cn.lingjiatong.re.auth.config;

import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.filter.SaServletFilter;
import cn.dev33.satoken.same.SaSameUtil;
import cn.lingjiatong.re.common.ResultVO;
import cn.lingjiatong.re.common.exception.ErrorEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Sa-Token Same-Token 校验配置类
 *
 * 作用：校验请求是否来自 Gateway 转发（携带有效的 Same-Token）
 * 注意：登录、验证码等公开接口已在 Gateway 白名单中，不会走到这里
 *
 * @author Ling, Jiatong
 */
@Slf4j
@Configuration
public class SaTokenSameTokenConfig {

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
                        // 以下接口在Gateway白名单中，但实际调用时可能也会进入此处
                        // 所以一并排除
                        "/user/login",
                        "/user/refreshVerifyCode"
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
