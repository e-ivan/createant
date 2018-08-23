package com.qnyy.re.business.entity;

import com.qnyy.re.base.util.container.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import org.codehaus.jackson.annotate.JsonIgnore;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by E_Iva on 2017.11.30 0030.
 */
@Setter@Getter
public class BaseOrder extends BaseEntity{

    private Long uid;

    private String orderId;

    private Integer state;

    private BigDecimal amount;

    private BigDecimal payAmount;

    private Date payTime;

    private String payType;

    private String outTradeNo;

    private String type;

    private Date created;

    @JsonIgnore
    private Integer version;
}
