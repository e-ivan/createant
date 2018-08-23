package com.qnyy.re.business.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 红包区域
 * Created by E_Iva on 2017.11.30 0030.
 */
@Getter@AllArgsConstructor
public enum ReMomentScopeEnum {
    ONE_KM(1,"一公里红包"),
    DISTRICT(2,"县区红包"),
    CITY(3,"全市红包"),
    ALL(4,"全部"),
    ;

    public static ReMomentScopeEnum getByCode(Integer code){
        for(ReMomentScopeEnum scope: ReMomentScopeEnum.values()){
            if (scope.getCode().equals(code)){
                return scope;
            }
        }
        return null;
    }

    private Integer code;
    private String value;
}
