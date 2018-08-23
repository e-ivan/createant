package com.qnyy.re.business.service;

import com.qnyy.re.base.util.container.PageResult;
import com.qnyy.re.business.entity.ShakearoundDevice;
import com.qnyy.re.business.query.ShakearoundDeviceQueryObject;
import com.qnyy.re.business.vo.param.BindDeviceVO;

import java.util.List;

/**
 * 微信公众平台服务
 * Created by E_Iva on 2018.5.23 0023.
 */
public interface IWeiXinMpService {
    /**
     * 申请周边设备
     *
     * @param count   申请数量
     * @param reason  申请理由
     * @param comment 申请注释
     */
    void applyShakearoundDevice(Integer count, String reason, String comment);

    /**
     * 添加申请通过设备
     */
    void addApplyPassDevice();

    PageResult<ShakearoundDevice> queryShakearoundDevice(ShakearoundDeviceQueryObject qo);

    /**
     * 通过周边设备id获取
     * @param iBeaconId
     */
    ShakearoundDevice getShakearoundDevice(String iBeaconId);

    /**
     * 查询用户店铺Id
     * @param uid
     */
    List<Long> queryUserStoreIds(Long uid);

    /**
     * 写入iBeaconId到设备中
     * @param did   设备序号
     * @param iBeaconId 周边设备id
     */
    void writeIBeacon2Device(Long did, String iBeaconId);

    /**
     * 为用户绑定周边设备
     * 指定创客或招商总监才能绑定
     */
    void bindIBeacon2User(BindDeviceVO vo);

    /**
     * 解绑用户周边设备
     * @param iBeaconId
     * @param uid
     */
    void unbindIBeacon2User(String iBeaconId, Long uid);

    /**
     * 查询用户设备
     */
    List<ShakearoundDevice> queryUserDevices(Long uid, Long storeId);

    Long getDeviceReMomentId(Integer major, Integer minor);

    int deleteDevices(List<Long> ids);
}
