package cn.lingjiatong.re.service.sys.security;

import cn.lingjiatong.re.common.exception.ErrorEnum;
import cn.lingjiatong.re.common.exception.PermissionException;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.KeyPair;

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
    private KeyPair keyPair;

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
            return parser.setSigningKey(keyPair.getPrivate())
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
