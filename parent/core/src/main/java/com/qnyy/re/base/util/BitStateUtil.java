package com.qnyy.re.base.util;

/**
 * 用户状态类，记录用户在平台使用系统中所有的状态。
 */
public class BitStateUtil {
    public final static Long OP_BIND_PHONE = 1L << 0; // 用户绑定手机状态码
    public final static Long OP_BIND_WECHAT = 1L << 1; // 用户绑定微信

    public final static Long OP_BIND_CASH_ACCOUNT = 1L << 2; //用户是否绑定提现账户
    public final static Long OP_HAS_CASH_PROCESS = 1L << 3; //用户是否有提现在进行

    public final static Long OP_HAS_BIND_DEVICE = 1L << 5; //用户是否有绑定设备

    public final static Long TYPE_CREATEANT_MERCHANT = 1L << 10; //创蚁招商总监
    public final static Long TYPE_CREATEANT_MEMBER = 1L << 11; //创蚁会员
    public final static Long TYPE_CREATEANT_AGENT = 1L << 12; //创蚁代理商

    public final static Long TYPE_ADMIN = 1L << 20; //管理类型


    /**
     * @param states 所有状态值
     * @param value  需要判断状态值
     * @return 是否存在
     */
    public static boolean hasState(long states, long value) {
        return (states & value) != 0;
    }

    /**
     * @param states 已有状态值
     * @param value  需要添加状态值
     * @return 新的状态值
     */
    public static long addState(long states, long value) {
        if (hasState(states, value)) {
            return states;
        }
        return (states | value);
    }

    /**
     * @param states 已有状态值
     * @param value  需要删除状态值
     * @return 新的状态值
     */
    public static long removeState(long states, long value) {
        if (!hasState(states, value)) {
            return states;
        }
        return states ^ value;
    }
}
