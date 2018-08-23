package com.qnyy.re.base.util.container;


import com.qnyy.re.base.enums.CommonErrorResultEnum;
import com.qnyy.re.base.util.exception.BusinessException;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * dly
 */
@Setter@Getter
public class Response implements Serializable {

    private static final long serialVersionUID = 1L;

    private int status = 0;
    protected String msg;

    public Response() {
        this.msg = "操作成功";
    }

    public Response(String exMsg) {
        this.msg = exMsg;
    }

    public Response(CommonErrorResultEnum resultMessage, String exMessage) {
        this.status = resultMessage.getCode();
        this.msg = exMessage;
    }

    public Response(CommonErrorResultEnum resultMessage) {
        this.status = resultMessage.getCode();
        this.msg = resultMessage.getExMessage();
    }

    public Response(BusinessException businessException){
        this.status = businessException.getErrorCode();
        this.msg = businessException.getExMessage();
    }
}
