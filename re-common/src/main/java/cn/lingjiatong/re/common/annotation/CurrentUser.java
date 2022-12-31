package cn.lingjiatong.re.common.annotation;

import java.lang.annotation.*;

/**
 * 注入当前用户的注解
 *
 * @author Ling, Jiatong
 * Date: 2022/12/31 23:26
 */
@Documented
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface CurrentUser {
}
