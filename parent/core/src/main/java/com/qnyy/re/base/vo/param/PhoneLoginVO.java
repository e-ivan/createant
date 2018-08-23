package com.qnyy.re.base.vo.param;

import com.qnyy.re.base.util.annotation.VerifyParam;
import lombok.Getter;
import lombok.Setter;

/**
 * 手机登录接口参数
 * Created by E_Iva on 2017/11/27.
 */
@Getter@Setter
public class PhoneLoginVO extends AbstractLoginVO {
    @VerifyParam
    private String phone;
    @VerifyParam
    private String password;
}
