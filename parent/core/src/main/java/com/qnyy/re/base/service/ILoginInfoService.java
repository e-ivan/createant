package com.qnyy.re.base.service;

import com.qnyy.re.base.entity.LoginInfo;
import com.qnyy.re.base.entity.UserToken;
import com.qnyy.re.base.vo.param.*;

/**
 * 登录信息服务
 * Created by E_Iva on 2017/11/24.
 */
public interface ILoginInfoService {
    /**
     * 第三方注册
     */
    UserToken partnerRegister(PartnerRegisterVO vo);

    /**
     * 第三方登录
     */
    UserToken partnerLogin(PartnerLoginVO vo);

    /**
     * 创蚁登录
     */
    UserToken createantLogin(CreateantLoginVO vo);

    /**
     * 手机登录
     */
    UserToken phoneLogin(PhoneLoginVO vo);

    /**
     * 手机注册
     */
    UserToken phoneRegister(PhoneRegisterVO vo);

    /**
     * 绑定手机
     */
    void bindUserPhone(String phone,Long uid);

    /**
     * 绑定第三方账户
     */
    void bindPartnerOpenId(Long uid,String openId,Integer source);

    /**
     * 通过手机修改密码
     */
    void resetPasswordByPhone(ResetPasswordByPhoneVO vo);

    /**
     * 通过原密码修改Miami
     */
    void resetPasswordByOld(ResetPasswordByOldVO vo);

    /**
     * 根据手机号码获取
     */
    LoginInfo getByPhone(String phone);

    /**
     * 根据id获取登录信息
     */
    LoginInfo getByUid(Long uid);

    /**
     * 初始化系统帐户
     * @return
     */
    void initSystemAccount();

    int deleteUser(String phone,Long uid);

    /**
     * 根据源用户信息
     */
    LoginInfo getBySource(String openId,Integer source);

}
