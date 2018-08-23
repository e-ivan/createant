package com.qnyy.re.base.vo.param;

import com.qnyy.re.base.util.annotation.VerifyParam;
import com.qnyy.re.base.util.container.BaseParamVO;
import lombok.Getter;
import lombok.Setter;

/**
 * 手机设置密码
 * Created by E_Iva on 2017.12.21.0021.
 */
@Getter@Setter
public class ResetPasswordByPhoneVO extends BaseParamVO {
    @VerifyParam
    private String phone;
    @VerifyParam
    private String password;
    @VerifyParam
    private String code;

    private String ip;
}
