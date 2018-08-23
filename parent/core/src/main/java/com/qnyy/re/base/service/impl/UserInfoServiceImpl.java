package com.qnyy.re.base.service.impl;

import com.qnyy.re.base.entity.*;
import com.qnyy.re.base.enums.CommonErrorResultEnum;
import com.qnyy.re.base.mapper.*;
import com.qnyy.re.base.query.UserFansQueryObject;
import com.qnyy.re.base.query.UserInfoQueryObject;
import com.qnyy.re.base.query.UserStatisticsQueryObject;
import com.qnyy.re.base.query.UserVisitHistoryQueryObject;
import com.qnyy.re.base.service.ILoginInfoService;
import com.qnyy.re.base.service.IUserInfoService;
import com.qnyy.re.base.service.IUserTokenService;
import com.qnyy.re.base.util.BitStateUtil;
import com.qnyy.re.base.util.SystemConstUtil;
import com.qnyy.re.base.util.UserContext;
import com.qnyy.re.base.util.container.MgrPageResult;
import com.qnyy.re.base.util.container.PageResult;
import com.qnyy.re.base.util.exception.BusinessException;
import com.qnyy.re.base.vo.param.UpdateUserInfoVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * Created by E_Iva on 2017/11/24.
 */
@Service
public class UserInfoServiceImpl implements IUserInfoService {
    @Autowired
    private UserInfoMapper mapper;
    @Autowired
    private UserStatisticsMapper userStatisticsMapper;
    @Autowired
    private UserVisitHistoryMapper historyMapper;
    @Autowired
    private UserFansMapper userFansMapper;
    @Autowired
    private UserAccountMapper userAccountMapper;
    @Autowired
    private IUserTokenService userTokenService;
    @Autowired
    private ILoginInfoService loginInfoService;

    @Override
    public UserToken save(Long uid, Long createantUid, String headUrl, String nickname, Integer sex, String lat, String lng, Long[] bitStates) {
        UserInfo userInfo = new UserInfo();
        userInfo.setUid(uid);
        userInfo.setHeadUrl(StringUtils.isBlank(headUrl) ? SystemConstUtil.userDefaultHeadUrl : headUrl);
        userInfo.setNickname(nickname);
        userInfo.setSex(sex);
        userInfo.setLat(lat);
        userInfo.setLng(lng);
        userInfo.setCreateantUid(createantUid);
        userInfo.setIntro(SystemConstUtil.userDefaultIntro);
        userInfo.setPromoCode("C" + String.format("%06d", uid));
        if (bitStates != null) {
            for (Long state : bitStates) {
                userInfo.addState(state);
            }
        }
        mapper.insert(userInfo);
        userStatisticsMapper.insert(uid);
        userAccountMapper.insert(uid);
        //注册融云
        String rongYunToken = null;
        try {
//            rongYunToken = RongYunUtil.registerUserToken(uid,userInfo.getNickname(),userInfo.getHeadUrl());
        } catch (Exception e) {
            System.err.println("融云注册失败");
            e.printStackTrace();
        }
        return userTokenService.save(uid, rongYunToken);
    }

    @Override
    public UserInfo getByUid(Long uid) {
        UserInfo userInfo = mapper.selectByPrimaryKey(uid);
        if (userInfo == null) {
            throw new BusinessException(CommonErrorResultEnum.USER_UN_EXIST);
        }
        return userInfo;
    }

    @Override
    public void update(UserInfo userInfo) {
        int i = mapper.updateByPrimaryKey(userInfo);
        if (i <= 0) {
            throw new BusinessException(CommonErrorResultEnum.OPTIMISTIC_LOCK_ERROR);
        }
    }

    @Override
    public void updateLocation(Long uid, String lng, String lat) {
        mapper.updateUserLocation(uid, lng, lat);
    }


    @Override
    public UserInfo updateUserInfo(UpdateUserInfoVO vo) {
        UserInfo user = mapper.selectByPrimaryKey(UserContext.getUserId());
        if (!vo.getSex().equals(-1)) {
            user.setSex(vo.getSex());
        }
        if (StringUtils.isNoneBlank(vo.getHeadUrl())) {
            user.setHeadUrl(vo.getHeadUrl());
        }
        if (StringUtils.isNoneBlank(vo.getNickname())) {
            user.setNickname(vo.getNickname());
        }
        if (StringUtils.isNoneBlank(vo.getAddress())) {
            user.setAddress(vo.getAddress());
        }
        if (vo.getIntro() != null) {
            user.setIntro(vo.getIntro());
        }
        mapper.updateByPrimaryKey(user);
        if (StringUtils.isNoneBlank(vo.getPhone()) && !user.getBindPhone()) {
            this.loginInfoService.bindUserPhone(vo.getPhone(), user.getUid());
        }
        return user;
    }

    @Override
    public UserStatistics getStatisticsByUid(Long uid) {
        UserStatistics userStatistics = userStatisticsMapper.selectByPrimaryKey(uid);
        if (userStatistics == null) {
            throw new BusinessException(CommonErrorResultEnum.USER_UN_EXIST);
        }
        return userStatistics;
    }

