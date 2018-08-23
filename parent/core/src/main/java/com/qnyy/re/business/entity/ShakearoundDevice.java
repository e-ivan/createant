package com.qnyy.re.business.entity;

import com.qnyy.re.base.util.container.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 摇一摇设备信息
 * Created by E_Iva on 2018.5.24 0024.
 */
@Getter@Setter
public class ShakearoundDevice extends BaseEntity {
    private String iBeaconId;   //硬件id
    private Integer deviceId;    //微信指定设备id
    private Integer status;     //激活状态，0：未激活，1：已激活
    private Integer minor;
    private Integer major;
    private String uuid;
    private Long uid;           //拥有该设备的用户id
    private Integer applyId;    //申请批次id
    private Date applyTime;
    private Long storeId;       //门店id
    private Date bindTime;      //绑定时间
    private Double distance;    //距离，在获取摇周边的设备及用户信息时使用
}
