package cn.lingjiatong.re.common.constant;

import java.util.regex.Pattern;

/**
 * 用户模块相关正则表达式常量池
 *
 * @author Ling, Jiatong
 * Date: 2023/3/16 10:54
 */
public interface UserRegexConstant {

    // 用户名正则表达式
    Pattern USERNAME_REGEX = Pattern.compile("^[a-zA-Z0-9]{4,20}$");
    // 密码正则表达式
    Pattern PASSWORD_REGEX = Pattern.compile("^(?=.*[A-Z]+)(?=.*[a-z]+)(?=.*[0-9，,。.+=\\[\\]]+)[a-zA-Z0-9，,。.+=\\[\\]]{6,20}$");
    // 邮箱正则表达式
    Pattern EMAIL_REGEX = Pattern.compile("^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$");
}
