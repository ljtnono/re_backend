package cn.lingjiatong.re.gateway.config;

import cn.lingjiatong.re.common.exception.ErrorEnum;
import cn.lingjiatong.re.common.exception.ServerException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

/**
 * 授权处理
 *
 * @author Ling, Jiatong
 * Date: 2020/7/13 19:12 下午
 */
@Component("authenticationHandler")
public class AuthenticationHandler implements ServerAccessDeniedHandler, ServerAuthenticationEntryPoint {

    /**
     *  ServerAuthenticationEntryPoint 用来解决匿名用户访问无权限资源时的异常
     */
    @Override
    public Mono<Void> commence(ServerWebExchange exchange, AuthenticationException e) {
        return responseInfo(exchange, ErrorEnum.USER_NOT_AUTHENTICATE_ERROR);
    }

    /**
     *  ServerAccessDeniedHandler 用来解决认证过的用户访问无权限资源时的异常
     */
    @Override
    public Mono<Void> handle(ServerWebExchange exchange, AccessDeniedException denied) {
        return responseInfo(exchange, ErrorEnum.PERMISSION_DENIED_ERROR);
    }

    /**
     * 自定义消息返回
     *
     * @param exchange ServerWebExchange
     * @param errorEnum 异常枚举
     * @return Mono
     */
    public Mono<Void> responseInfo(ServerWebExchange exchange, ErrorEnum errorEnum) {
        // 自定义返回格式
        Map<String, Object> resultMap = new HashMap<>(2);
        resultMap.put("code", errorEnum.getCode());
        resultMap.put("message", errorEnum.getMessage());
        ObjectMapper objectMapper = new ObjectMapper();
        return Mono.defer(() -> {
            byte[] bytes;
            try {
                bytes = objectMapper.writeValueAsBytes(resultMap);
            } catch (Exception e) {
                throw new ServerException(ErrorEnum.UNKNOWN_ERROR);
            }
            ServerHttpResponse response = exchange.getResponse();
            response.getHeaders().add("Content-Type", MediaType.APPLICATION_JSON_UTF8.toString());
            DataBuffer buffer = response.bufferFactory().wrap(bytes);
            return response.writeWith(Flux.just(buffer));
        });
    }

}
