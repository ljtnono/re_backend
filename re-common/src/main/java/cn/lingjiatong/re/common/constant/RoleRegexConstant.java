package cn.lingjiatong.re.common.constant;

import java.util.regex.Pattern;

/**
 * 角色模块相关的正则表达式常量池
 *
 * @author Ling, Jiatong
 * Date: 3/28/23 9:41 PM
 */
public interface RoleRegexConstant {

    // 角色名正则表达式  只能为中文、英文、数字，长度不超过4-30个字符
    Pattern ROLE_SAVE_NAME_REGEX = Pattern.compile("[a-zA-Z0-9\\u4e00-\\u9fa5]{4,30}");

}
