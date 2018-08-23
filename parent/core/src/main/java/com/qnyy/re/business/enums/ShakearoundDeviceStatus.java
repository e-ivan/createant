package com.qnyy.re.business.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 周边设备状态
 * Created by E_Iva on 2018.5.28 0028.
 */
@Getter@AllArgsConstructor
public enum ShakearoundDeviceStatus {

    UN_BIND(0, "未绑定"),
    BIND(1, "已绑定"),
    ;
    private Integer code;
    private String value;
}
