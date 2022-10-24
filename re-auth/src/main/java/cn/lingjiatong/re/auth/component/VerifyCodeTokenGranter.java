package cn.lingjiatong.re.auth.component;

import cn.lingjiatong.re.common.constant.RedisCacheKeyEnum;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.token.AbstractTokenGranter;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 短信验证码token处理类
 *
 * @author Ling, Jiatong
 * Date: 2022/10/25 01:00
 */
public class VerifyCodeTokenGranter extends AbstractTokenGranter {

    private static final String GRANT_TYPE = "verify_code";
    private AuthenticationManager authenticationManager;
    private RedisTemplate<String, Object> redisTemplate;

    public VerifyCodeTokenGranter(AuthorizationServerTokenServices tokenServices, ClientDetailsService clientDetailsService, OAuth2RequestFactory requestFactory, AuthenticationManager authenticationManager, RedisTemplate<String, Object> redisTemplate) {
        super(tokenServices, clientDetailsService, requestFactory, GRANT_TYPE);
        this.authenticationManager = authenticationManager;
        this.redisTemplate = redisTemplate;
    }

    @Override
    protected OAuth2Authentication getOAuth2Authentication(ClientDetails client, TokenRequest tokenRequest) {
        Map<String, String> parameters = new LinkedHashMap(tokenRequest.getRequestParameters());

        // 验证码校验逻辑
        String validateCode = parameters.get("code");
        String uuid = parameters.get("uuid");

        Assert.isTrue(!StringUtils.hasLength(validateCode), "验证码不能为空");
        String validateCodeKey = RedisCacheKeyEnum.LOGIN_VERIFY_CODE.getValue() + uuid;

        // 从缓存取出正确的验证码和用户输入的验证码比对
        String correctValidateCode = (String) redisTemplate.opsForValue().get(validateCodeKey);
        Assert.isTrue(!StringUtils.hasLength(correctValidateCode),"验证码已过期");
        Assert.isTrue(validateCode.equals(correctValidateCode),"您输入的验证码不正确");

        // 验证码验证通过，删除 Redis 的验证码
        redisTemplate.delete(validateCodeKey);

        String username = parameters.get("username");
        String password = parameters.get("password");

        // 移除后续无用参数
        parameters.remove("password");
        parameters.remove("code");
        parameters.remove("uuid");

        // 和密码模式一样的逻辑
        Authentication userAuth = new UsernamePasswordAuthenticationToken(username, password);
        ((AbstractAuthenticationToken) userAuth).setDetails(parameters);

        try {
            userAuth = this.authenticationManager.authenticate(userAuth);
        } catch (AccountStatusException var8) {
            throw new InvalidGrantException(var8.getMessage());
        } catch (BadCredentialsException var9) {
            throw new InvalidGrantException(var9.getMessage());
        }

        if (userAuth != null && userAuth.isAuthenticated()) {
            OAuth2Request storedOAuth2Request = this.getRequestFactory().createOAuth2Request(client, tokenRequest);
            return new OAuth2Authentication(storedOAuth2Request, userAuth);
        } else {
            throw new InvalidGrantException("Could not authenticate user: " + username);
        }
    }

}
