package com.qnyy.re.business.service.impl;

import com.qnyy.re.base.enums.CommonErrorResultEnum;
import com.qnyy.re.base.service.IUserAccountService;
import com.qnyy.re.base.util.AccountUtil;
import com.qnyy.re.base.util.SystemConstUtil;
import com.qnyy.re.base.util.exception.BusinessException;
import com.qnyy.re.business.service.IBaseOrderService;
import com.qnyy.re.business.service.IPayUtilService;
import com.qnyy.re.business.util.AlipayUtil;
import com.qnyy.re.business.util.ThirdPayUtil;
import com.qnyy.re.business.vo.PayDetailVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;

import static com.qnyy.re.business.util.OrderUtil.OrderPayType.*;

/**
 * Created by E_Iva on 2017.12.1 0001.
 */
@Service
public class PayUtilServiceImpl implements IPayUtilService {

    @Autowired
    private IBaseOrderService baseOrderService;

    @Autowired
    private IUserAccountService accountService;


    @Override
    public Map<String,String> createWechatOrder(String ip, String orderId) {
        final String payType = WX_PAY.getValue();
        PayDetailVO payDetail = baseOrderService.createOrderDetail(orderId, payType);
        return ThirdPayUtil.createPrepayParams(ip, SystemConstUtil.productionState ?  payDetail.getTotalFee(): new BigDecimal("0.01"),payDetail.getOutTradeNo() , payDetail.getTitle());
    }

    @Override
    public String createAlipayOrder(String orderId) {
        final String payType = ALI_PAY.getValue();
        PayDetailVO payDetail = baseOrderService.createOrderDetail(orderId, payType);
        return AlipayUtil.getPayInfo(payDetail.getTitle(), payDetail.getDetail(), payDetail.getTotalFee(), payDetail.getOutTradeNo());
    }

    @Override
    public void balancePayOrder(Long uid, String orderId) {
        final String payType = BALANCE.getValue();
        PayDetailVO payDetail = baseOrderService.createOrderDetail(orderId, payType);
        //使用余额充值，拒绝
        if (AccountUtil.AccountDealType.RECHARGE.equals(payDetail.getType())){
            throw new BusinessException(CommonErrorResultEnum.REJECT_USE_BALANCE);
        }
        accountService.updateUserAccount(uid,payDetail.getTotalFee(),payDetail.getType(),payDetail.getRealId());
        baseOrderService.setOrderPaySuccess(orderId, payDetail.getTotalFee());
    }


}
