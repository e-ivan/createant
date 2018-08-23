package com.qnyy.re.business.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.qnyy.re.base.entity.UserInfo;
import com.qnyy.re.base.entity.UserStatistics;
import com.qnyy.re.base.enums.CommonErrorResultEnum;
import com.qnyy.re.base.service.IUserInfoService;
import com.qnyy.re.base.util.BitStateUtil;
import com.qnyy.re.base.util.container.PageResult;
import com.qnyy.re.base.util.exception.BusinessException;
import com.qnyy.re.business.entity.ShakearoundDevice;
import com.qnyy.re.business.entity.ShakearoundPage;
import com.qnyy.re.business.enums.ReMomentStateEnum;
import com.qnyy.re.business.enums.ShakearoundApplyDeviceStatusEnum;
import com.qnyy.re.business.enums.ShakearoundDeviceStatus;
import com.qnyy.re.business.mapper.ReMomentMapper;
import com.qnyy.re.business.mapper.ShakearoundDeviceMapper;
import com.qnyy.re.business.mapper.ShakearoundPageMapper;
import com.qnyy.re.business.query.ShakearoundDeviceQueryObject;
import com.qnyy.re.business.service.IWeiXinMpService;
import com.qnyy.re.business.util.CreateantRequestUtil;
import com.qnyy.re.business.util.WeiXinMpRequestUtil;
import com.qnyy.re.business.vo.ShakearoundApplyDeviceVO;
import com.qnyy.re.business.vo.param.BindDeviceVO;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by E_Iva on 2018.5.24 0024.
 */
@Service
public class WeiXinMpServiceImpl implements IWeiXinMpService {
    private static String pageUrl;
    private static String description;
    private static String iconUrl;


    @Value("${createantPageUrl}")
    public void setPageUrl(String pageUrl) {
        WeiXinMpServiceImpl.pageUrl = pageUrl;
    }

    @Value("${createantIconUrl}")
    public void setIconUrl(String iconUrl) {
        WeiXinMpServiceImpl.iconUrl = iconUrl;
    }

    @Value("${createantPageDescription}")
    public void setDescription(String pageDescription) {
        WeiXinMpServiceImpl.description = pageDescription;
    }

    @Autowired
    private MongoTemplate template;
    @Autowired
    private ShakearoundDeviceMapper shakearoundDeviceMapper;
    @Autowired
    private ShakearoundPageMapper shakearoundPageMapper;
    @Autowired
    private ReMomentMapper reMomentMapper;
    @Autowired
    private IUserInfoService userInfoService;

    @Override
    public void applyShakearoundDevice(Integer count, String reason, String comment) {
        //查询是否有正在申请设备
        if (this.getApplyingDevice() != null) {
            throw new BusinessException(CommonErrorResultEnum.MP_HAS_DEVICE_APPLYING);
        }
        if (count == null || count <= 0 || count > 50) {
            throw new BusinessException(CommonErrorResultEnum.REQUEST_PARAM_LACK, "count:单次申请数量必须在1~50之间");
        }
        JSONObject deviceApply = WeiXinMpRequestUtil.deviceApply(count, reason, comment);
        //创建申请设备对象
        ShakearoundApplyDeviceVO ad = new ShakearoundApplyDeviceVO();
        ad.setApplyComment(comment);
        ad.setApplyId(deviceApply.getInteger("apply_id"));
        ad.setApplyTime(new Date());
        ad.setApplyReason(reason);
        ad.setAuditComment(deviceApply.getString("audit_comment"));
        ad.setAuditStatus(deviceApply.getInteger("audit_status"));
        ad.setQuantity(count);
        template.insert(ad);
    }

    //查询正在申请设备
    private ShakearoundApplyDeviceVO getApplyingDevice() {
        return template.findOne(new Query(Criteria.where("auditStatus").is(ShakearoundApplyDeviceStatusEnum.AUDIT.getCode())),
                ShakearoundApplyDeviceVO.class);
    }

