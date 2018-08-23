package com.qnyy.re.business.service;

import com.qnyy.re.business.entity.BaseOrder;
import com.qnyy.re.business.vo.PayDetailVO;

import java.math.BigDecimal;

/**
 * 通用订单服务
 * Created by E_Iva on 2017.12.1 0001.
 */
public interface IBaseOrderService {

    /**
     * 创建通用订单
     * @param baseOrder
     */
    void createBaseOrder(BaseOrder baseOrder);

    /**
     * 查询订单是否支付
     * @param orderId
     * @return  支付返回true
     */
    boolean checkOrderPay(String orderId);

    /**
     * 设置订单成功
     * @param orderId
     * @param payAmount 回调实际支付的金额
     */
    void setOrderPaySuccess(String orderId, BigDecimal payAmount);

    /**
     * 创建订单支付信息
     */
    PayDetailVO createOrderDetail(String orderId, String payType);

}
