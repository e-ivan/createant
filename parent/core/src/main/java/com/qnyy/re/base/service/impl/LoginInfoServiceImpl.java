package com.qnyy.re.base.service.impl;

import com.qnyy.re.base.entity.LoginInfo;
import com.qnyy.re.base.entity.UserInfo;
import com.qnyy.re.base.entity.UserToken;
import com.qnyy.re.base.enums.LoginInfoStateEnum;
import com.qnyy.re.base.enums.LoginInfoUserTypeEnum;
import com.qnyy.re.base.mapper.LoginInfoMapper;
import com.qnyy.re.base.mapper.UserAccountMapper;
import com.qnyy.re.base.service.ILoginInfoService;
import com.qnyy.re.base.service.IUserInfoService;
import com.qnyy.re.base.service.IUserTokenService;
import com.qnyy.re.base.util.BitStateUtil;
import com.qnyy.re.base.util.MD5Util;
import com.qnyy.re.base.util.SystemConstUtil;
import com.qnyy.re.base.util.UserContext;
import com.qnyy.re.base.util.exception.BusinessException;
import com.qnyy.re.base.vo.param.*;
import com.qnyy.re.business.enums.RegisterSourceEnum;
import com.qnyy.re.business.service.IUserClassRelationService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.qnyy.re.base.enums.CommonErrorResultEnum.*;

/**
 * Created by E_Iva on 2017/11/24.
 */
@Service
public class LoginInfoServiceImpl implements ILoginInfoService {
    @Autowired
    private LoginInfoMapper mapper;
    @Autowired
    private IUserInfoService userInfoService;
    @Autowired
    private IUserTokenService userTokenService;
    @Autowired
    private IUserClassRelationService userClassRelationService;
    @Autowired
    private UserAccountMapper accountMapper;
    @Override
    public UserToken partnerRegister(PartnerRegisterVO vo) {
        if (mapper.queryUserBySource(vo.getOpenId(),vo.getSource()) != null){
            throw new BusinessException(USER_EXIST);
        }
        LoginInfo loginInfo = this.getByPhone(vo.getPhone());
        UserToken userToken;
        if (loginInfo == null) {//首次注册
            if (StringUtils.isBlank(vo.getPassword())) {
                throw new BusinessException(REQUEST_PARAM_LACK,"password");
            }
            loginInfo = new LoginInfo();
            loginInfo.setState(LoginInfoStateEnum.NORMAL.getCode());
            loginInfo.setWechatToken(vo.getOpenId());
            loginInfo.setUserType(LoginInfoUserTypeEnum.USER.getCode());
            loginInfo.setPhone(vo.getPhone());
            loginInfo.setPassword(MD5Util.encodePassword(vo.getPassword()));
            mapper.insert(loginInfo);
            userToken = userInfoService.save(loginInfo.getUid(), null, vo.getHeadUrl(), vo.getNickname(), vo.getSex(), vo.getLat(), vo.getLng(), vo.getBitStates());
            if (StringUtils.isNoneBlank(vo.getPromoCode())) {
                //关联推广
                UserInfo user = userInfoService.selectUserByPromoCode(vo.getPromoCode());
                userClassRelationService.createRelation(user.getUid(),loginInfo.getUid());
            }
        }else {//不是首次，绑定微信号码
            loginInfo.setWechatToken(vo.getOpenId());
            mapper.updateByPrimaryKey(loginInfo);
            userToken = userTokenService.updateToken(loginInfo.getUid());
        }
        //设置绑定微信
        UserInfo userInfo = userInfoService.getByUid(loginInfo.getUid());
        userInfo.addState(BitStateUtil.OP_BIND_WECHAT);
        userInfo.addState(BitStateUtil.OP_BIND_PHONE);
        userInfoService.update(userInfo);
        return userToken;
    }

    @Override
    public UserToken partnerLogin(PartnerLoginVO vo) {
        LoginInfo loginInfo = mapper.queryUserBySource(vo.getOpenId(), vo.getSource());
        if (loginInfo == null) {
            throw new BusinessException(USER_UN_EXIST);
        }
        return userTokenService.updateToken(loginInfo.getUid());
    }

    @Override
    public UserToken createantLogin(CreateantLoginVO vo) {
        //获取创蚁用户
        LoginInfo loginInfo = mapper.queryUserBySource(vo.getOpenId(), vo.getSource());
        if (loginInfo != null) {
            return userTokenService.updateToken(loginInfo.getUid());
        }
        loginInfo = new LoginInfo();
        loginInfo.setUsername(vo.getUsername());
        loginInfo.setState(LoginInfoStateEnum.NORMAL.getCode());
        loginInfo.setCreateantToken(vo.getOpenId());
        loginInfo.setUserType(LoginInfoUserTypeEnum.USER.getCode());
        loginInfo.setPhone(vo.getPhone());
        mapper.insert(loginInfo);
        return userInfoService.save(loginInfo.getUid(),vo.getCreateantUid(), vo.getHeadUrl(), vo.getNickname(), vo.getSex(), vo.getLat(), vo.getLng(),vo.getBitStates());
    }

    @Override
    public UserToken phoneLogin(PhoneLoginVO vo) {
        LoginInfo l = this.getByPhone(vo.getPhone());
        if (l == null || !MD5Util.encodePassword(vo.getPassword()).equals(l.getPassword())) {
            throw new BusinessException(LOGIN_USER_ERROR);
        }
        return userTokenService.updateToken(l.getUid());

    }

