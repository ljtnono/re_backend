package cn.lingjiatong.re.common.constant;

import java.util.regex.Pattern;

/**
 * 菜单正则表达式常量池
 *
 * @author Ling, Jiatong
 * Date: 2023/4/14 15:08
 */
public interface MenuRegexConstant {

    // 菜单名称为2-20个字符，只能为字母
    Pattern MENU_NAME_REGEX = Pattern.compile("");
    // 菜单标题为2-20个字符，包含字母、数字、中文，不能包含特殊字符
    Pattern MENU_TITLE_REGEX = Pattern.compile("");
    // 菜单组件名称
    Pattern MENU_COMPONENT_NAME = Pattern.compile("");
    // 菜单组件路径
    Pattern MENU_COMPONENT_PATH = Pattern.compile("");
}
