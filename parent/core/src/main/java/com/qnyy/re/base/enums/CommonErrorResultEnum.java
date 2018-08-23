package com.qnyy.re.base.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by dly on 2016/7/29.
 */
@Getter@AllArgsConstructor
public enum CommonErrorResultEnum {
    //100000通用异常
    LOGIC_ERROR(-1,"内部异常"),
    REQUEST_SIGN_ERROR(100100,"方法签名错误"),
    REQUEST_PARAM_LACK(100101,"请求参数不完整"),
    CALL_BACK_FAIL(100102,"回调接口失败"),
    OBJECT_UN_EXIST(100103,"操作对象不存在"),
    OBJECT_NOP(100104,"对象不可操作"),
    MCH_UN_EXIST(100105,"商户不存在"),
    MCH_KEY_ERROR(100106,"授权码错误"),
    MCH_NO_PERMISSION(100107,"无权限操作"),
    MCH_EXIST(100108,"商户ID已存在"),
    USER_EXIST(100201,"用户已存在，请登录"),
    USER_UN_EXIST(100202,"用户不存在"),
    OWN2OWN(100205,"不允许操作自己"),
    RELATION_USER_UN_EXIST(100206,"推广用户不存在"),
    LOGIN_USER_ERROR(100207,"手机或密码错误"),
    PASSWORD_ERROR(100208,"密码错误"),
    LOGIN_EXPIRED(100301,"登录失效，请重新登录"),
    YONGYUN_TOKEN_ERROR(100302,"获取融云TOKEN失败"),
    LOCATION_ERROR(100303,"无法获取当前位置信息"),
    REQUEST_TIMEOUT(100403,"请求超时"),
    OPTIMISTIC_LOCK_ERROR(100404,"网络繁忙"),//乐观锁失败
    VERIFY_CODE_OFTEN(100501,"{}分钟内不能重复发送"),
    VERIFY_CODE_ERROR(100502, "验证码错误"),
    VERIFY_CODE_EXPIRED(100503, "验证码失效，请重新获取"),
    USER_HAD_UPPER(100601,"你已有上级"),
    USER_HAD_JUNIOR(100602,"你已有下级"),
    FEEDBACK_OFTEN(100603,"反馈过于频繁"),
    INFORM_OFTEN(100604,"你已举报过该用户"),
    USER_PHONE_EXIST(100701,"该手机号码已绑定其它号"),
    USER_PHONE_REPEAT(100702,"用户已绑定手机号，请解绑再操作"),
    WECHAT_UN_BIND(100703,"未绑定微信帐号"),
    HAD_BIND_WECHAT(100704,"账户已绑定过微信"),
    USER_WECHAT_EXIST(100705,"微信已绑定其它账户"),
    FILE_TYPE_UN_EXIST(100901, "文件类型不存在"),
    FILE_PURPOSE_UN_EXIST(100902, "文件用途不存在"),
    FILE_UPLOAD_ERROR(100903, "文件上传失败，请联系客服"),
    //200000订单异常
    ORDER_UN_EXIST(200100,"订单不存在"),
    ORDER_OPERATE_OVERDUE(200101,"订单已过期"),
    ORDER_TYPE_UN_EXIST(200102,"订单类型不存在"),

    //300000账户异常
    AMOUNT_ERROR(300100,"金额异常"),
    CASH_AMOUNT_LACK(300101,"可提金额不足"),
    BALANCE_LACK(300102,"余额不足"),
    ACCOUNT_ERROR(300103,"账户异常"),
    CASH_ACCOUNT_UN_BIND(300104,"请先绑定提现方式"),
    CASH_HAS_PROCESS(300105,"您有一单提现正在处理"),
    REJECT_USE_BALANCE(300106,"拒绝使用余额"),

    //400000红包异常
    RE_AMOUNT_ERROR(400100,"红包金额错误"),
    RE_NUM_ERROR(400101,"红包人数必须大于0"),
    RE_SPLIT_ERROR(400102,"每个红包金额不能{}"),
    RE_OUT_OF_RANGE(400103,"红包不在范围内"),
    RE_NOT_CREATEANT(400104,"请先升级为创客"),
    RE_DAY_MAX_DRAW(400105,"每天最多领取{}次"),
    RE_STORE_NO_DRAW(400106,"店铺红包已领完"),

    //500000微信接口相关异常
    MP_ACCESS_TOKEN_EXPIRED(500100,"微信公众号令牌失效"),
    MP_HAS_DEVICE_APPLYING(500200,"已有在审核设备批次"),
    MP_DEVICE_HAD_BIND(500301,"该设备已被绑定"),
    MP_UN_BIND_FAIL(500302,"设备解绑失败"),

    ;

    private Integer code;
    private String exMessage;
}
