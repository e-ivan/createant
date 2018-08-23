package com.qnyy.re.business.vo.param;

import com.qnyy.re.base.util.annotation.VerifyParam;
import com.qnyy.re.base.util.container.BaseParamVO;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by E_Iva on 2017.12.16.0016.
 */
@Getter@Setter
public class SaveCashAccountVO extends BaseParamVO {

    @VerifyParam
    private String alipayAccount;

    @VerifyParam
    private String accountName;

    @VerifyParam
    private String phone;

    private String remark;

    @VerifyParam
    private String password;

}
