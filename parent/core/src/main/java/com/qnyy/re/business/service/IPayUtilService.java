package com.qnyy.re.business.service;

import java.util.Map;

/**
 * 支付工具服务
 * Created by E_Iva on 2017.12.1 0001.
 */
public interface IPayUtilService {
    /**
     * 创建微信支付订单参数
     */
    Map<String,String> createWechatOrder(String ip, String orderId);

    /**
     * 创建支付宝订单参数
     */
    String createAlipayOrder(String orderId);

    /**
     * 余额支付订单
     */
    void balancePayOrder(Long uid,String orderId);


}
