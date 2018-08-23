package com.qnyy.re.base.entity;

import com.qnyy.re.base.util.container.BaseUserEntity;
import lombok.Getter;
import lombok.Setter;

@Setter@Getter
public class LoginInfo extends BaseUserEntity {
    private String username;

    private String password;

    private String phone;

    private Integer state;

    private Integer userType;

    private String wechatToken;

    private String createantToken;

}