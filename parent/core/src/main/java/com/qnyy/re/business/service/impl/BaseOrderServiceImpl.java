package com.qnyy.re.business.service.impl;

import com.qnyy.re.base.enums.CommonErrorResultEnum;
import com.qnyy.re.base.service.IUserAccountService;
import com.qnyy.re.base.util.AccountUtil;
import com.qnyy.re.base.util.SystemConstUtil;
import com.qnyy.re.base.util.exception.BusinessException;
import com.qnyy.re.business.entity.BaseOrder;
import com.qnyy.re.business.mapper.BaseOrderMapper;
import com.qnyy.re.business.service.IBaseOrderService;
import com.qnyy.re.business.service.IReMomentService;
import com.qnyy.re.business.service.IRechargeOrderService;
import com.qnyy.re.business.util.OrderUtil;
import com.qnyy.re.business.vo.PayDetailVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * Created by E_Iva on 2017.12.1 0001.
 */
@Service
public class BaseOrderServiceImpl implements IBaseOrderService {
    @Autowired
    private BaseOrderMapper baseOrderMapper;
    @Autowired
    private IReMomentService reMomentService;
    @Autowired
    private IRechargeOrderService rechargeOrderService;
    @Autowired
    private IUserAccountService accountService;


    @Override
    public void createBaseOrder(BaseOrder baseOrder) {
        baseOrderMapper.insert(baseOrder);
    }

    @Override
    public boolean checkOrderPay(String orderId) {
        BaseOrder baseOrder = baseOrderMapper.getByOrderId(orderId);
        return baseOrder != null && OrderUtil.OrderState.PAY.getCode().equals(baseOrder.getState());
    }

    @Override
    public void setOrderPaySuccess(String orderId, BigDecimal payAmount) {
        BaseOrder baseOrder = baseOrderMapper.getByOrderId(orderId);
        if (baseOrder != null && OrderUtil.OrderState.CREATE.getCode().equals(baseOrder.getState())) {
            baseOrder = this.orderDispatcher(baseOrder);
            if (SystemConstUtil.productionState && baseOrder.getPayAmount().compareTo(payAmount) != 0) {
                throw new BusinessException(CommonErrorResultEnum.CALL_BACK_FAIL,"本地订单金额：" + baseOrder.getPayAmount() + "，实际支付金额：" + payAmount);
            }
            baseOrderMapper.updateByPrimaryKey(baseOrder);
                OrderUtil.OrderPayType payType = OrderUtil.OrderPayType.getByCode(baseOrder.getPayType());
            if (!OrderUtil.OrderPayType.BALANCE.equals(payType) && payType != null) {
                accountService.updateSysAccount(payAmount, AccountUtil.AccountDealType.SYS_PARTNER_IN,baseOrder.getId(),
                        baseOrder.getUid() + "," + payType.getTypeName() + ",orderId:" + baseOrder.getOrderId());
            }
        }
    }

    @Override
    public PayDetailVO createOrderDetail(String orderId, String payType) {
        BaseOrder baseOrder = baseOrderMapper.getByOrderId(orderId);
        if (baseOrder != null) {
            if (!OrderUtil.OrderState.CREATE.getCode().equals(baseOrder.getState())) {
                throw new BusinessException(CommonErrorResultEnum.ORDER_OPERATE_OVERDUE);
            }
            return this.orderDispatcher(baseOrder,payType);
        }
        throw new BusinessException(CommonErrorResultEnum.ORDER_UN_EXIST);
    }


    /**
     * 订单路由器
     */
    private <T> T orderDispatcher(BaseOrder baseOrder, String payType){
        //订单分类处理
        if (OrderUtil.OrderType.RE_MOMENT.getValue().equals(baseOrder.getType())){
            if (payType == null){
                return (T) reMomentService.setOrderSuccess(baseOrder.getOrderId());
            }else {
                baseOrder = reMomentService.getByOrderId(baseOrder.getOrderId());
                this.setOrderPayDetail(baseOrder,payType);
                reMomentService.updateReOrder(baseOrder);
                PayDetailVO payDetailVO = new PayDetailVO(OrderUtil.OrderType.RE_MOMENT.getTypeName(), baseOrder, AccountUtil.AccountDealType.PAY_RED_ENVELOPE);
                return (T) payDetailVO;
            }
        }else if (OrderUtil.OrderType.RECHARGE.getValue().equals(baseOrder.getType())){
            if (payType == null){
                return (T) rechargeOrderService.setOrderSuccess(baseOrder.getOrderId());
            }else {
                baseOrder = rechargeOrderService.getByOrderId(baseOrder.getOrderId());
                this.setOrderPayDetail(baseOrder,payType);
                rechargeOrderService.updateRechargeOrder(baseOrder);
                PayDetailVO payDetailVO = new PayDetailVO(OrderUtil.OrderType.RECHARGE.getTypeName(), baseOrder, AccountUtil.AccountDealType.RECHARGE);
                return (T) payDetailVO;
            }
        }

        throw new BusinessException(CommonErrorResultEnum.ORDER_TYPE_UN_EXIST);
    }

    private BaseOrder setOrderPayDetail(BaseOrder baseOrder,String payType){
        baseOrder.setPayType(payType);
        baseOrder.setOutTradeNo(OrderUtil.addOrderSuffix(baseOrder.getOrderId()));
        return baseOrder;
    }

    private BaseOrder orderDispatcher(BaseOrder baseOrder){
        return this.orderDispatcher(baseOrder,null);
    }



}
