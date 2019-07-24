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
