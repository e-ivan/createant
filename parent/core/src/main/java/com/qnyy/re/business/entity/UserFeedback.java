package com.qnyy.re.business.entity;

import com.qnyy.re.base.util.container.BaseAuditEntity;
import lombok.Getter;
import lombok.Setter;
@Getter@Setter
public class UserFeedback extends BaseAuditEntity{
    private String phone;

    private String email;

    private Integer type;

    private String ip;

    private String qq;

    private String content;
}