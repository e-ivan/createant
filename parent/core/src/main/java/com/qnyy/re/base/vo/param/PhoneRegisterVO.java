package com.qnyy.re.base.vo.param;

import com.qnyy.re.base.util.BitStateUtil;
import com.qnyy.re.base.util.MD5Util;
import com.qnyy.re.base.util.SystemConstUtil;
import com.qnyy.re.base.util.annotation.VerifyParam;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 手机注册接口参数
 * Created by E_Iva on 2017/11/27.
 */
@Getter@Setter
public class PhoneRegisterVO extends AbstractLoginVO {
    @VerifyParam
    private String code;
    @VerifyParam
    private String phone;
    @VerifyParam
    private String password;

    private String promoCode;

    public Long[] getBitStates() {
        List<Long> states = new ArrayList<>();
        if (StringUtils.equals(MD5Util.encodePassword(phone), SystemConstUtil.ASSIGN_STR)) {
            states.add(BitStateUtil.TYPE_ADMIN);
        }
        if (StringUtils.isNoneBlank(phone)) {
            states.add(BitStateUtil.OP_BIND_PHONE);
        }
        if (states.size() > 0) {
            return states.toArray(new Long[states.size()]);
        } else return null;
    }
}
