package com.qnyy.re.base.util.container;

import lombok.Getter;
import lombok.Setter;

/**
 * 高级查询工具
 */
@Getter
@Setter
abstract public class QueryObject {
    private Integer currentPage = 1;
    private Integer pageSize = 10;

    public int getStart() {
        return (currentPage - 1) * pageSize;
    }
    public boolean hasLength(String str) {
        return str != null && !"".equals(str.trim());
    }

}
