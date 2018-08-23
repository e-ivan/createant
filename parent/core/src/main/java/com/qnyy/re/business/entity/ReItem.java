package com.qnyy.re.business.entity;

import com.qnyy.re.base.util.container.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import org.codehaus.jackson.annotate.JsonIgnore;

import java.math.BigDecimal;
import java.util.Date;

@Getter@Setter
public class ReItem extends BaseEntity {

    private Long reId;

    private Long uid;

    private Integer state;

    @JsonIgnore
    private Integer type;

    private Date drawTime;

    private BigDecimal amount;

    private String nickname;

    private String headUrl;

    @JsonIgnore
    private String location;

    @JsonIgnore
    private String lng;

    @JsonIgnore
    private String lat;

    @JsonIgnore
    private Integer version;

}