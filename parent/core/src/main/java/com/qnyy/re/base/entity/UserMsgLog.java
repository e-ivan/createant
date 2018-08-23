package com.qnyy.re.base.entity;

import com.qnyy.re.base.enums.UserMsgLogTypeEnum;
import com.qnyy.re.base.util.container.BaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.codehaus.jackson.annotate.JsonIgnore;

import java.util.Date;

/**
 * 用户消息日志
 */
@Setter@Getter
@NoArgsConstructor
public class UserMsgLog extends BaseEntity {

    public UserMsgLog(Long from, Long to, Integer type, Long objectId, String content) {
        this.from = from;
        this.to = to;
        this.type = type;
        this.objectId = objectId;
        this.content = content;
    }

    @JsonIgnore
    private Long from;

    @JsonIgnore
    private Long to;

    private UserInfo fromUser;

    private Integer type;

    private Long objectId;

    private Date created;

    private Integer state;

    private String content;

    public String getTitle(){
        return UserMsgLogTypeEnum.getTitleByCode(type);
    }

}