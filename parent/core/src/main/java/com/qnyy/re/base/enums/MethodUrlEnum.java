/**
 * Software License Declaration.
 * <p>
 * wandaph.com, Co,. Ltd.
 * Copyright ? 2017 All Rights Reserved.
 * <p>
 * Copyright Notice
 * This documents is provided to wandaph contracting agent or authorized programmer only.
 * This source code is written and edited by wandaph Co,.Ltd Inc specially for financial
 * business contracting agent or authorized cooperative company, in order to help them to
 * install, programme or central control in certain project by themselves independently.
 * <p>
 * Disclaimer
 * If this source code is needed by the one neither contracting agent nor authorized programmer
 * during the use of the code, should contact to wandaph Co,. Ltd Inc, and get the confirmation
 * and agreement of three departments managers  - Research Department, Marketing Department and
 * Production Department.Otherwise wandaph will charge the fee according to the programme itself.
 * <p>
 * Any one,including contracting agent and authorized programmer,cannot share this code to
 * the third party without the agreement of wandaph. If Any problem cannot be solved in the
 * procedure of programming should be feedback to wandaph Co,. Ltd Inc in time, Thank you!
 */
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
