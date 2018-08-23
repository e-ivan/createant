package com.qnyy.re.business.vo;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * 微信公众号jsapi ticket
 * Created by E_Iva on 2018.6.1 0001.
 */
@Getter@Setter@Document(collection = "WxMpJSApiTicket")
public class WxMpJSApiTicketVO {

    private String ticket;

    private Integer expiresIn;

    private Date created;

    private Integer errcode;
}
