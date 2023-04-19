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
    // 菜单标题不能为空
    String MENU_TITLE_EMPTY_ERROR_MESSAGE = "菜单标题不能为空";
    String MENU_TITLE_FORMAT_ERROR_MESSAGE = "菜单标题为2-20个字符，可以是字母、数字和中文字符";
    // 菜单路由路径不能为空
    String MENU_ROUTE_PATH_EMPTY_ERROR_MESSAGE = "菜单路由路径不能为空";
    // 菜单路由路径格式错误
    String MENU_ROUTE_PATH_FORMAT_ERROR_MESSAGE = "菜单路由路径格式异常";
    // 菜单路由名称不能为空
    String MENU_ROUTE_NAME_EMPTY_ERROR_MESSAGE = "菜单路由名称不能为空";
    // 菜单路由名称只能为大驼峰形式
    String MENU_ROUTE_NAME_FORMAT_ERROR_MESSAGE = "菜单路由名称为2-20个字符，只能包含字母";
    // 菜单组件路径不能为空
    String MENU_COMPONENT_PATH_EMPTY_ERROR_MESSAGE = "菜单组件路径不能为空";
    // 菜单组件路径格式异常
    String MENU_COMPONENT_PATH_FORMAT_ERROR_MESSAGE = "菜单组件路径格式异常";
    // 菜单权限名称不能为空
    String MENU_PERMISSION_NAME_EMPTY_ERROR_MESSAGE = "菜单权限名称不能为空";
    // 菜单权限名称为2-50个字符，包含字母、数字、中文，不能包含特殊字符
    String MENU_PERMISSION_NAME_FORMAT_ERROR_MESSAGE = "菜单权限名称为2-50个字符，包含字母、数字、中文，不能包含特殊字符";
    // 菜单权限表达式不能为空
    String MENU_PERMISSION_EXPRESSION_EMPTY_ERROR_MESSAGE = "菜单权限表达式不能为空";
    // 菜单权限表达式2-100个字符，包含字母和分号
    String MENU_PERMISSION_EXPRESSION_FORMAT_ERROR_MESSAGE = "菜单权限表达式为2-100个字符，包含字母和分号";
    // 父级菜单不存在
    String MENU_PARENT_MENU_NOT_EXIST_ERROR_MESSAGE = "父级菜单不存在或已被删除";
}
