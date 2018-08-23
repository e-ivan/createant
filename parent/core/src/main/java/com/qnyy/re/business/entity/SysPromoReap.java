package com.qnyy.re.business.entity;

import com.qnyy.re.base.util.container.BaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.codehaus.jackson.annotate.JsonIgnore;

import java.math.BigDecimal;
import java.util.Date;
@Getter@Setter
@NoArgsConstructor
public class SysPromoReap extends BaseEntity{
    public SysPromoReap(BigDecimal firstReapRatio, BigDecimal secondReapRatio) {
        this.firstReapRatio = firstReapRatio;
        this.secondReapRatio = secondReapRatio;
    }

    private BigDecimal firstReapRatio;

    private BigDecimal secondReapRatio;

    @JsonIgnore
    private Date created;

}