package cn.lingjiatong.re.gateway.filter;

import cn.dev33.satoken.same.SaSameUtil;
import cn.dev33.satoken.stp.StpUtil;
import cn.lingjiatong.re.common.constant.CommonConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 认证转发过滤器
 *
 * 1. 将用户的 Authorization token 透传给下游微服务
 * 2. 附加 Same-Token 用于微服务间安全校验
 * 3. 将当前登录用户信息写入请求头，供下游服务使用
 *
 * @author Ling, Jiatong
 * Date: 2025/10/21 00:30
 */
@Slf4j
@Component
public class ForwardAuthFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpRequest.Builder requestBuilder = request.mutate();

        // 1. 透传用户原始的 Authorization token
        String userToken = request.getHeaders().getFirst(CommonConstant.TOKE_HTTP_HEADER);
        if (StringUtils.hasLength(userToken)) {
            requestBuilder.header(CommonConstant.TOKE_HTTP_HEADER, userToken);
        }

        // 2. 附加 Same-Token 用于微服务间安全校验
        requestBuilder.header(SaSameUtil.SAME_TOKEN, SaSameUtil.getToken());

        // 3. 将当前登录用户信息写入自定义请求头，避免下游服务重复解析 JWT
        try {
            if (StpUtil.isLogin()) {
                Object loginId = StpUtil.getLoginId();
                if (loginId != null) {
                    requestBuilder.header("X-User-Username", loginId.toString());
                }
                // 尝试从 Session 中获取 userId 并写入请求头
                Object userIdObj = StpUtil.getSession().get("userId");
                if (userIdObj != null) {
                    requestBuilder.header("X-User-Id", userIdObj.toString());
                }
            }
        } catch (Exception e) {
            log.debug("获取当前登录用户信息失败: {}", e.getMessage());
        }

        ServerHttpRequest newRequest = requestBuilder.build();
        ServerWebExchange newExchange = exchange.mutate().request(newRequest).build();
        return chain.filter(newExchange);
    }

    @Override
    public int getOrder() {
        // 确保在 SaReactorFilter 之后执行
        return -100;
    }
}
