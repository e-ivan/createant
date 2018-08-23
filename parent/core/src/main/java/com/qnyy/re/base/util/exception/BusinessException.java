package com.qnyy.re.base.util.exception;


import com.qnyy.re.base.enums.CommonErrorResultEnum;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MessageFormatter;

/**
 * Created by dly on 2016/8/3.
 */
@Getter@Setter
public class BusinessException extends RuntimeException{
    private Integer errorCode;
    private String exMessage;

    public BusinessException(CommonErrorResultEnum errorResult, Object... args){
        super(formatErrorMsg(errorResult, args));
        this.errorCode = errorResult.getCode();
        this.exMessage = super.getMessage();
    }

    public BusinessException(CommonErrorResultEnum errorResult) {
        super(formatErrorMsg(errorResult, null));
        this.errorCode = errorResult.getCode();
        this.exMessage = super.getMessage();
    }

    private static String formatErrorMsg(CommonErrorResultEnum errorResult, Object... args) {
        if (args == null || args.length <= 0) {
            return errorResult.getExMessage();
        }
        if (args.length == 1 && args[0] != null && !errorResult.getExMessage().contains("{}")) {
            return errorResult.getExMessage() + "," + args[0];
        }
        FormattingTuple ft = MessageFormatter.arrayFormat(errorResult.getExMessage(), args);
        return ft.getMessage();
    }
}
