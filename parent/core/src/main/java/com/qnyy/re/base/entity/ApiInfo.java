package com.qnyy.re.base.entity;

import com.qnyy.re.base.util.container.BaseEntity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * 接口信息
 */
@Getter@Setter@EqualsAndHashCode(callSuper = false)
public class ApiInfo extends BaseEntity{

    private String name;

    private String uri;

    private String type;

    private String beanClass;

    private String method;

    private String intro;

    private String requestMethod;

    private String site;

    private boolean requiredLogin;

    private boolean checkSign;
}