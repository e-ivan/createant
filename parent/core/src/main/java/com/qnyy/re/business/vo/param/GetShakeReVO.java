package com.qnyy.re.business.vo.param;

import com.qnyy.re.base.util.annotation.VerifyParam;
import com.qnyy.re.base.util.container.BaseParamVO;
import lombok.Getter;
import lombok.Setter;

@Getter@Setter
public class GetShakeReVO extends BaseParamVO {
    @VerifyParam("type")
    private Long storeId;
    @VerifyParam("type")
    private Long uid;
}
