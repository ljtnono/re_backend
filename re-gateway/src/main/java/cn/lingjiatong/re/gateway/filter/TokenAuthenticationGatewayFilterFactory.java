package cn.lingjiatong.re.gateway.filter;

import cn.lingjiatong.re.common.constant.CommonConstant;
import cn.lingjiatong.re.common.constant.RedisCacheKeyEnum;
import cn.lingjiatong.re.common.entity.cache.UserInfoCache;
import cn.lingjiatong.re.common.exception.ErrorEnum;
import cn.lingjiatong.re.common.exception.PermissionException;
import cn.lingjiatong.re.common.exception.ServerException;
import cn.lingjiatong.re.common.util.PathMatcherUtil;
import cn.lingjiatong.re.common.util.RedisUtil;
import cn.lingjiatong.re.gateway.component.JwtUtil;
import cn.lingjiatong.re.gateway.config.ReSecurityProperties;
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
import org.springframework.util.AntPathMatcher;
import org.springframework.util.CollectionUtils;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

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
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private ReSecurityProperties reSecurityProperties;

    @Override
    public GatewayFilter apply(Object config) {
        // 获取当前
        List<String> passTokenUrl = reSecurityProperties.getPassTokenUrl();
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            MultiValueMap<String, String> queryParams = request.getQueryParams();
            MultiValueMap<String, HttpCookie> cookies = request.getCookies();
            String username = null;
            String token = request.getHeaders().getFirst(CommonConstant.TOKE_HTTP_HEADER);
            if (!CollectionUtils.isEmpty(passTokenUrl)) {
                // passTokenUrl中匹配的路径
                AtomicBoolean atomicBoolean = new AtomicBoolean(Boolean.FALSE);
                passTokenUrl.stream().anyMatch(passTokenPattern -> {
                    String reqPath = request.getPath().toString();
                    if (PathMatcherUtil.getInstance().match(new AntPathMatcher(), passTokenPattern, reqPath)) {
                        // 说明解析成功，进入下一个过滤器
                        atomicBoolean.set(Boolean.TRUE);
                        return true;
                    }
                    return false;
                });
                if (atomicBoolean.get()) {
                    // 跳过验证Token，进入下一个过滤器
                    return chain.filter(exchange);
                }
            }

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
            // 如果url中也没有，那么从cookie中取, 此处因为已经禁用
            if (!CollectionUtils.isEmpty(cookies) && !CollectionUtils.isEmpty(cookies.get(CommonConstant.TOKEN_COOKIE_HEADER))) {
                String value = URLDecoder.decode(cookies.getFirst(CommonConstant.TOKEN_COOKIE_HEADER).getValue(), StandardCharsets.UTF_8);
                token = URLDecoder.decode(value.substring(CommonConstant.TOKEN_PREFIX.length()), StandardCharsets.UTF_8);
                // 从token中解析出来用户名
                try {
                    username = jwtUtil.getUsernameFromToken(token);
                } catch (PermissionException e) {
                    // 返回错误消息
                    return responseInfo(exchange, e.getCode(), e.getMessage());
                }
            }
            if (StringUtils.hasLength(token) && StringUtils.hasLength(username)) {
                if (!StringUtils.hasLength(request.getHeaders().getFirst(CommonConstant.TOKE_HTTP_HEADER))) {
                    // 统一将token设置到请求头中去
                    ServerHttpRequest.Builder requestBuilder = request.mutate();
                    requestBuilder.header(CommonConstant.TOKE_HTTP_HEADER, CommonConstant.TOKEN_PREFIX + token);
                    exchange = exchange.mutate().request(requestBuilder.build()).build();
                }
                // 查询redis，如果redis中不存在则返回错误
                UserInfoCache userInfoCache = (UserInfoCache) redisUtil.getCacheObject(RedisCacheKeyEnum.USER_INFO.getValue() + username);
                if (null == userInfoCache) {
                    // 用户已注销，返回失败消息
                    return responseInfo(exchange, ErrorEnum.USER_ALREADY_LOGOUT_ERROR.getCode(), ErrorEnum.USER_ALREADY_LOGOUT_ERROR.getMessage());
                }
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
