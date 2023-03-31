package cn.lingjiatong.re.common.constant;

/**
 * 用户相关异常信息常量池
 *
 * @author Ling, Jiatong
 * Date: 2023/3/16 10:31
 */
public interface UserErrorMessageConstant {

    // 用户名为空
    String USERNAME_EMPTY_ERROR_MESSAGE = "用户名不能为空";
    // 用户名必须为 4-20英文数字字符串
    String USERNAME_FORMAT_ERROR_MESSAGE = "用户名为4-20位英文数字字符串";
    // 密码不能为空
    String PASSWORD_EMPTY_ERROR_MESSAGE = "密码不能为空";
    // 密码格式异常 密码必须包含大写字母和小写字母，然后特殊符号和数字其中的一种（特殊符号有，,。.+=[]），并且长度在8-20位之间
    String PASSWORD_FORMAT_ERROR_MESSAGE = "密码必须包含大写字母和小写字母，然后特殊符号和数字其中的一种（，,。.+=[]），并且长度在8-20位之间";
    // 邮箱不能为空
    String EMAIL_EMPTY_ERROR_MESSAGE = "邮箱不能为空";
    // 邮箱格式错误
    String EMAIL_FORMAT_ERROR_MESSAGE = "邮箱格式错误";
    // 角色不存在
    String ROLE_NOT_EXIST_ERROR_MESSAGE = "角色不存在或已被删除";
    // 用户id不能为空
    String USER_ID_EMPTY_ERROR_MESSAGE = "用户id不能为空";
    // 用户不存在或已被删除
    String USER_NOT_EXIST_ERROR_MESSAGE = "用户不存在或已被删除";

    // 不支持的deleteStatus值
    String DELETE_STATUS_NOT_SUPPORT_ERROR_MESSAGE = "不支持的deleteStatus值";
    // 无法删除超级管理员账号
    String DELETE_SUPER_ADMIN_ERROR_MESSAGE = "无法删除系统内置超级管理员账号";
    String HIDDEN_SUPER_ADMIN_ERROR_MESSAGE = "无法隐藏系统内置超级管理员帐号";

}
