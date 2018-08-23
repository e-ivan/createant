package com.qnyy.re.base.entity;

import com.qnyy.re.base.util.container.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import org.codehaus.jackson.annotate.JsonIgnore;

import java.util.Date;

/**
 * 系统广播
 */
@Getter@Setter
public class SysBroadcast extends BaseEntity {

    private String url;

    private Integer type;

    private Integer location;

    @JsonIgnore
    private Date created;

    @JsonIgnore
    private Integer sequence;

    private String title;

    @JsonIgnore
    private Integer state;

}