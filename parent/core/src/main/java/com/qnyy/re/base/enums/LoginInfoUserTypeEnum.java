package com.qnyy.re.base.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 用户类型
 * Created by E_Iva on 2017/11/24.
 */
@Getter@AllArgsConstructor
public enum LoginInfoUserTypeEnum {
    USER(0,"普通用户"),
    MANAGE(1,"管理人员");

    private Integer code;
    private String value;
}
