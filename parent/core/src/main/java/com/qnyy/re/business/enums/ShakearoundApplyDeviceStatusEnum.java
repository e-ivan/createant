package com.qnyy.re.business.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 周边摇一摇申请状态
 * Created by E_Iva on 2018.5.24 0024.
 */
@Getter@AllArgsConstructor
public enum ShakearoundApplyDeviceStatusEnum {
    FAIL(0, "未通过"),
    AUDIT(1, "审核中"),
    PASS(2, "审核通过");
    private Integer code;
    private String value;
}
