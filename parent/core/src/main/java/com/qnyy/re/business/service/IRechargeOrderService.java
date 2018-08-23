package com.qnyy.re.business.service;

import com.qnyy.re.business.entity.BaseOrder;
import com.qnyy.re.business.entity.RechargeOrder;

import java.math.BigDecimal;

/**
 * 充值服务
 * Created by E_Iva on 2017.12.15.0015.
 */
public interface IRechargeOrderService {
    /**
     * 创建充值订单
     */
    RechargeOrder createRechargeOrder(Long uid, BigDecimal amount);

    RechargeOrder getByOrderId(String orderId);

    /**
     * 设置订单成功
     */
    RechargeOrder setOrderSuccess(String orderId);

    void updateRechargeOrder(BaseOrder baseOrder);
}
