package com.qnyy.re.base.vo;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * 用户登录日志
 * Created by E_Iva on 2017.12.21.0021.
 */
@Document(collection = "userLoginLog")
@Getter@Setter
public class UserLoginLogVO {
    public static final Integer STATE_SUCCESS = 1;
    public static final Integer STATE_FAULT = 0;

    public static final Integer TYPE_REGISTER = 0;//注册
    public static final Integer TYPE_PHONE = 1;//手机登录
    public static final Integer TYPE_WECAHT = 2;//微信登录
    public static final Integer TYPE_CREATEANT = 3;//创蚁登录
    @Id
    private String id;
    private String loginPhone;
    private String loginOpenId;
    private Integer type;
    private Long uid;
    private String ip;
    @Indexed(expireAfterSeconds = 30 * 24 * 60 * 60)
    private Date loginTime;
    private Integer state;
    private String msg;
    private String address;
    private String lng;
    private String lat;
}
