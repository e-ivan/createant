package com.qnyy.re.base.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 审核状态
 * Created by Administrator on 2016/12/15.
 */
@Getter@AllArgsConstructor
public enum AuditStatusEnum {
    CREATE(0,"已创建"),
    PASS(1,"审核通过"),
    REFUSE(2,"审核拒绝");

    private Integer code;
    private String value;

    public static AuditStatusEnum getByCode(Integer code){
        for(AuditStatusEnum status: AuditStatusEnum.values()){
            if (status.getCode().equals(code)){
                return status;
            }
        }
        return null;
    }
}
