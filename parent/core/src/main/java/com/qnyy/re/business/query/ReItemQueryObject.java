package com.qnyy.re.business.query;

import com.qnyy.re.base.util.container.QueryObject;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * 红包明细查询对象
 * Created by E_Iva on 2017.12.4.0004.
 */
@Setter@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ReItemQueryObject extends QueryObject{
    private Long reId;
    private List<Long> reIds;
    private Long uid = -1L;
    private Integer state = -1;
    private Integer type = -1;
    private String orderBy;
    private String groupBy;
}
