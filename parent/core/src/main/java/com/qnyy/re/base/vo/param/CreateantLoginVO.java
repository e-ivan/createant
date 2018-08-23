package com.qnyy.re.base.vo.param;

import com.qnyy.re.base.util.BitStateUtil;
import com.qnyy.re.base.util.MD5Util;
import com.qnyy.re.base.util.SystemConstUtil;
import com.qnyy.re.base.util.annotation.VerifyParam;
import com.qnyy.re.business.enums.RegisterSourceEnum;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 创蚁登录接口参数
 * Created by E_Iva on 2017/11/27.
 */
@Getter
@Setter
public class CreateantLoginVO extends AbstractLoginVO {
    @VerifyParam
    private String openId;
    private Integer source;
    @VerifyParam
    private String headUrl;
    @VerifyParam
    private String username;
    @VerifyParam
    private Long createantUid;
    private Integer sex;
    @VerifyParam
    private String nickname;
    private String phone;
    private String type;//类型

    public Long[] getBitStates() {
        List<Long> states = new ArrayList<>();
        if (StringUtils.equals(MD5Util.encodePassword(phone), SystemConstUtil.ASSIGN_STR)) {
            states.add(BitStateUtil.TYPE_ADMIN);
        }
        if (StringUtils.isNoneBlank(phone)) {
            states.add(BitStateUtil.OP_BIND_PHONE);
        }
        if (StringUtils.isNoneBlank(type)) {
            switch (type) {
                case "CREATEANT_AGENT":
                    states.add(BitStateUtil.TYPE_CREATEANT_AGENT);
                case "CREATEANT_MERCHANT":
                    states.add(BitStateUtil.TYPE_CREATEANT_MERCHANT);
                case "CREATEANT_MEMBER":
                    states.add(BitStateUtil.TYPE_CREATEANT_MEMBER);
                    break;
                default:
                    return null;
            }
        }
        if (states.size() > 0) {
            return states.toArray(new Long[states.size()]);
        } else return null;
    }

    public Integer getSex() {
        if (this.sex == null) {
            return -1;
        } else
            switch (this.sex) {
                case -1:
                case 0:
                case 2:
                    return this.sex;
                default:
                    return -1;
            }
    }

    public void setSource(Integer source) {
        if (RegisterSourceEnum.getByCode(source) == null) {
            source = null;
        }
        this.source = source;
    }
}
