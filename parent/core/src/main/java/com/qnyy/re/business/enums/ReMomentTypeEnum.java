package com.qnyy.re.business.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 红包类型
 * Created by E_Iva on 2017.12.14.0014.
 */
@Getter@AllArgsConstructor
public enum ReMomentTypeEnum {
    MOMENT(0,"朋友圈"),
    SINGLE(1,"单人红包"),
    ACTIVITY(2,"活动红包"),
    ;

    private Integer code;
    private String value;
}
