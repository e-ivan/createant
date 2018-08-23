package com.qnyy.re.business.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 红包领取状态
 * Created by E_Iva on 2017.11.29 0029.
 */
@Getter@AllArgsConstructor
public enum ReMomentDrawStateEnum {
    UN_DRAW(0,"未领取"),
    DRAW(1,"已领取"),
    ;

    private Integer code;
    private String value;
}
