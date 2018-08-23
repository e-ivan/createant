package com.qnyy.re.business.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 红包明细状态
 * Created by E_Iva on 2017.12.1 0001.
 */
@Getter@AllArgsConstructor
public enum ReCommentStateEnum {
    NORMAL(0,"正常"),
    DELETE(1,"删除"),
    ;

    private Integer code;
    private String value;
}
