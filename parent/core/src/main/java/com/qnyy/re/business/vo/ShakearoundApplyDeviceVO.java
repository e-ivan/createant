package com.qnyy.re.business.vo;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * 周边申请设备
 * Created by E_Iva on 2018.5.24 0024.
 */
@Getter
@Setter
@Document(collection = "deviceApply")
public class ShakearoundApplyDeviceVO {
    private Integer applyId;    //申请id
    private Integer quantity;   //申请数量
    private Date applyTime;     //申请时间
    private String applyReason; //申请理由
    private String applyComment;//申请备注
    private Integer auditStatus;//审核状态。 0：审核未通过、 1：审核中、 2：审核已通过；
    private String auditComment;//审核备注
    private Date auditTime;     //审核时间
}
