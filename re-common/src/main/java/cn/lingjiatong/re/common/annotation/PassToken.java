package cn.lingjiatong.re.common.annotation;

import java.lang.annotation.*;

/**
 * 跳过token验证
 *
 * @author Ling, Jiatong
 * Date: 2020/7/10 10:58
 */
@Documented
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface PassToken {

}
