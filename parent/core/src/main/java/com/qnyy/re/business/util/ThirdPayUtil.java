package com.qnyy.re.business.util;

import com.qnyy.re.base.enums.CommonErrorResultEnum;
import com.qnyy.re.base.util.DateUtil;
import com.qnyy.re.base.util.NumberUtil;
import com.qnyy.re.base.util.SystemConstUtil;
import com.qnyy.re.base.util.exception.BusinessException;
import com.qnyy.re.business.util.wxPay.WXPay;
import com.qnyy.re.business.util.wxPay.WXPayConfigImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * 第三方支付
 * Created by hh on 2017.9.21 0021.
 */
@Component
public class ThirdPayUtil {

    private static WXPay wxPay;
    static {
        try {
            wxPay = new WXPay(WXPayConfigImpl.getInstance(), SystemConstUtil.wechatNotifyUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建微信支付参数
     */
    public static Map<String, String> createPrepayParams(String ip, BigDecimal totalFee, String outTradeNo, String title) {
        Integer payMoney = NumberUtil.fromYuan2Fen(totalFee);
        SortedMap<String, String> sortedMap = new TreeMap<>();
        sortedMap.put("body", title);
        sortedMap.put("out_trade_no", outTradeNo);
        sortedMap.put("total_fee", payMoney.toString());
        sortedMap.put("spbill_create_ip", ip);
        sortedMap.put("trade_type", "APP");
        Date now = new Date();
        sortedMap.put("time_start", DateUtil.wechatPayDateFormat(now));
        sortedMap.put("time_expire", DateUtil.wechatPayDateFormat(now,30));
        try {
            Map<String, String> map = wxPay.unifiedOrder(sortedMap);
            String prepayId = map.get("prepay_id");
            if (StringUtils.isBlank(prepayId)){
                throw new RuntimeException();
            }
            return wxPay.fillPrepayRequestData(prepayId);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(CommonErrorResultEnum.OPTIMISTIC_LOCK_ERROR, "调用微信统一下单接口失败");
        }
    }

}
