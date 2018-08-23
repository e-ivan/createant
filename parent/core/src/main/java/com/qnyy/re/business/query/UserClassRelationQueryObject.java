package com.qnyy.re.business.query;

import com.qnyy.re.base.util.container.QueryObject;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by E_Iva on 2017.12.18.0018.
 */
@Getter@Setter
public class UserClassRelationQueryObject extends QueryObject {
    private Long uid = -1L;
    private Integer level = -1;
    private String orderBy;
}
