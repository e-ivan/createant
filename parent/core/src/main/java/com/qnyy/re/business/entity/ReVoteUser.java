package com.qnyy.re.business.entity;

import com.qnyy.re.base.util.container.BaseEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * 红包点赞
 */
@Getter@Setter
public class ReVoteUser extends BaseEntity{
    private Long reId;

    private Long uid;

    private boolean state;
}