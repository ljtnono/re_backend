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
    // 菜单路由路径
    Pattern MENU_ROUTE_PATH_REGEX = Pattern.compile("^(?!.*?\\/\\/)(?!.*?\\.\\.\\/)((\\/?[a-zA-Z_][\\w\\-]*)+\\/?)*");
    // 菜单路由名称 2-20个字母的字符串
    Pattern MENU_ROUTE_NAME_REGEX = Pattern.compile("^[a-zA-Z]{2,20}$");
    // 菜单组件路径 后缀名为.vue的路径
    Pattern MENU_COMPONENT_PATH_REGEX = Pattern.compile("^(?!.*\\.\\.\\/\\.\\.\\/)[\\w/]+\\.vue$");
    // 菜单权限名称2-50个字符，包含字母、数字、中文，不能包含特殊字符
    Pattern MENU_PERMISSION_NAME_REGEX = Pattern.compile("^[\\u4E00-\\u9FA5A-Za-z0-9]{2,50}$");
    // 菜单权限表达式2-100个字符，包含字母和分号
    Pattern MENU_PERMISSION_EXPRESSION_REGEX = Pattern.compile("^[A-Za-z:]{2,100}$");
}
