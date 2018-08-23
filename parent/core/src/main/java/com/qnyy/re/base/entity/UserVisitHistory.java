package com.qnyy.re.base.entity;

import com.qnyy.re.base.util.container.BaseUserEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * 用户访问历史
 */
@Getter@Setter
public class UserVisitHistory extends BaseUserEntity {

    private Long visitor;

    private Integer time;

}