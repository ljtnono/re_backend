package cn.lingjiatong.re.gateway.filter;

import cn.dev33.satoken.same.SaSameUtil;
import cn.lingjiatong.re.common.constant.CommonConstant;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 认证转发
 *
 * @author Ling, Jiatong
 * Date: 2025/10/21 00:30
 */
@Component
public class ForwardAuthFilter implements GlobalFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest newRequest = exchange
                .getRequest()
                .mutate()
                .header(CommonConstant.TOKE_HTTP_HEADER, SaSameUtil.getToken())
                .build();
        ServerWebExchange newExchange = exchange.mutate().request(newRequest).build();
        return chain.filter(newExchange);
    }

}
