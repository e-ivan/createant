package com.qnyy.re.base.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * api信息位置
 * Created by E_Iva on 2018.2.12.0012.
 */
@Getter@AllArgsConstructor
public enum ApiInfoSite {
    APP("APP"),
    MGR("MGR")
    ;
    private String value;
}
