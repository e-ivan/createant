package com.qnyy.re.base.vo.param;

import com.qnyy.re.base.util.annotation.VerifyParam;
import com.qnyy.re.base.util.container.BaseParamVO;
import lombok.Getter;
import lombok.Setter;

/**
 * 旧密码设置密码
 * Created by E_Iva on 2017.12.21.0021.
 */
@Getter@Setter
public class ResetPasswordByOldVO extends BaseParamVO {
    @VerifyParam
    private String oldPassword;
    @VerifyParam
    private String password;

    private String ip;
}
