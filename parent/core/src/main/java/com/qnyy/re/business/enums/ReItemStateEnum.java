package com.qnyy.re.business.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 红包明细状态
 * Created by E_Iva on 2017.12.1 0001.
 */
@Getter@AllArgsConstructor
public enum ReItemStateEnum {
    CREATE(0,"已创建"),
    DRAW(1,"已领取"),
    ;
    private Integer code;
    private String value;
}
