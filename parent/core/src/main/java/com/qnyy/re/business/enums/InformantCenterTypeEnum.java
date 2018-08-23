package com.qnyy.re.business.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 举报中心类型
 * Created by E_Iva on 2017.12.19.0019.
 */
@Getter@AllArgsConstructor
public enum InformantCenterTypeEnum {
    USER_INFO(0,"用户信息"),
    RE_MOMENT(1,"红包信息"),
    RE_COMMENT(2,"评论信息"),
    ;

    public static InformantCenterTypeEnum getByCode(Integer code){
        for(InformantCenterTypeEnum type: InformantCenterTypeEnum.values()){
            if (type.getCode().equals(code)){
                return type;
            }
        }
        return null;
    }

    private Integer code;
    private String value;
}
