package cn.lingjiatong.re.gateway.filter;

import cn.dev33.satoken.same.SaSameUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 认证转发过滤器
 * <p>
 * 1. 将用户的 Authorization token 透传给下游微服务
 * 2. 附加 Same-Token 用于微服务间安全校验
 * 3. 将当前登录用户信息写入请求头，供下游服务使用
 *
 * @author Ling, Jiatong
 * Date: 2025/10/21 00:30
 */
@Slf4j
@Component
public class ForwardAuthFilter implements GlobalFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest newRequest = exchange
                .getRequest()
                .mutate()
                // 为请求追加 Same-Token 参数
                .header(SaSameUtil.SAME_TOKEN, SaSameUtil.getToken())
                .build();
        ServerWebExchange newExchange = exchange.mutate().request(newRequest).build();
        return chain.filter(newExchange);
    }

}
