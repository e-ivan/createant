package com.qnyy.re.base.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 登录信息状态
 * Created by E_Iva on 2017/11/24.
 */
@Getter@AllArgsConstructor
public enum LoginInfoStateEnum {
    NORMAL(0,"正常");

    private Integer code;
    private String value;
}
