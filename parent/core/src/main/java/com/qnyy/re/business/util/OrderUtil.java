package com.qnyy.re.business.util;


import com.qnyy.re.business.entity.BaseOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Administrator on 2016/11/16.
 */
public class OrderUtil {
    private OrderUtil(){}
    @Getter@AllArgsConstructor
    public enum OrderType{
        RE_MOMENT("reMoment","RM","红包订单"),
        RECHARGE("recharge","RC","充值订单"),
        ;
        private String value;
        private String prefix;
        private String typeName;
        public static OrderType getByValue(String value){
            for (OrderType orderType : OrderType.values()) {
                if (orderType.getValue().equals(value)) {
                    return orderType;
                }
            }
            return null;
        }
    }
    @Getter@AllArgsConstructor
    public enum OrderState{
        CREATE(0,"已创建"),
        PAY(1,"已支付"),
        CANCEL(2,"已取消"),
        ;
        private Integer code;
        private String value;
    }
    @Getter@AllArgsConstructor
    public enum OrderPayType{
        ALI_PAY(0,"alipay","支付宝"),
        WX_PAY(1,"wxpay","微信"),
        BALANCE(2,"balance","余额");
        public static OrderPayType getByCode(String code){
            for (OrderPayType payType : OrderPayType.values()) {
                if (payType.getValue().equals(code)) {
                    return payType;
                }
            }
            return null;
        }
        private Integer code;
        private String value;
        private String typeName;
    }
    private static final String SPLIT = "T";

    public static <T extends BaseOrder> T setOrderBaseParam(BaseOrder order, Long uid, BigDecimal amount, OrderType type){
        order.setOrderId(createOrderId(type));
        order.setState(OrderState.CREATE.getCode());
        order.setAmount(amount);
        order.setPayAmount(order.getAmount());
        order.setUid(uid);
        order.setType(type.getValue());
        return (T)order;
    }

    public static <T extends BaseOrder> T setOrderPay(BaseOrder order){
        order.setState(OrderUtil.OrderState.PAY.getCode());
        order.setPayTime(new Date());
        return (T)order;
    }

    public static String getUUID() {
        String str = String.valueOf(System.currentTimeMillis());
        int i = (int) (Math.random() * 10000);
        str += String.format("%05d", i);
        return str;
    }

    public static String createOrderId(OrderType type) {
        return type.getPrefix() + getUUID();
    }

    /**
     * 为订单添加由时间生成的后缀
     *
     * @param orderId
     */
    public static String addOrderSuffix(String orderId) {
        String suf = String.valueOf(System.currentTimeMillis()).substring(0, 10);
        StringBuilder sb = new StringBuilder(orderId.length() + suf.length() + 2);
        return sb.append(orderId).append(SPLIT).append(suf).toString();
    }

    /**
     * 去除商户订单的后缀，生成原订单id
     *
     * @param outTradeNo
     * @return
     */
    public static String removeOrderSuffix(String outTradeNo) {
        int endIndex = outTradeNo.lastIndexOf(SPLIT);
        return outTradeNo.substring(0, endIndex < 0 ? outTradeNo.length() - 1 : endIndex);
    }

    public static void main(String args[]) {

        for (int i = 0; i < 100; i++) {
            System.out.println(getUUID().length());
        }
    }
}
