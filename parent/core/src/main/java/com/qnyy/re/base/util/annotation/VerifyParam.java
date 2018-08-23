package com.qnyy.re.base.util.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 需要检验空值的注解
 * Created by E_Iva on 2017/11/27.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface VerifyParam {
    /**
     * 定义一个名，名字相同则分为一组，在这个组中多选一
     * @return
     */
    String value() default "";

    /**
     * 不需要检查空值的方法
     * @return
     */
    String[] unCheckMethod() default "";
}
