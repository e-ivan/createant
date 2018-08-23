package com.qnyy.re.base.util.container;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 审核基类
 * Created by E_Iva on 2017.12.16.0016.
 */
@Setter@Getter
abstract public class BaseAuditEntity extends BaseEntity {
    protected Integer state;

    protected Long applierId;   //申请用户

    protected Long auditorId;   //审核用户

    protected Date auditTime;     //审核时间

    protected String remark;      //审核留言

    protected Integer version;

    protected Date created;
}
