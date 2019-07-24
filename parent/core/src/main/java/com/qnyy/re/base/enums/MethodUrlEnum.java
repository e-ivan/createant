package com.qnyy.re.base.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 *
 * @author lianghaifeng
 * @version Id: MethodUrl.java, v 0.1 2019.5.30 030 15:56 lianghaifeng Exp $$
 */
@Getter
@AllArgsConstructor
public enum MethodUrlEnum {
    /**
     * 获取数据
     */
    GET_DATA("/system/getData","/get"),
    /**
     * 保存数据
     */
    PUT_DATA("/system/putData","/put"),
    /**
     * 移除数据
     */
    REMOVE_DATA("/system/clearData","/clear"),
    ;
    private String url;
    private String newUrl;
}
