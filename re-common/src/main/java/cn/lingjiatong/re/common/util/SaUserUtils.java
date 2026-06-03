package cn.lingjiatong.re.common.util;

import cn.lingjiatong.re.common.entity.User;

/**
 * 当前用户ThreadLocal工具类
 *
 * 在Filter/Interceptor中设置，后续整个线程链路可获取
 * 子线程和线程池通过ContextTaskDecorator传递
 *
 * @author Ling, Jiatong
 */
public class SaUserUtils {

    private static final ThreadLocal<User> USER_HOLDER = new InheritableThreadLocal<>();

    public static void setCurrentUser(User user) {
        USER_HOLDER.set(user);
    }

    public static User getCurrentUser() {
        return USER_HOLDER.get();
    }

    public static void clear() {
        USER_HOLDER.remove();
    }
}
