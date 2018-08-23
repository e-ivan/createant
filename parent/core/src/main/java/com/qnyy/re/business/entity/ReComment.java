package com.qnyy.re.business.entity;

import com.qnyy.re.base.util.container.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import org.codehaus.jackson.annotate.JsonIgnore;

import java.util.Date;
import java.util.List;

@Getter@Setter
public class ReComment extends BaseEntity{

    private Long reId;

    private Long uid;

    private String nickname;

    private String headUrl;

    @JsonIgnore
    private Long parentId;

    private Long replyUid;

    private String replyNickname;

    private String replyHeadUrl;

    private Date created;

    @JsonIgnore
    private Integer state;

    private String content;

    @JsonIgnore
    private Integer replyCount;

    private List<ReComment> replys;
}