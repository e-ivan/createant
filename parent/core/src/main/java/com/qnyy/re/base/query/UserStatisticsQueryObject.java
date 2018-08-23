package com.qnyy.re.base.query;

import com.qnyy.re.base.util.container.QueryObject;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by E_Iva on 2017.12.14.0014.
 */
@Getter@Setter
public class UserStatisticsQueryObject extends QueryObject {
    private double minRePayAmount;//最低发送红包
    private String orderBy;
}
