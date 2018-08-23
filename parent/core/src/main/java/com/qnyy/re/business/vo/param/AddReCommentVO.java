package com.qnyy.re.business.vo.param;

import com.qnyy.re.base.util.annotation.VerifyParam;
import com.qnyy.re.base.util.container.BaseParamVO;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by E_Iva on 2017.12.5.0005.
 */
@Getter@Setter
public class AddReCommentVO extends BaseParamVO {
    @VerifyParam
    private Long reId;
    private Long parentId;
    @VerifyParam
    private String content;
}
