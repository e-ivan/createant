package com.qnyy.re.base.query;

import com.qnyy.re.base.util.container.QueryObject;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by E_Iva on 2017.12.13.0013.
 */
@Getter@Setter
public class UserMsgLogQueryObject extends QueryObject {
    private Long to = -1L;
}
