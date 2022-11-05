package cn.lingjiatong.re.api.frontend.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.stereotype.Component;

/**
 * 自定义token解析认证服务
 *
 * @author Ling, Jiatong
 * Date: 2022/11/4 15:13
 */
@Slf4j
@Component
public class ResourceTokenService implements ResourceServerTokenServices {

    @Autowired
    private JwtTokenStore jwtTokenStore;

    @Override
    public OAuth2Authentication loadAuthentication(String accessToken) throws AuthenticationException, InvalidTokenException {
        // 配置如何从token生成OAuth2Authentication
        return jwtTokenStore.readAuthentication(accessToken);
    }

    @Override
    public OAuth2AccessToken readAccessToken(String accessToken) {
        throw new UnsupportedOperationException("暂不支持 readAccessToken!");
    }
}
