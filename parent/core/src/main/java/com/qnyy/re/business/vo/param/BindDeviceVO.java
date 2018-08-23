package com.qnyy.re.business.vo.param;

import com.qnyy.re.base.util.annotation.VerifyParam;
import com.qnyy.re.base.util.container.BaseParamVO;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by E_Iva on 2018.6.6 0006.
 */
@Getter
@Setter
public class BindDeviceVO extends BaseParamVO {
    private Long uid;
    @VerifyParam
    private String iBeaconId;
    private long storeId;
    @VerifyParam
    private String pageTitle;
    private String pageIconUrl;
    private String description;
}
