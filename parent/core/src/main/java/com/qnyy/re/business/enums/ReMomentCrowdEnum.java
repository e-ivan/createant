package com.qnyy.re.business.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 红包人群
 * Created by E_Iva on 2017.11.30 0030.
 */
@Getter@AllArgsConstructor
public enum ReMomentCrowdEnum {
    ALL(0,"全部"),
    MALE(1,"男性"),
    FEMALE(2,"女性"),
    JUNIOR(3,"下级"),
    CREATEANT(4,"创蚁"),
    SHAKE(5,"摇一摇"),
    ;

    public static ReMomentScopeEnum getByCode(Integer code){
        for(ReMomentScopeEnum crowd: ReMomentScopeEnum.values()){
            if (crowd.getCode().equals(code)){
                return crowd;
            }
        }
        return null;
    }

    private Integer code;
    private String value;
}
