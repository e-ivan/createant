package com.qnyy.re.base.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 用户消息日志类型
 * Created by E_Iva on 2017.12.13.0013.
 */
@Getter@AllArgsConstructor
public enum UserMsgLogTypeEnum {
    RE_VOTE(0,"reVote","TA赞了你"),//红包点赞
    RE_DRAW(1,"reDraw","TA领取了你的红包"),//红包领取
    SINGLE_RE_DRAW(2,"singleReDraw","TA领取了你的红包"),//敲门红包领取
    RE_COMMENT(3,"reComment","TA评论了你的红包"),//红包评论
    ;

    public static String getTitleByCode(Integer code){
        for (UserMsgLogTypeEnum typeEnum : UserMsgLogTypeEnum.values()) {
            if (typeEnum.getCode().equals(code)){
                return typeEnum.getTitle();
            }
        }
        return null;
    }

    private Integer code;
    private String value;
    private String title;

}
