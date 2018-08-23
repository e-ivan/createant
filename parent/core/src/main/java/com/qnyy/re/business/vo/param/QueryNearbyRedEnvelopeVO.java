package com.qnyy.re.business.vo.param;

import com.qnyy.re.base.util.annotation.VerifyParam;
import com.qnyy.re.base.util.container.BaseParamVO;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by E_Iva on 2017.12.9.0009.
 */
@Getter@Setter
public class QueryNearbyRedEnvelopeVO extends BaseParamVO {
    private Long uid;
    @VerifyParam
    private String lng;
    @VerifyParam
    private String lat;
}
