package com.qnyy.re.base.query;

import com.qnyy.re.base.util.container.QueryObject;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by E_Iva on 2017.12.8.0008.
 */
@Getter@Setter
public class UserVisitHistoryQueryObject extends QueryObject {
    private Long byVisitUid = -1L;//查询该用户被访记录
    private Long visitUid = -1L;//查询该用户访问记录
}
