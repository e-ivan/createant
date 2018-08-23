package com.qnyy.re.base.entity;

import com.qnyy.re.base.util.container.BaseUserEntity;
import lombok.Getter;
import lombok.Setter;
import org.codehaus.jackson.annotate.JsonIgnore;

import java.math.BigDecimal;
import java.util.Date;

@Getter@Setter
public class UserAccount extends BaseUserEntity {

    private String creantOpenId;

    private BigDecimal balance;

    private BigDecimal freezeBalance;

    private BigDecimal refuseCash;

    @JsonIgnore
    private Integer version;

    @JsonIgnore
    private Date created;

    @JsonIgnore
    private Date updated;


}