    @Override
    public void updateUserStatistics(UserStatistics userStatistics) {
        userStatisticsMapper.update(userStatistics);
    }

    @Override
    public void addUserStatisticsCount(Long uid, UserStatistics.CountField field) {
        userStatisticsMapper.addCount(uid, field.getValue());
    }

    @Override
    public List<UserInfo> queryUserByStatistics(UserStatisticsQueryObject qo) {
        return userStatisticsMapper.query(qo);
    }

    @Override
    public void addUserHistory(Long uid) {
        if (!uid.equals(UserContext.getUserId())) {
            this.getByUid(uid);
            UserVisitHistory history = historyMapper.selectByUserAndVisitor(uid, UserContext.getUserId());
            if (history == null) {
                historyMapper.insert(uid, UserContext.getUserId());
            } else {
                historyMapper.addTime(history);
            }
        }
        this.addUserStatisticsCount(uid, UserStatistics.CountField.VISITOR_COUNT);
    }

    @Override
    public PageResult<UserInfo> queryUserVisitHistory(UserVisitHistoryQueryObject qo) {
        int count = historyMapper.queryCount(qo);
        List<UserInfo> list = historyMapper.query(qo);
        return new PageResult<>(list, count, qo.getCurrentPage(), qo.getPageSize());
    }

    @Override
    public void handleUserFans(Long uid, boolean add) {
        this.getByUid(uid);
        if (uid.equals(UserContext.getUserId())) {
            throw new BusinessException(CommonErrorResultEnum.OWN2OWN);
        }
        UserFans userFans = userFansMapper.selectByUserAndFan(uid, UserContext.getUserId());
        if (userFans != null) {
            userFans.setState(add);
            userFansMapper.updateByPrimaryKey(userFans);
        } else if (add) {
            userFansMapper.insert(uid, UserContext.getUserId());
        }
    }

    @Override
    public boolean checkIfUserFan(Long uid) {
        UserFans userFans = userFansMapper.selectByUserAndFan(uid, UserContext.getUserId());
        return userFans != null && userFans.isState();
    }

    @Override
    public PageResult<UserInfo> queryUserFans(UserFansQueryObject qo) {
        int count = userFansMapper.queryCount(qo);
        List<UserInfo> list = userFansMapper.query(qo);
        return new PageResult<>(list, count, qo.getCurrentPage(), qo.getPageSize());
    }

    @Override
    public UserInfo selectUserByPromoCode(String promoCode) {
        UserInfo userInfo = mapper.selectByPromoCode(promoCode);
        if (userInfo == null) {
            throw new BusinessException(CommonErrorResultEnum.RELATION_USER_UN_EXIST);
        }
        return userInfo;
    }

    @Override
    public int queryUserCount() {
        UserInfoQueryObject qo = new UserInfoQueryObject();
        return mapper.queryCount(qo);
    }

    @Override
    public MgrPageResult<UserInfo> queryUser(UserInfoQueryObject qo) {
        int count = mapper.queryCount(qo);
        List<UserInfo> list = mapper.query(qo);
        return new MgrPageResult<>(list, count, qo.getCurrentPage(), qo.getPageSize());
    }

    @Override
    public void updateUserType(Long uid, String type) {
        UserInfo user = this.getByUid(uid);
        if (StringUtils.isBlank(type)) {
            throw new BusinessException(CommonErrorResultEnum.REQUEST_PARAM_LACK, "type");
        }
        switch (type) {
            case "CREATEANT_AGENT":
                user.addState(BitStateUtil.TYPE_CREATEANT_AGENT);
                user.addState(BitStateUtil.TYPE_CREATEANT_MERCHANT);
                user.addState(BitStateUtil.TYPE_CREATEANT_MEMBER);
                break;
            case "CREATEANT_MERCHANT":
                user.addState(BitStateUtil.TYPE_CREATEANT_MERCHANT);
                user.addState(BitStateUtil.TYPE_CREATEANT_MEMBER);
                user.removeState(BitStateUtil.TYPE_CREATEANT_AGENT);
                break;
            case "CREATEANT_MEMBER":
                user.addState(BitStateUtil.TYPE_CREATEANT_MEMBER);
                user.removeState(BitStateUtil.TYPE_CREATEANT_MERCHANT);
                user.removeState(BitStateUtil.TYPE_CREATEANT_AGENT);
                break;
            case "NORMAL":
                user.removeState(BitStateUtil.TYPE_CREATEANT_MEMBER);
                user.removeState(BitStateUtil.TYPE_CREATEANT_MERCHANT);
                user.removeState(BitStateUtil.TYPE_CREATEANT_AGENT);
                break;
            default:
                throw new BusinessException(CommonErrorResultEnum.REQUEST_PARAM_LACK, "type");
        }
        mapper.updateByPrimaryKey(user);
    }

}
