package cn.lingjiatong.re.auth.component;

import cn.lingjiatong.re.auth.constant.AuthErrorMessageConstant;
import cn.lingjiatong.re.common.constant.RedisCacheKeyEnum;
import cn.lingjiatong.re.common.entity.cache.LoginVerifyCodeCache;
import cn.lingjiatong.re.common.exception.BusinessException;
import cn.lingjiatong.re.common.exception.ErrorEnum;
import cn.lingjiatong.re.common.exception.ParamErrorException;
import cn.lingjiatong.re.common.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.token.AbstractTokenGranter;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.util.StringUtils;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 自定义验证码授权
 *
 * @author Ling, Jiatong
 * Date: 2022/10/25 01:00
 */
@Slf4j
public class VerifyCodeTokenGranter extends AbstractTokenGranter {

    private static final String GRANT_TYPE = "verify_code";
    private AuthenticationManager authenticationManager;
    private RedisUtil redisUtil;

    public VerifyCodeTokenGranter(AuthorizationServerTokenServices tokenServices, ClientDetailsService clientDetailsService, OAuth2RequestFactory requestFactory, AuthenticationManager authenticationManager, RedisUtil redisUtil) {
        super(tokenServices, clientDetailsService, requestFactory, GRANT_TYPE);
        this.authenticationManager = authenticationManager;
        this.redisUtil = redisUtil;
    }

    @Override
    protected OAuth2Authentication getOAuth2Authentication(ClientDetails client, TokenRequest tokenRequest) {
        Map<String, String> parameters = new LinkedHashMap<>(tokenRequest.getRequestParameters());

        // 验证码校验逻辑
        String verifyCodeValue = parameters.get("verifyCode");
        String verifyCodeKey = parameters.get("verifyCodeKey");
        String username = parameters.get("username");
        String password = parameters.get("password");

        // 这里不再校验用户名密码的严格格式，只校验是否为空，因为此处用户名和密码已经录入数据库
        if (!StringUtils.hasLength(username)) {
            throw new ParamErrorException(ErrorEnum.REQUEST_PARAM_ERROR.getCode(), AuthErrorMessageConstant.USERNAME_EMPTY_ERROR_MESSAGE);
        }
        if (!StringUtils.hasLength(password)) {
            throw new ParamErrorException(ErrorEnum.REQUEST_PARAM_ERROR.getCode(), AuthErrorMessageConstant.PASSWORD_EMPTY_ERROR_MESSAGE);
        }
        if (!StringUtils.hasLength(verifyCodeValue)) {
            throw new ParamErrorException(ErrorEnum.REQUEST_PARAM_ERROR.getCode(), AuthErrorMessageConstant.VERIFY_CODE_VALUE_EMPTY_ERROR_MESSAGE);
        }
        // 从redis查找验证码key
        LoginVerifyCodeCache cache = (LoginVerifyCodeCache) redisUtil.getCacheObject(RedisCacheKeyEnum.LOGIN_VERIFY_CODE.getValue() + verifyCodeKey);
        Optional.ofNullable(cache)
                .orElseThrow(() -> new BusinessException(ErrorEnum.LOGIN_VERIFY_CODE_EXPIRED_ERROR));
        // 验证码校验错误
        if (!verifyCodeValue.equalsIgnoreCase(cache.getValue())) {
            throw new ParamErrorException(ErrorEnum.REQUEST_PARAM_ERROR.getCode(), AuthErrorMessageConstant.VERIFY_CODE_ERROR_MESSAGE);
        }

        // 验证码验证通过，删除 Redis 的验证码
//        redisTemplate.delete(key);

        // 和密码模式一样的逻辑
        Authentication userAuth = new UsernamePasswordAuthenticationToken(username, password);
        ((AbstractAuthenticationToken) userAuth).setDetails(parameters);

        try {
            // 此过程中会调用UserDetailService中的loadUserByUsername方法
            userAuth = authenticationManager.authenticate(userAuth);
            OAuth2Request storedOAuth2Request = this.getRequestFactory().createOAuth2Request(client, tokenRequest);
            return new OAuth2Authentication(storedOAuth2Request, userAuth);
        } catch (Exception e) {
            log.error("==========认证失败");
            log.error(e.getMessage(), e);
            throw e;
        }
    }

}
