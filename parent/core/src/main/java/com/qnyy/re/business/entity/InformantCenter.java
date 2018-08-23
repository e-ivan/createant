package com.qnyy.re.business.entity;

import com.qnyy.re.base.util.container.BaseAuditEntity;
import lombok.Getter;
import lombok.Setter;

@Setter@Getter
public class InformantCenter extends BaseAuditEntity {

    private Long defendant;

    private String email;

    private String ip;

    private String qq;

    private String phone;

    private Integer type;

    private String content;

}