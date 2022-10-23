package cn.lingjiatong.re.auth.constant;

/**
 * 认证模块错误消息常量池
 *
 * @author Ling, Jiatong
 * Date: 2022/10/23 02:15
 */
public interface AuthErrorMessageConstant {

    // 用户名为空
    String USERNAME_EMPTY_ERROR_MESSAGE = "请输入用户名";
    // 密码为空
    String PASSWORD_EMPTY_ERROR_MESSAGE = "请输入密码";
    // 验证码为空
    String VERIFY_CODE_VALUE_EMPTY_ERROR_MESSAGE = "请输入验证码";
    // 验证码错误
    String VERIFY_CODE_ERROR_MESSAGE = "验证码错误";

}
