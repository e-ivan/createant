package com.qnyy.re.business.query;

import com.qnyy.re.base.util.container.QueryObject;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by E_Iva on 2017.12.5.0005.
 */
@Getter@Setter
public class UserAccountStatementQueryObject extends QueryObject {
    private Long uid = -1L;
    private Integer dealType = -1;
    private String orderBy;
}
