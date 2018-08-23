package com.qnyy.re.base.vo.param;

import com.qnyy.re.base.util.annotation.VerifyParam;
import com.qnyy.re.business.enums.RegisterSourceEnum;
import lombok.Getter;
import lombok.Setter;

/**
 * 登录接口参数
 * Created by E_Iva on 2017/11/27.
 */
@Getter@Setter
public class PartnerLoginVO extends AbstractLoginVO {
    @VerifyParam
    private String openId;
    @VerifyParam
    private Integer source;

    public void setSource(Integer source){
        if (RegisterSourceEnum.getByCode(source) == null) {
            source = null;
        }
        this.source = source;
    }
}
