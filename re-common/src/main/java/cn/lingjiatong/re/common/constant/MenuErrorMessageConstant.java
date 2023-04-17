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
    String MENU_NAME_FORMAT_ERROR_MESSAGE = "菜单名称为2-20个字符，只能包括字母";
    // 菜单标题不能为空
    String MENU_TITLE_EMPTY_ERROR_MESSAGE = "菜单标题不能为空";
    String MENU_TITLE_FORMAT_ERROR_MESSAGE = "菜单标题为2-20个字符，可以是字母、数字和中文字符";
    // 菜单路由路径不能为空
    String MENU_PATH_EMPTY_ERROR_MESSAGE = "菜单路由路径不能为空";
    // 菜单路由路径格式错误
    String MENU_PATH_FORMAT_ERROR_MESSAGE = "菜单路由路径格式异常";
    // 菜单组件名称不能为空
    String MENU_COMPONENT_NAME_EMPTY_ERROR_MESSAGE = "菜单组件名称不能为空";
    // 菜单组件名称只能为大驼峰形式
    String MENU_COMPONENT_NAME_FORMAT_ERROR_MESSAGE = "菜单组件名称为2-20个字符，只能包含字母";
    // 菜单组件路径不能为空
    String MENU_COMPONENT_PATH_EMPTY_ERROR_MESSAGE = "菜单组件路径不能为空";
    // 菜单组件路径格式异常
    String MENU_COMPONENT_PATH_FORMAT_ERROR_MESSAGE = "菜单组件路径格式异常";
    // 父级菜单不存在
    String MENU_PARENT_MENU_NOT_EXIST_ERROR_MESSAGE = "父级菜单不存在或已被删除";
}
