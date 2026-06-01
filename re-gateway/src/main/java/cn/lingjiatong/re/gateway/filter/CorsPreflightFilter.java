package cn.lingjiatong.re.gateway.filter;

import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * CORS 全局响应头过滤器
 *
 * 最高优先级，为所有响应添加 Access-Control-Allow-* 头，
 * OPTIONS 预检请求直接返回 200，避免被下游 SaReactorFilter 拦截。
 *
 * @author Ling, Jiatong
 */
@Component
public class CorsPreflightFilter implements WebFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpResponse response = exchange.getResponse();
        HttpHeaders headers = response.getHeaders();

        headers.add("Access-Control-Allow-Origin", "*");
        headers.add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD, PATCH");
        headers.add("Access-Control-Allow-Headers", "*");
        headers.add("Access-Control-Allow-Credentials", "true");
        headers.add("Access-Control-Max-Age", "3600");
        headers.add("Access-Control-Expose-Headers", "Content-Disposition, Content-Type, Cache-Control, Authorization");

//        if ("OPTIONS".equalsIgnoreCase(exchange.getRequest().getMethodValue())) {
//            response.setStatusCode(HttpStatus.OK);
//            return Mono.empty();
//        }

        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
