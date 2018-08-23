package com.qnyy.re.business.service.impl;

import com.qnyy.re.base.enums.CommonErrorResultEnum;
import com.qnyy.re.base.service.IUserAccountService;
import com.qnyy.re.base.util.AccountUtil;
import com.qnyy.re.base.util.container.BaseEntity;
import com.qnyy.re.base.util.exception.BusinessException;
import com.qnyy.re.business.entity.BaseOrder;
import com.qnyy.re.business.entity.RechargeOrder;
import com.qnyy.re.business.mapper.RechargeOrderMapper;
import com.qnyy.re.business.service.IBaseOrderService;
import com.qnyy.re.business.service.IRechargeOrderService;
import com.qnyy.re.business.util.OrderUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * Created by E_Iva on 2017.12.15.0015.
 */
@Service
public class RechargeOrderServiceImpl implements IRechargeOrderService {
    @Autowired
    private RechargeOrderMapper mapper;
    @Autowired
    private IBaseOrderService baseOrderService;
    @Autowired
    private IUserAccountService userAccountService;
    @Override
    public RechargeOrder createRechargeOrder(Long uid, BigDecimal amount) {
        if (amount == null || BigDecimal.ZERO.compareTo(amount) >= 0) {
            throw new BusinessException(CommonErrorResultEnum.AMOUNT_ERROR);
        }
        userAccountService.getUserAccountByUser(uid);
        RechargeOrder order = new RechargeOrder();
        OrderUtil.setOrderBaseParam(order, uid, amount, OrderUtil.OrderType.RECHARGE);
        mapper.insert(order);
        baseOrderService.createBaseOrder(order);
        return order;
    }

    @Override
    public RechargeOrder getByOrderId(String orderId) {
        return this.mapper.getByOrderId(orderId);
    }

    @Override
    public RechargeOrder setOrderSuccess(String orderId) {
        RechargeOrder order = mapper.getByOrderId(orderId);
        OrderUtil.setOrderPay(order);
        this.updateRechargeOrder(order);
        this.rechargeBalance(order);
        return order;
    }

    /**
     * 充值余额
     */
    private void rechargeBalance(RechargeOrder order){
        userAccountService.updateUserAccount(order.getUid(),order.getAmount(), AccountUtil.AccountDealType.RECHARGE,order.getId(),order.getAmount() + "元");
    }

    @Override
    public void updateRechargeOrder(BaseOrder baseOrder) {
        if (mapper.updateByPrimaryKey(baseOrder) <= 0) {
            throw new BusinessException(CommonErrorResultEnum.OPTIMISTIC_LOCK_ERROR);
        }
    }
}
