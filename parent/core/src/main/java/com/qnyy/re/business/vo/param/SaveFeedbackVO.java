package com.qnyy.re.business.vo.param;

import com.qnyy.re.base.util.annotation.VerifyParam;
import com.qnyy.re.base.util.container.BaseParamVO;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by E_Iva on 2017.12.19.0019.
 */
@Getter@Setter
public class SaveFeedbackVO extends BaseParamVO {
    private Long uid;

    private String phone;

    private String ip;

    private String email;

    private Integer type;

    private String qq;

    @VerifyParam
    private String content;
}
