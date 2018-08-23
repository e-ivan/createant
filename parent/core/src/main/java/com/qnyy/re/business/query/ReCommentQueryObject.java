package com.qnyy.re.business.query;

import com.qnyy.re.base.util.container.QueryObject;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by E_Iva on 2017.12.5.0005.
 */
@Getter@Setter
public class ReCommentQueryObject extends QueryObject {
    private Long reId = -1L;
    private Integer state = -1;
    private Long parentId = -1L;

}
