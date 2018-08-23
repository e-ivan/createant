package com.qnyy.re.business.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 红包状态
 * Created by E_Iva on 2017.11.29 0029.
 */
@Getter@AllArgsConstructor
public enum ReMomentStateEnum {
    WAIT(0,"待付款"),
    NORMAL(1,"派发中"),
    FINISH(2,"已领完"),
    ;

    private Integer code;
    private String value;
}
