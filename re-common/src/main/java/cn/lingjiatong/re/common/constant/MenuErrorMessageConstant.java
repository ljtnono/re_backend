package cn.lingjiatong.re.common.constant;

/**
 * 菜单相关常量池
 *
 * @author Ling, Jiatong
 * Date: 2023/4/14 14:34
 */
public interface MenuErrorMessageConstant {

    // 不支持的项目名称
    String ILLEGAL_PROJECT_NAME_ERROR_MESSAGE = "不支持的项目名称";
    // 请选择父级菜单
    String PARENT_ID_EMPTY_ERROR_MESSAGE = "请选择父级菜单或者作为顶层菜单";
    // 菜单名称不能为空
    String MENU_NAME_EMPTY_ERROR_MESSAGE = "菜单名称不能为空";
    String MENU_NAME_FORMAT_ERROR_MESSAGE = "菜单名称为2-20个字符串的，只能包括字母";
    // 菜单标题不能为空
    String MENU_TITLE_EMPTY_ERROR_MESSAGE = "菜单标题不能为空";
    String MENU_TITLE_FORMAT_ERROR_MESSAGE = "菜单标题为2-20个字符串，可以是字母、数字和中文字符";
    // 菜单路由路径不能为空
    String MENU_PATH_EMPTY_ERROR_MESSAGE = "菜单路由路径不能为空";
    // 菜单组件名称不能为空
    String MENU_COMPONENT_NAME_EMPTY_ERROR_MESSAGE = "菜单组件名称不能为空";
    // 菜单组件路径不能为空
    String MENU_COMPONENT_PATH_EMPTY_ERROR_MESSAGE = "菜单组件路径不能为空";
    // 子级菜单不能再添加子级菜单
    String MENU_CANNOT_ADD_MENU_TO_A_SUB_MENU_ERROR_MESSAGE = "子级菜单无法再添加子级菜单";
}
