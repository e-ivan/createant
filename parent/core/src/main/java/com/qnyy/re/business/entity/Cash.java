package com.qnyy.re.business.entity;

import com.qnyy.re.base.util.container.BaseAuditEntity;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
@Getter@Setter
public class Cash extends BaseAuditEntity {

    private String note;

    private BigDecimal amount;
}