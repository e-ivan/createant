package com.qnyy.re.base.entity;

import com.qnyy.re.base.util.container.BaseUserEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter@Getter
public class UserToken extends BaseUserEntity {

    private String token;

    private String deviceToken;

    private String rongYunToken;

    private Integer platform;

    private String appVersion;

    private Date loginTime;

    private Date updateTime;

}