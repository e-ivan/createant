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
package com.qnyy.re.util;

import lombok.Getter;
import lombok.Setter;

import java.util.regex.Matcher;

import static com.qnyy.re.util.Constant.ARR_KEY_REGEX;
import static com.qnyy.re.util.Constant.KEY_INDEX_PATTERN;

/**
 *
 * @author lianghaifeng
 * @version Id: KeyIndex.java, v 0.1 2019.6.25 025 9:38 lianghaifeng Exp $$
 */
@Getter
@Setter
public class KeyIndex {
    public KeyIndex(String sourceKey) {
        boolean arrKey = sourceKey.matches(ARR_KEY_REGEX);
        if (arrKey) {
            Matcher m = KEY_INDEX_PATTERN.matcher(sourceKey);
            if (m.find()) {
                this.key = m.group(1);
                this.index = Integer.parseInt(m.group(2));
                this.isArr = true;
            }
        } else {
            this.key = sourceKey;
        }
    }

    private String key;
    private int index;
    private boolean isArr;
}
