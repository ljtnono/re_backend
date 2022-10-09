package cn.lingjiatong.re.service.sys.constant;

/**
 * 记录系统配置接口返回的错误消息的常量
 *
 * @author Ling, Jiatong
 * Date: 2022/10/8 21:07
 */
public interface SysConfigErrorMessageConstant {

    // 系统配置健格式错误返回的消息
    String SYS_CONFIG_KEY_FORMAT_MESSAGE = "系统配置的健必须是4-20位的字母和数字的组合";
    // 系统配置Key不能为空字符串或者NULL
    String SYS_CONFIG_KEY_NOT_EMPTY_MESSAGE = "系统配置的健不能为空字符串或NULL";

}
