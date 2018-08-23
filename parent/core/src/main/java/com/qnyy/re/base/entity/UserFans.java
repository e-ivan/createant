package com.qnyy.re.base.entity;

import com.qnyy.re.base.util.container.BaseUserEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * 用户关注
 */
@Getter@Setter
public class UserFans extends BaseUserEntity {

    private Long fan;

    private boolean state;
}