    @Override
    public void addApplyPassDevice() {
        ShakearoundApplyDeviceVO applyingDevice = this.getApplyingDevice();
        if (applyingDevice != null) {
            //查询审核状态
            JSONObject jsonObject = WeiXinMpRequestUtil.deviceApplyStatus(applyingDevice.getApplyId());
            Integer status = jsonObject.getInteger("audit_status");
            //不是审核状态处理
            if (!Objects.equals(status, ShakearoundApplyDeviceStatusEnum.AUDIT.getCode())) {
                //如果审核通过
                if (Objects.equals(status, ShakearoundApplyDeviceStatusEnum.PASS.getCode())) {
                    //查询该申请批次的所有设备，并保存对象信息
                    JSONArray deviceJsonArray = WeiXinMpRequestUtil.deviceSearch(applyingDevice.getApplyId(), applyingDevice.getQuantity()).getJSONArray("devices");
                    //封装对象
                    List<ShakearoundDevice> list = deviceJsonArray.stream().map(a -> {
                        JSONObject device = JSON.parseObject(a.toString());
                        ShakearoundDevice shakearoundDevice = new ShakearoundDevice();
                        shakearoundDevice.setApplyId(applyingDevice.getApplyId());
                        shakearoundDevice.setDeviceId(device.getInteger("device_id"));
                        shakearoundDevice.setMajor(device.getInteger("major"));
                        shakearoundDevice.setMinor(device.getInteger("minor"));
                        shakearoundDevice.setStatus(device.getInteger("status"));
                        shakearoundDevice.setUuid(device.getString("uuid"));
                        shakearoundDevice.setApplyTime(applyingDevice.getApplyTime());
                        return shakearoundDevice;
                    }).collect(Collectors.toList());
                    shakearoundDeviceMapper.insertBatch(list);
                }
                //更新审核状态和信息
                Update update = new Update();
                update.set("auditStatus", status);
                update.set("auditComment", jsonObject.getString("audit_comment"));
                long timestamp = jsonObject.getLong("audit_time") * 1000;
                update.set("auditTime", new Date(timestamp));
                template.updateFirst(new Query(Criteria.where("applyId").is(applyingDevice.getApplyId())), update, ShakearoundApplyDeviceVO.class);
            }
        }

    }

    @Override
    public PageResult<ShakearoundDevice> queryShakearoundDevice(ShakearoundDeviceQueryObject qo) {
        return new PageResult<>(shakearoundDeviceMapper.query(qo), shakearoundDeviceMapper.queryCount(qo),
                qo.getCurrentPage(), qo.getPageSize());
    }

    @Override
    public void writeIBeacon2Device(Long did, String iBeaconId) {
        ShakearoundDevice device = shakearoundDeviceMapper.selectByPrimaryKey(did);
        if (device == null) {
            throw new BusinessException(CommonErrorResultEnum.OBJECT_UN_EXIST);
        }
        if (device.getUid() != null) {
            throw new BusinessException(CommonErrorResultEnum.OBJECT_NOP);
        }
        device.setIBeaconId(iBeaconId);
        shakearoundDeviceMapper.updateByPrimaryKey(device);
    }

    public ShakearoundDevice getShakearoundDevice(String iBeaconId) {
        ShakearoundDevice device = shakearoundDeviceMapper.selectByIBeaconId(iBeaconId);
        if (device == null) {
            throw new BusinessException(CommonErrorResultEnum.OBJECT_UN_EXIST);
        }
        return device;
    }

    @Override
    public List<Long> queryUserStoreIds(Long uid) {
        return shakearoundDeviceMapper.queryStoreIdByUid(uid);
    }

    @Override
    public void bindIBeacon2User(BindDeviceVO vo) {
        ShakearoundDevice device = this.getShakearoundDevice(vo.getIBeaconId());
        if (device.getUid() != null) {
            throw new BusinessException(CommonErrorResultEnum.MP_DEVICE_HAD_BIND);
        }
        if (vo.getStoreId() != 0 && CreateantRequestUtil.getStoreInfo(vo.getStoreId(), false) == null) {
            throw new BusinessException(CommonErrorResultEnum.OBJECT_NOP, "storeId:" + vo.getStoreId());
        }
        //获取用户
        UserInfo user = userInfoService.getByUid(vo.getUid());
        if (!user.getCreateantMember()) {
            throw new BusinessException(CommonErrorResultEnum.RE_NOT_CREATEANT);
        }
        device.setUid(user.getUid());
        device.setStatus(ShakearoundDeviceStatus.BIND.getCode());
        device.setStoreId(vo.getStoreId());
        device.setBindTime(new Date());
        shakearoundDeviceMapper.updateByPrimaryKey(device);
        user.addState(BitStateUtil.OP_HAS_BIND_DEVICE);
        userInfoService.update(user);
        userInfoService.addUserStatisticsCount(user.getUid(), UserStatistics.CountField.DEVICE_COUNT);
        ShakearoundPage page = this.createShakearoundPage(vo);
        //关联页面
        WeiXinMpRequestUtil.deviceBindPage(device.getDeviceId(), page.getPageId());
    }

