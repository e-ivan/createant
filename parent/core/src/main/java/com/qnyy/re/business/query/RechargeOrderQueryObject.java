package com.qnyy.re.business.query;

import com.qnyy.re.base.util.container.QueryObject;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by E_Iva on 2017.12.5.0005.
 */
@Getter@Setter
public class RechargeOrderQueryObject extends QueryObject {
    private Long userId = -1L;
    private Integer state = -1;

    private Integer hour = -1;
}
