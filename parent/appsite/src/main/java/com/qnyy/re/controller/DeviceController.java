package com.qnyy.re.controller;

import com.qnyy.re.base.util.UserContext;
import com.qnyy.re.base.util.annotation.ApiDocument;
import com.qnyy.re.base.util.annotation.UnRequiredLogin;
import com.qnyy.re.base.util.container.ObjectResponse;
import com.qnyy.re.base.util.container.Response;
import com.qnyy.re.business.entity.ShakearoundDevice;
import com.qnyy.re.business.query.ShakearoundDeviceQueryObject;
import com.qnyy.re.business.util.CreateantRequestUtil;
import com.qnyy.re.business.vo.StoreVO;
import com.qnyy.re.business.vo.param.BindDeviceVO;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 设备控制器
 * Created by E_Iva on 2018.6.6 0006.
 */
@RestController
@RequestMapping("device")
public class DeviceController extends BaseController {

    /**
     * 申请周边设备
     */
    @RequestMapping(value = "applyDevice", method = RequestMethod.POST)
    @UnRequiredLogin
    @ApiDocument("申请周边设备")
    public Response applyDevice(Integer count, String reason, String comment) {
        weiXinMpService.applyShakearoundDevice(count, reason, comment);
        return new Response("申请成功");
    }

    /**
     * 查询周边设备
     */
    @RequestMapping(value = "queryDevice", method = RequestMethod.POST)
    @ApiDocument("查询周边设备")
    @UnRequiredLogin
    public Response queryDevice(ShakearoundDeviceQueryObject qo) {
        weiXinMpService.addApplyPassDevice();//先更新
        return new ObjectResponse<>(weiXinMpService.queryShakearoundDevice(qo));
    }

    @RequestMapping(value = "delDevice", method = RequestMethod.POST)
    @ApiDocument("删除周边设备")
    @UnRequiredLogin
    public Response delDevice(Long[] ids) {
        int i = weiXinMpService.deleteDevices(Arrays.asList(ids));
        return new Response("已删除" + i + "个设备");
    }

    /**
     * 绑定IBeacon
     */
    @RequestMapping(value = "bindIBeacon", method = RequestMethod.POST)
    @ApiDocument("绑定IBeacon")
    @UnRequiredLogin
    public Response bindIBeacon(Long did, String iBeaconId) {
        weiXinMpService.writeIBeacon2Device(did, iBeaconId);
        return new Response("绑定成功");
    }

    /**
     * 用户绑定周边设备
     */
    @RequestMapping(value = "bindDevice", method = RequestMethod.POST)
    @ApiDocument("用户绑定周边设备")
    public Response bindDevice(BindDeviceVO vo) {
        vo.setUid(UserContext.getUserId());
        weiXinMpService.bindIBeacon2User(vo);
        return new Response("绑定成功");
    }

    /**
     * 用户解绑周边设备
     */
    @RequestMapping(value = "unbindDevice", method = RequestMethod.POST)
    @ApiDocument("用户解绑周边设备")
    @UnRequiredLogin
    public Response unbindDevice(Long uid,String iBeaconId) {
        if (UserContext.getUserId() != null) {
            uid = UserContext.getUserId();
        }
        weiXinMpService.unbindIBeacon2User(iBeaconId, uid);
        return new Response("解绑成功");
    }

    /**
     * 查询用户店铺
     */
    @RequestMapping(value = "queryUserStore", method = {RequestMethod.POST, RequestMethod.GET})
    @ApiDocument("查询用户店铺")
    public Response queryUserStore() {
        List<Long> storeIds = weiXinMpService.queryUserStoreIds(UserContext.getUserId());
        List<StoreVO> storeInfo = CreateantRequestUtil.getStoreInfo(storeIds,false);
        if (storeIds.contains(0L)) {
            storeInfo.add(new StoreVO(0L,"不选择店铺"));
        }
        return new ObjectResponse<>(storeInfo);
    }

    /**
     * 查询用户设备
     */
    @RequestMapping(value = "queryUserDevice", method = {RequestMethod.POST, RequestMethod.GET})
    @ApiDocument("查询用户设备")
    public Response queryUserDevice(Long storeId) {
        List<ShakearoundDevice> devices = weiXinMpService.queryUserDevices(UserContext.getUserId(), storeId);
        Map<Long, List<ShakearoundDevice>> collect = devices.stream().collect(Collectors.groupingBy(ShakearoundDevice::getStoreId));
        Set<Long> storeIds = collect.keySet();
        List<StoreVO> storeInfo = CreateantRequestUtil.getStoreInfo(storeIds,false);
        storeInfo.forEach(s -> s.setDevices(collect.get(s.getStoreId())));
        if (collect.containsKey(0L)) {
            StoreVO vo = new StoreVO(0L,"我的设备");
            vo.setDevices(collect.get(0L));
            storeInfo.add(vo);
        }
        return new ObjectResponse<>(storeInfo);
    }


}
