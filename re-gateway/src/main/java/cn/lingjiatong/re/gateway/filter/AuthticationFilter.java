package cn.lingjiatong.re.gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;

/**
 * 认证过滤器
 *
 * @author Ling, Jiatong
 * Date: 2022/10/23 13:07
 */
@Slf4j
@Component
public class AuthticationFilter extends AbstractGatewayFilterFactory {

    @Override
    public GatewayFilter apply(Object config) {
        return null;
    }
}
