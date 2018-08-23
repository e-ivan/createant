package com.qnyy.re.business.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 注册源
 * Created by E_Iva on 2017/11/24.
 */
@Getter@AllArgsConstructor
public enum RegisterSourceEnum {
    WECHAT(1,"微信"),
    CREATEANT(2,"创蚁")
    ;

    public static RegisterSourceEnum getByCode(Integer code){
        for(RegisterSourceEnum source: RegisterSourceEnum.values()){
            if (source.getCode().equals(code)){
                return source;
            }
        }
        return null;
    }

    private Integer code;
    private String value;
}
