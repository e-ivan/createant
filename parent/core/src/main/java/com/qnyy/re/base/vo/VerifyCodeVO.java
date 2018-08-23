package com.qnyy.re.base.vo;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * 手机验证码
 */
@Document(collection = "verifyCode")
@Getter
public class VerifyCodeVO {
    public static final Integer VALID_MINUTE = 5;//最长验证分钟数
    public static final Integer CANNOT_RESEND_MINUTE = 1;//最快获取分钟数
    @Id
    private String id;
    private String code;        //验证码
    private String phone; //绑定手机号码
    private Date sendTime;    //发生时间
    @Setter
    private boolean use;

    public VerifyCodeVO(String code, String phone, Date sendTime) {
        this.code = code;
        this.phone = phone;
        this.sendTime = sendTime;
    }
}
