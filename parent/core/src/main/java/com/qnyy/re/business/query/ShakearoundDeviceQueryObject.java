package com.qnyy.re.business.query;

import com.qnyy.re.base.util.container.QueryObject;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by E_Iva on 2018.5.24 0024.
 */
@Getter@Setter
public class ShakearoundDeviceQueryObject extends QueryObject {
    private Integer state = -1;
    private Integer deviceId = -1;
    private Integer code = -1;
}
