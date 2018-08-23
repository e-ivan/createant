package com.qnyy.re.base.entity;

import com.qnyy.re.base.util.AccountUtil;
import com.qnyy.re.base.util.container.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import org.codehaus.jackson.annotate.JsonIgnore;

import java.math.BigDecimal;
import java.util.Date;
@Getter@Setter
public class UserAccountStatement extends BaseEntity{

    @JsonIgnore
    private Long accountUser;

    private Date created;

    private String accountType;

    private String remark;

    private BigDecimal amount;

    private BigDecimal usableAmount;

    private BigDecimal freezeAmount;

    private Integer dealType;

    private Boolean inOrOut;

    private Long objectId;

    //获取运算符
    public Boolean getSymbol(){
        return AccountUtil.AccountDealType.getSymbol(dealType);
    }

}