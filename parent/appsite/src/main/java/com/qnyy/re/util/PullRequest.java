package com.qnyy.re.util;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Map;

/**
 *
 * @author lianghaifeng
 * @version Id: PullRequest.java, v 0.1 2019.6.25 025 15:11 lianghaifeng Exp $$
 */
@Getter
@Setter
public class PullRequest implements Serializable {
    private String url;
    private Map<String, String> param;
}
