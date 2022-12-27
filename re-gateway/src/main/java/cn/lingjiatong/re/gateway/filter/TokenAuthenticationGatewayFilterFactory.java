package cn.lingjiatong.re.gateway.filter;

import cn.lingjiatong.re.common.constant.CommonConstant;
import cn.lingjiatong.re.common.exception.ErrorEnum;
import cn.lingjiatong.re.common.exception.PermissionException;
import cn.lingjiatong.re.common.exception.ServerException;
import cn.lingjiatong.re.gateway.component.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpCookie;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * 认证过滤器
 *
 * 本过滤器只校验请求是否携带Token和token是否过期，鉴权的流程放在各个微服务自己来做
 * 如果携带的token没有过期则进入下一个过滤器
 * 如果没有携带token或者token过期则直接返回错误消息
 *
 * @author Ling, Jiatong
 * Date: 2022/10/23 13:07
 */
@Component
public class TokenAuthenticationGatewayFilterFactory extends AbstractGatewayFilterFactory<Object> {

    @Autowired
    private JwtUtil jwtUtil;
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public GatewayFilter apply(Object config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            MultiValueMap<String, String> queryParams = request.getQueryParams();
            MultiValueMap<String, HttpCookie> cookies = request.getCookies();
            String username = null;
            String token = request.getHeaders().getFirst(CommonConstant.TOKE_HTTP_HEADER);
            if (StringUtils.hasLength(token) && token.startsWith(CommonConstant.TOKEN_PREFIX)) {
                token = token.substring(CommonConstant.TOKEN_PREFIX.length());
                // 从token中解析出来用户名
                try {
                    username = jwtUtil.getUsernameFromToken(token);
                } catch (PermissionException e) {
                    // 返回错误消息
                    return responseInfo(exchange, e.getCode(), e.getMessage());
                }
            }
            // 如果header中没有，那么从url中取
            if (!CollectionUtils.isEmpty(queryParams) && StringUtils.hasLength(queryParams.getFirst(CommonConstant.TOKE_HTTP_HEADER))) {
                token = URLDecoder.decode(queryParams.getFirst(CommonConstant.TOKE_HTTP_HEADER).substring(CommonConstant.TOKEN_PREFIX.length()), StandardCharsets.UTF_8);
                // 从token中解析出来用户名
                try {
                    username = jwtUtil.getUsernameFromToken(token);
                } catch (PermissionException e) {
                    // 返回错误消息
                    return responseInfo(exchange, e.getCode(), e.getMessage());
                }
            }
            // 如果url中也没有，那么从cookie中取
            if (!CollectionUtils.isEmpty(cookies) && !CollectionUtils.isEmpty(cookies.get(CommonConstant.TOKEN_COKKIE_HEADER))) {
                String value = cookies.getFirst(CommonConstant.TOKEN_COKKIE_HEADER).getValue();
                token = URLDecoder.decode(value.substring(CommonConstant.TOKEN_PREFIX.length()), StandardCharsets.UTF_8);
                // 从token中解析出来用户名
                try {
                    username = jwtUtil.getUsernameFromToken(token);
                } catch (PermissionException e) {
                    // 返回错误消息
                    return responseInfo(exchange, e.getCode(), e.getMessage());
                }
            }

            // TODO 如何放行passTokenUrl中匹配的路径
            if (StringUtils.hasLength(token) && StringUtils.hasLength(username)) {
                // 说明解析成功，进入下一个过滤器
                return chain.filter(exchange);
            } else {
                // 解析失败，返回失败消息
                return responseInfo(exchange, ErrorEnum.USER_NOT_AUTHENTICATE_ERROR.getCode(), ErrorEnum.USER_NOT_AUTHENTICATE_ERROR.getMessage());
            }
        };
    }

    /**
     * 自定义消息返回
     *
     * @param exchange ServerWebExchange
     * @param message  异常信息
     * @param code     异常码
     * @return Mono
     */
    public static Mono<Void> responseInfo(ServerWebExchange exchange, Integer code, String message) {
        // 自定义返回格式
        Map<String, Object> resultMap = new HashMap<>(2);
        resultMap.put("code", code);
        resultMap.put("message", message);
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
