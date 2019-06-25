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

import java.util.regex.Pattern;

/**
 *
 * @author lianghaifeng
 * @version Id: Constant.java, v 0.1 2019.6.25 025 9:39 lianghaifeng Exp $$
 */
public class Constant {
    /**
     * key[index]
     */
    public static final Pattern KEY_INDEX_PATTERN = Pattern.compile("(\\w+)\\[(\\d+)]$");
    /**
     * 正则表达式存在字符
     */
    public static final Pattern REGEX_PATTERN = Pattern.compile("[\\\\ !$%^&*()+=|{}\\[\\].<>/?]|\n|\r|\t");
    /**
     * 链表结构
     */
    public static final String LINK_KEY_REGEX = "\\w+(\\[\\d+])?(\\.\\w+(\\[\\d+])?)+";
    /**
     * 数组key
     */
    public static final String ARR_KEY_REGEX = "\\w+\\[\\d+]$";
    /**
     * 数组
     */
    public static final String ARR_REGEX = "\\s*\\[.*]\\s*";
    /**
     * json
     */
    public static final String JSON_REGEX = "\\s*\\{.*}\\s*";
    public static final String ALL_DATA = "ALL_DATA";
    public static final String ALL_KEY = "ALL_KEY";
    public static final String ALL_CONFIG = "ALL_CONFIG";
    public static final String MAX_HISTORY_ROW = "MAX_HISTORY_ROW";
    public static final String NO_RECORD_HISTORY_KEY = "NO_RECORD_HISTORY_KEY";
}
