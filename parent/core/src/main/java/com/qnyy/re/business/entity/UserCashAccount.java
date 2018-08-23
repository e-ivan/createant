package com.qnyy.re.business.entity;

import com.qnyy.re.base.util.container.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import org.codehaus.jackson.annotate.JsonIgnore;

import java.util.Date;

@Getter@Setter
public class UserCashAccount extends BaseEntity {
    @JsonIgnore
    private Long uid;

    private String alipayAccount;

    private String accountName;

    private String phone;

    private String remark;

    @JsonIgnore
    private Date created;

    @JsonIgnore
    private Date updated;


}