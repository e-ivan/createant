package com.qnyy.re.base.vo.param;

import com.qnyy.re.base.util.annotation.VerifyParam;
import com.qnyy.re.base.util.container.BaseParamVO;
import lombok.Getter;
import lombok.Setter;

/**
 * 抽象的登录通用类
 */
@Getter@Setter
public abstract class AbstractLoginVO extends BaseParamVO {
    @VerifyParam(unCheckMethod = "createantLogin")
    private String lng;//经度
    @VerifyParam(unCheckMethod = "createantLogin")
    private String lat;//纬度
    private String ip;
}
