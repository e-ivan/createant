package com.qnyy.re.business.vo.param;

import com.qnyy.re.base.util.annotation.VerifyParam;
import com.qnyy.re.base.util.container.BaseParamVO;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * Created by E_Iva on 2017.11.29 0029.
 */
@Setter@Getter
public class CreateSingleReVO extends BaseParamVO {
    private String content;
    @VerifyParam
    private BigDecimal amount;
}
