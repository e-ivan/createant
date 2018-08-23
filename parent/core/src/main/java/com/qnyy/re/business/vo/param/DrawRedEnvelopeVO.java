package com.qnyy.re.business.vo.param;

import com.qnyy.re.base.util.annotation.VerifyParam;
import com.qnyy.re.base.util.container.BaseParamVO;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by E_Iva on 2017.12.4.0004.
 */
@Getter
@Setter
public class DrawRedEnvelopeVO extends BaseParamVO {
    @VerifyParam("type")
    private Long reId;
    @VerifyParam
    private String lng;
    @VerifyParam
    private String lat;
    private Long uid;
    @VerifyParam("type")
    private String ticket;
    private Integer storeId;
    private Integer activityId;
}
