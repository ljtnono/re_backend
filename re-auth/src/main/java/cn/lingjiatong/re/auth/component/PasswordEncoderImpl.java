package cn.lingjiatong.re.auth.component;

import cn.lingjiatong.re.common.util.EncryptUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * 加密器
 * 默认使用md5对密码加密处理
 *
 * @author Ling, Jiatong
 * Date: 2020/7/7 20:36 下午
 */
@Component
public class PasswordEncoderImpl implements PasswordEncoder {

    @Override
    public String encode(CharSequence charSequence) {
        return EncryptUtil.getInstance().getMd5LowerCase((String) charSequence);
    }

    @Override
    public boolean matches(CharSequence charSequence, String s) {
        return s.equals(EncryptUtil.getInstance().getMd5LowerCase((String) charSequence));
    }
}
