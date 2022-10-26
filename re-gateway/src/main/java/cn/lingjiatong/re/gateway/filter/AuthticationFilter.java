package cn.lingjiatong.re.gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 认证过滤器
 *
 * @author Ling, Jiatong
 * Date: 2022/10/23 13:07
 */
@Slf4j
@Component
public class AuthticationFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 校验逻辑
        return null;
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
