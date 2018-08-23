package com.qnyy.re.business.entity;

import com.qnyy.re.base.entity.UserInfo;
import com.qnyy.re.base.util.container.BaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.codehaus.jackson.annotate.JsonIgnore;

import java.math.BigDecimal;
import java.util.Date;

@Getter@Setter
@NoArgsConstructor
public class UserClassRelation extends BaseEntity {
    public UserClassRelation(Long upper, Long junior) {
        this.upper = upper;
        this.junior = junior;
    }
    @JsonIgnore
    private Long upper;

    @JsonIgnore
    private Long junior;

    private UserInfo juniorUser;

    private BigDecimal reap;

    private BigDecimal secondReap;

    private Integer reapCount;

    private Date created;
}