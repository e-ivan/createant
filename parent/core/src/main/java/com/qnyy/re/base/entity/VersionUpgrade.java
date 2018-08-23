package com.qnyy.re.base.entity;

import com.qnyy.re.base.util.container.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import org.codehaus.jackson.annotate.JsonIgnore;

import java.util.Date;

/**
 * App版本升级
 */
@Getter@Setter
public class VersionUpgrade extends BaseEntity {

    private Integer versionCode;

    private String versionName;

    @JsonIgnore
    private Date created;

    private boolean must;

    private String apkUrl;

    @JsonIgnore
    private byte appType;

    private String upgradePoint;

}