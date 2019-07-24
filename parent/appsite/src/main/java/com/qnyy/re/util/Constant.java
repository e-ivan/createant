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
