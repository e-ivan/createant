package com.qnyy.re.business.vo;

import com.qnyy.re.base.util.AccountUtil;
import com.qnyy.re.base.util.SystemConstUtil;
import com.qnyy.re.business.entity.BaseOrder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 订单详情临时对象
 * Created by hh on 2017.9.24 0024.
 */
@Getter
@NoArgsConstructor
public class PayDetailVO {
    private String title = "红运当投-";       //商品信息
    private String detail = "订单金额";      //商品详情
    private String outTradeNo;  //商户订单号
    private BigDecimal totalFee;//支付价格
    private AccountUtil.AccountDealType type;//交易类型
    private Long realId;
    public PayDetailVO(String title, String detail, BaseOrder baseOrder, AccountUtil.AccountDealType type) {
        this.title = title;
        this.detail = detail;
        this.outTradeNo = baseOrder.getOutTradeNo();
        this.totalFee = baseOrder.getPayAmount();
        this.type = type;
        this.realId = baseOrder.getId();
    }

    public PayDetailVO(String title, BaseOrder baseOrder, AccountUtil.AccountDealType type) {
        this.title += title + (SystemConstUtil.productionState ? "" : "(测试)");
        this.outTradeNo = baseOrder.getOutTradeNo();
        this.totalFee = baseOrder.getPayAmount();
        this.type = type;
        this.realId = baseOrder.getId();
    }
}
