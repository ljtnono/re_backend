package cn.lingjiatong.re.service.sys.constant;

import java.util.regex.Pattern;

/**
 * 系统配置正则表达式常量
 *
 * @author Ling, Jiatong
 * Date: 2022/10/8 20:54
 */
public interface SysConfigRegexConstant {


    // 只包括数字和字母的组合
    Pattern SYS_CONFIG_KEY_REGEX = Pattern.compile("^[a-zA-Z0-9_]$");

}
