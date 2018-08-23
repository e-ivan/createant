package com.qnyy.re.base.service;


/**
 * 验证服务
 * Created by E_Iva on 2017/11/27.
 */
public interface IVerifyService {
    /**
     * 创建验证码
     */
    String createVerifyCode(String phone);

    /**
     * 校验验证码
     */
    void checkVerifyCode(String phone,String code);

    /**
     * 设置验证码使用
     */
    void updateVerifyCodeUse(String phone,String code);
}
