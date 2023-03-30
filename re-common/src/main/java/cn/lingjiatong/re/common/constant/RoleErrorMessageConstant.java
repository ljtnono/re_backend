package cn.lingjiatong.re.common.constant;

/**
 * 角色异常信息常量池
 *
 * @author Ling, Jiatong
 * Date: 3/28/23 9:34 PM
 */
public interface RoleErrorMessageConstant {

    // 角色id为空
    String ROLE_UPDATE_ID_EMPTY_ERROR_MESSAGE = "请选择要编辑的角色";
    // 角色不存在或已经被删除
    String ROLE_NOT_EXIST_ERROR_MESSAGE = "角色不存在或已被删除";
    // 角色名为空
    String ROLE_NAME_EMPTY_ERROR_MESSAGE = "角色名不能为空";
    // 角色格式错误
    String ROLE_NAME_FORMAT_ERROR_MESSAGE = "角色名为中文、英文和数字的字符串，长度为4-30个字符";
    // 角色描述长度异常
    String ROLE_DESCRIPTION_LENGTH_ERROR_MESSAGE = "角色描述长度不能超过200个字符";
    // 角色菜单不存在
    String ROLE_MENU_ID_NOT_EXIST_ERROR_MESSAGE = "菜单不存在或已被删除";
    // 不能删除内置的admin角色
    String CAN_NOT_DELETE_ADMIN_ROLE_ERROR_MESSAGE = "无法删除内置管理员角色";
    // 无法修改内置admin角色
    String CAN_NOT_UPDATE_ADMIN_ROLE_ERROR_MESSAGE = "无法修改内置管理员角色";

}
