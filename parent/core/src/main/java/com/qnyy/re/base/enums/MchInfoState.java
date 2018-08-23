package com.qnyy.re.base.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 商户信息状态
 * Created by E_Iva on 2018.2.8.0008.
 */
@Getter@AllArgsConstructor
public enum MchInfoState {
    NORMAL((byte)0,"正常"),
    CLOSE((byte)1,"关闭"),
    ;
    private byte code;
    private String value;
}