    @Override
    public UserToken phoneRegister(PhoneRegisterVO vo) {
        LoginInfo loginInfo = this.getByPhone(vo.getPhone());
        if (loginInfo != null) {
            throw new BusinessException(USER_EXIST);
        }
        loginInfo = new LoginInfo();
        loginInfo.setState(LoginInfoStateEnum.NORMAL.getCode());
        loginInfo.setUserType(LoginInfoUserTypeEnum.USER.getCode());
        loginInfo.setPhone(vo.getPhone());
        loginInfo.setPassword(MD5Util.encodePassword(vo.getPassword()));
        mapper.insert(loginInfo);
        UserToken userToken = userInfoService.save(loginInfo.getUid(), null, null, vo.getPhone(), -1, vo.getLat(), vo.getLng(), vo.getBitStates());
        //设置绑定手机
        UserInfo userInfo = userInfoService.getByUid(loginInfo.getUid());
        userInfo.addState(BitStateUtil.OP_BIND_PHONE);
        userInfoService.update(userInfo);
        if (StringUtils.isNoneBlank(vo.getPromoCode())) {
            //关联推广
            UserInfo user = userInfoService.selectUserByPromoCode(vo.getPromoCode());
            userClassRelationService.createRelation(user.getUid(),loginInfo.getUid());
        }
        return userToken;
    }

    @Override
    public void bindUserPhone(String phone, Long uid) {
        //查看手机号码是否已绑定
        LoginInfo byPhone = this.getByPhone(phone);
        if (byPhone != null) {
            throw new BusinessException(USER_PHONE_EXIST);
        }
        UserInfo userInfo = userInfoService.getByUid(uid);
        if (userInfo.getBindPhone()) {
            throw new BusinessException(USER_PHONE_REPEAT);
        }
        LoginInfo loginInfo = this.getByUid(uid);
        loginInfo.setPhone(phone);
        mapper.updateByPrimaryKey(loginInfo);
        userInfo.addState(BitStateUtil.OP_BIND_PHONE);
        userInfoService.update(userInfo);
    }

    @Override
    public void bindPartnerOpenId(Long uid, String openId, Integer source) {
        UserInfo user = userInfoService.getByUid(uid);
        RegisterSourceEnum sourceEnum = RegisterSourceEnum.getByCode(source);
        if (sourceEnum == null) {
            throw new BusinessException(REQUEST_PARAM_LACK,"source");
        }
        if (RegisterSourceEnum.WECHAT.equals(sourceEnum)) {
            if (user.getBindWechat()) {
                throw new BusinessException(HAD_BIND_WECHAT);
            }
            if (mapper.queryUserBySource(openId, source) != null) {
                throw new BusinessException(USER_WECHAT_EXIST);
            }
            LoginInfo loginInfo = this.getByUid(uid);
            loginInfo.setWechatToken(openId);
            mapper.updateByPrimaryKey(loginInfo);
            user.addState(BitStateUtil.OP_BIND_WECHAT);
            userInfoService.update(user);
        }
    }

    @Override
    public void resetPasswordByPhone(ResetPasswordByPhoneVO vo) {
        LoginInfo loginInfo = this.getByPhone(vo.getPhone());
        if (loginInfo == null) {
            throw new BusinessException(USER_UN_EXIST);
        }
        loginInfo.setPassword(MD5Util.encodePassword(vo.getPassword()));
        mapper.updateByPrimaryKey(loginInfo);
        userTokenService.updateToken(loginInfo.getUid());
    }

    @Override
    public void resetPasswordByOld(ResetPasswordByOldVO vo) {
        LoginInfo loginInfo = this.getByUid(UserContext.getUserId());
        if (loginInfo == null) {
            throw new BusinessException(USER_UN_EXIST);
        }
        if (!MD5Util.encodePassword(vo.getOldPassword()).equals(loginInfo.getPassword())) {
            throw new BusinessException(PASSWORD_ERROR);
        }
        loginInfo.setPassword(MD5Util.encodePassword(vo.getPassword()));
        mapper.updateByPrimaryKey(loginInfo);
        userTokenService.updateToken(loginInfo.getUid());
    }

    @Override
    public LoginInfo getByPhone(String phone) {
        return mapper.getByPhone(phone);
    }

    @Override
    public LoginInfo getByUid(Long uid) {
        return mapper.selectByPrimaryKey(uid);
    }

    @Override
    public void initSystemAccount() {
        LoginInfo loginInfo = mapper.selectSystemAccount();
        if (loginInfo == null) {
            loginInfo = new LoginInfo();
            loginInfo.setUsername(SystemConstUtil.systemAccountUsername);
            loginInfo.setUserType(LoginInfoUserTypeEnum.MANAGE.getCode());
            loginInfo.setUid(SystemConstUtil.systemAccountId);
            loginInfo.setCreateantToken(SystemConstUtil.systemCreateantToken);
            loginInfo.setState(LoginInfoStateEnum.NORMAL.getCode());
            mapper.insert(loginInfo);
            //创建账户
            accountMapper.insert(loginInfo.getUid());
        }
    }

    @Override
    public int deleteUser(String phone, Long uid) {
        if (StringUtils.isNoneBlank(phone)) {
            LoginInfo byPhone = getByPhone(phone);
            if (byPhone != null) {
                uid = byPhone.getUid();
            }
        }
        return mapper.deleteUserAndCorrelation(uid);
    }

    @Override
    public LoginInfo getBySource(String openId, Integer source) {
        LoginInfo loginInfo = mapper.queryUserBySource(openId, source);
        if (loginInfo == null) {
            throw new BusinessException(USER_UN_EXIST);
        }
        return loginInfo;
    }

}
