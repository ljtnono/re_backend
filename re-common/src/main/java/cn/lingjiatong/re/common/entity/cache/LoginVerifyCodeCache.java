package cn.lingjiatong.re.common.entity.cache;

import lombok.Data;

/**
 * 登录验证码缓存实体
 *
 * @author Ling, Jiatong
 * Date: 2022/10/23 12:18
 */
@Data
public class LoginVerifyCodeCache {

    /**
     * 验证码值
     */
    private String value;

}
