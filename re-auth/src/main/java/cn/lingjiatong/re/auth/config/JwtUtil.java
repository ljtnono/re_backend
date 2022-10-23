package cn.lingjiatong.re.auth.config;

import cn.lingjiatong.re.common.constant.CommonConstant;
import cn.lingjiatong.re.common.constant.RedisCacheKeyEnum;
import cn.lingjiatong.re.common.entity.User;
import cn.lingjiatong.re.common.entity.cache.UserInfoCache;
import cn.lingjiatong.re.common.exception.ErrorEnum;
import cn.lingjiatong.re.common.exception.PermissionException;
import cn.lingjiatong.re.common.util.EncryptUtil;
import cn.lingjiatong.re.common.util.RedisUtil;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * jwt工具类
 *
 * @author Ling, Jiatong
 * Date: 2020/7/8 8:43 上午
 */
@Slf4j
@Component
public class JwtUtil {

    @Autowired
    private RedisUtil redisUtil;

    /**
     * 根据userId、username、email、phone生成token
     *
     * @param userId   用户id
     * @param username 用户名
     * @param email    用户邮箱
     * @param phone    用户手机号
     * @return 生成的token
     */
    public String generateToken(Integer userId, String username, String email, String phone) {
        // token应该尽可能保证数据无关性，避免token泄漏用户信息
        Map<String, Object> claims = new HashMap<>(2);
        claims.put("userId", userId);
        claims.put("username", username);
        claims.put("X-TOKEN", EncryptUtil.getInstance().getMd5LowerCase(userId + username + email + phone));
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(generateExpiration())
                .signWith(SignatureAlgorithm.HS256, CommonConstant.TOKEN_SECRET_SALT).compact();
    }

    /**
     * 生成过期时间
     * 默认为8小时过期（一般人的工作时间）
     *
     * @return 过期时间
     */
    private Date generateExpiration() {
        return new Date(System.currentTimeMillis() + ((long) 8 * 60 * 60 * 1000));
    }

    /**
     * 判断token是否过期
     *
     * @param token token
     * @return 过期返回false, 未过期返回true
     * @throws NullPointerException 当token为null或空串时抛出
     */
    public boolean isTokenExpired(String token) {
        Claims claims = getClaimsFromToken(token);
        Date expiration = claims.getExpiration();
        return expiration.before(new Date());
    }

    /**
     * 从token获取Claims
     *
     * @param token token
     * @return token中的Claims键值对信息，当token不符合格式时返回null
     * @see JwtParser#parseClaimsJws(String)  在解析token时会抛出各种异常，具体见此方法
     */
    public Claims getClaimsFromToken(String token) {
        if (!StringUtils.hasLength(token)) {
            throw new PermissionException(ErrorEnum.USER_NOT_AUTHTICATE_ERROR);
        }
        JwtParser parser = Jwts.parser();
        try {
            return parser.setSigningKey(CommonConstant.TOKEN_SECRET_SALT)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (MalformedJwtException | UnsupportedJwtException | SignatureException e ) {
            log.error("==========token格式错误，异常：{}", e.toString());
            throw new PermissionException(ErrorEnum.TOKEN_ILLEGAL_FORMAT_ERROR);
        } catch (ExpiredJwtException e) {
            log.error("==========token已过期，异常：{}", e.toString());
            throw new PermissionException(ErrorEnum.TOKEN_EXPIRED_ERROR);
        }
    }

    /**
     * 验证token是否合法
     *
     * @param token token
     * @param user  user
     * @return 合法返回true, 不合法返回false
     */
    public boolean validateToken(String token, User user) {
        // token校验方式  这里只校验了用户名
        String username = getUsernameFromToken(token);
        Object cache = redisUtil.getCacheObject(RedisCacheKeyEnum.USER_INFO.getValue() + user.getId());
        if (cache == null) {
            return false;
        }
        UserInfoCache userInfoCache = (UserInfoCache) cache;
        if (!userInfoCache.getToken().equals(token)) {
            return false;
        }
        if (username != null && username.equals(user.getUsername()) && !isTokenExpired(token)) {
            return true;
        }
        return true;
    }


    /**
     * 根据token解析出token中的username
     *
     * @param token 令牌
     * @return username, 如果解析失败那么返回null
     */
    public String getUsernameFromToken(String token) {
        Claims claimsFromToken = getClaimsFromToken(token);
        return claimsFromToken.get("username").toString();
    }
}
