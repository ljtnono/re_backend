package cn.lingjiatong.re.common.constant;

import java.util.regex.Pattern;

/**
 * 菜单正则表达式常量池
 *
 * @author Ling, Jiatong
 * Date: 2023/4/14 15:08
 */
public interface MenuRegexConstant {

    // 菜单标题为2-20个字符，包含字母、数字、中文，不能包含特殊字符
    Pattern MENU_TITLE_REGEX = Pattern.compile("^[\\u4E00-\\u9FA5A-Za-z0-9]{2,20}$");
    // 菜单名称为2-20个字符，只能为字母
    Pattern MENU_NAME_REGEX = Pattern.compile("^[a-zA-Z]{2,20}$");
    // 菜单路由路径
    Pattern MENU_PATH_REGEX = Pattern.compile("^(?!.*?\\/\\/)(?!.*?\\.\\.\\/)((\\/?[a-zA-Z_][\\w\\-]*)+\\/?)*");
    // 菜单组件名称 2-20个字母的字符串
    Pattern MENU_COMPONENT_NAME = Pattern.compile("^[a-zA-Z]{2,20}$");
    // 菜单组件路径 后缀名为.vue的路径
    Pattern MENU_COMPONENT_PATH = Pattern.compile("^(?!.*\\.\\.\\//\\.\\.\\/)[\\w\\/]+\\.vue$");
}
