package com.qnyy.re.business.vo.param;

import com.qnyy.re.base.util.annotation.VerifyParam;
import com.qnyy.re.base.util.container.BaseParamVO;
import com.qnyy.re.business.enums.InformantCenterTypeEnum;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by E_Iva on 2017.12.19.0019.
 */
@Setter@Getter
public class CreateInformantVO extends BaseParamVO {

    private Long applierId;

    @VerifyParam
    private Long uid;//被举报的用户

    private String email;

    private String qq;

    private String ip;

    private String phone;

    @VerifyParam
    private Integer type;

    @VerifyParam
    private String content;

    public void setType(Integer type){
        if (InformantCenterTypeEnum.getByCode(type) == null) {
            type = null;
        }
        this.type = type;
    }

}