    /**
     * 创建周边设备页面
     */
    private ShakearoundPage createShakearoundPage(BindDeviceVO vo) {
        //查询是否存在该页面，存在直接返回
        ShakearoundPage page = shakearoundPageMapper.selectByCondition(vo.getStoreId(), vo.getUid());
        //不存在重新去申请
        if (page == null) {
            String comment;
            String urlParam;
            if (vo.getStoreId() == 0) {
                comment = "会员ID:" + vo.getUid();
                urlParam = "?uid=" + vo.getUid();

            } else {
                comment = "店铺ID:" + vo.getStoreId();
                urlParam = "?storeid=" + vo.getStoreId();
            }
            page = new ShakearoundPage();
            page.setComment(comment);
            page.setDescription(StringUtils.isEmpty(vo.getDescription()) ? description : vo.getDescription());
            page.setTitle(vo.getPageTitle());
            page.setIconUrl(vo.isUrl(vo.getPageIconUrl()) ? vo.getPageIconUrl() : iconUrl);
            page.setPageUrl(pageUrl + urlParam);
            //上传到微信
            Integer pageId = WeiXinMpRequestUtil.addDevicePage(page);
            page.setPageId(pageId);
            page.setUid(vo.getUid());
            page.setStoreId(vo.getStoreId());
            //保存页面
            shakearoundPageMapper.insert(page);
        }
        return page;
    }

    @Override
    public void unbindIBeacon2User(String iBeaconId, Long uid) {
        ShakearoundDevice device = this.getShakearoundDevice(iBeaconId);
        if (!Objects.equals(device.getUid(), uid)) {
            throw new BusinessException(CommonErrorResultEnum.MP_UN_BIND_FAIL, "unmatch user");
        }
        /*ReMomentQueryObject qo = new ReMomentQueryObject();
        qo.setQueryType(ReMomentQueryObject.QUERY_PAY_RE);
        qo.setStoreId(device.getStoreId());
        qo.setStates(new int[]{ReMomentStateEnum.NORMAL.getCode()});
        if (reMomentMapper.queryCount(qo) > 0 && this.queryUserDevices(uid,device.getStoreId()).size() <= 1) {//当店铺红包大于0且店铺设备数小于等于1时不能解绑
            throw new BusinessException(CommonErrorResultEnum.MP_UN_BIND_FAIL, "The device has started and must exist");
        }*/
        Long queryUid = device.getUid();
        Long queryStoreId = device.getStoreId();
        device.setUid(null);
        device.setBindTime(null);
        device.setStoreId(null);
        device.setStatus(ShakearoundDeviceStatus.UN_BIND.getCode());
        shakearoundDeviceMapper.updateByPrimaryKey(device);
        //解绑页面
        WeiXinMpRequestUtil.deviceBindPage(device.getDeviceId(), null);
        UserInfo user = userInfoService.getByUid(uid);
        UserStatistics userStatistics = userInfoService.getStatisticsByUid(uid);
        userStatistics.setDeviceCount(userStatistics.getDeviceCount() - 1);
        userInfoService.updateUserStatistics(userStatistics);
        if (userStatistics.getDeviceCount() <= 0) {
            ShakearoundPage page = shakearoundPageMapper.selectByCondition(queryStoreId,queryUid);
            //删除页面
            try {
                WeiXinMpRequestUtil.delDevicePage(page.getPageId());
                //删除页面
                shakearoundPageMapper.deleteByPrimaryKey(page.getId());
            } catch (Exception e) {
                e.printStackTrace();
            }
            user.removeState(BitStateUtil.OP_HAS_BIND_DEVICE);
            userInfoService.update(user);
        }
    }

    @Override
    public List<ShakearoundDevice> queryUserDevices(Long uid, Long storeId) {
        return shakearoundDeviceMapper.queryByUid(uid, storeId);
    }

    @Override
    public Long getDeviceReMomentId(Integer major, Integer minor) {
        return shakearoundDeviceMapper.getDeviceReMomentId(major, minor, ReMomentStateEnum.NORMAL.getCode());
    }

    @Override
    public int deleteDevices(List<Long> ids) {
        List<ShakearoundDevice> devices = shakearoundDeviceMapper.selectBatch(ids);
        Map<Long, Integer> map = devices.stream().filter(d -> StringUtils.isBlank(d.getIBeaconId())).collect(Collectors.toMap(ShakearoundDevice::getId, ShakearoundDevice::getDeviceId));
        //标记删除
        map.forEach((k, v) -> WeiXinMpRequestUtil.deviceUpdateComment(v, "标记删除"));
        if (CollectionUtils.isNotEmpty(map.keySet())) {
            shakearoundDeviceMapper.deleteBatch(map.keySet());
        }
        return map.size();
    }

}
