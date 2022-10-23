package cn.lingjiatong.re.common.annotation;

import java.lang.annotation.*;

/**
 * 自定义注解，自动注入当前登录用户对象
 *
 * @author Ling, Jiatong
 * Date: 2020/7/10
 */
@Documented
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface LoginUser {

}
