package cn.lingjiatong.re.gateway.filter;

import cn.dev33.satoken.same.SaSameUtil;
import cn.dev33.satoken.stp.StpUtil;
import cn.lingjiatong.re.common.constant.CommonConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
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
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpRequest.Builder requestBuilder = request.mutate();

        // 1. 透传用户原始的 Authorization token
        String authHeader = request.getHeaders().getFirst(CommonConstant.TOKE_HTTP_HEADER);
        if (StringUtils.hasLength(authHeader)) {
            requestBuilder.header(CommonConstant.TOKE_HTTP_HEADER, authHeader);
            // 2. 从JWT解析用户名写入请求头，供下游 SaUserUtils 使用
            try {
                String token = authHeader.startsWith(CommonConstant.TOKEN_PREFIX)
                        ? authHeader.substring(CommonConstant.TOKEN_PREFIX.length())
                        : authHeader;
                Object loginId = StpUtil.getLoginIdByToken(token);
                if (loginId != null) {
                    requestBuilder.header("X-User-Username", loginId.toString());
                }
            } catch (Exception e) {
                log.debug("解析JWT获取loginId失败: {}", e.getMessage());
            }
        }

        // 3. 附加 Same-Token 用于微服务间安全校验
        requestBuilder.header(SaSameUtil.SAME_TOKEN, SaSameUtil.getToken());

        ServerHttpRequest newRequest = requestBuilder.build();
        ServerWebExchange newExchange = exchange.mutate().request(newRequest).build();
        return chain.filter(newExchange);
    }

}
