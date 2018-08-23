package com.qnyy.re.base.util.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 不需要登录检查注解
 * Created by E_Iva on 2017/11/24.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface UnRequiredLogin {
    boolean checkSign() default true;//是否检查签名
}
