package com.qnyy.re.base.entity;

import com.qnyy.re.base.util.container.BaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

/**
 * 商户信息
 */
@Getter@Setter@NoArgsConstructor
public class MchInfo extends BaseEntity {
    public MchInfo(String mchName, String mchId, Integer tokenSource) {
        this.mchName = mchName;
        this.mchId = mchId;
        this.tokenSource = tokenSource;
    }

    private String mchName;

    private String mchId;

    private String mchKey;

    private Byte state;

    private String apiPermissions;

    private Integer tokenSource;//对应注册源RegisterSourceEnum

    private Date created;

    private Date updated;
}