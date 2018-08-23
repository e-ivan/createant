package com.qnyy.re.base.util.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * api文档
 * Created by E_Iva on 2018.2.8.0008.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface ApiDocument {
    String value();//接口名称
    String intro() default "";//接口详细
}
