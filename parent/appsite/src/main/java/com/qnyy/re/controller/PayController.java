package com.qnyy.re.controller;

import com.qnyy.re.base.util.NumberUtil;
import com.qnyy.re.base.util.SystemConstUtil;
import com.qnyy.re.base.util.UserContext;
import com.qnyy.re.base.util.container.ObjectResponse;
import com.qnyy.re.base.util.container.Response;
import com.qnyy.re.business.util.OrderUtil;
import com.qnyy.re.business.util.wxPay.WXPayConstants;
import com.qnyy.re.business.util.wxPay.WXPayUtil;
import com.qnyy.re.base.util.annotation.ApiDocument;
import com.qnyy.re.base.util.annotation.UnRequiredLogin;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * 支付控制器
 * Created by E_Iva on 2017.12.4.0004.
 */
@Controller
@RequestMapping("pay")
public class PayController extends BaseController {


    /**
     * 创建微信支付订单
     *
     * @param orderId
     */
    @RequestMapping(value = "wechatPay", method = RequestMethod.POST)
    @ResponseBody
    @ApiDocument("创建微信支付订单")
    public Response wechatPay(HttpServletRequest request, String orderId) {
        return new ObjectResponse<>(payUtilService.createWechatOrder(getIpAddr(request), orderId));
    }

    /**
     * 创建支付宝订单
     */
    @RequestMapping(value = "aliPay", method = RequestMethod.POST)
    @ResponseBody
    @ApiDocument("创建支付宝订单")
    public Response aliPay(String orderId) {
        return new ObjectResponse<>(payUtilService.createAlipayOrder(orderId));
    }

    /**
     * 余额支付
     */
    @RequestMapping(value = "balancePay", method = RequestMethod.POST)
    @ResponseBody
    @ApiDocument("余额支付")
    public Response balancePay(String orderId) {
        payUtilService.balancePayOrder(UserContext.getUserId(), orderId);
        return new Response("支付成功");
    }

    /**
     * 微信支付回调接口
     */
    @RequestMapping(value = "wechatPayCallBack")
    @UnRequiredLogin(checkSign = false)
    @ApiDocument("微信支付回调接口")
    public void wechatPayCallBack(HttpServletRequest request,HttpServletResponse response) throws Exception {
        response.setContentType("text/xml;charset=UTF-8");
        String xml = WXPayUtil.readXML(request.getInputStream());
        Map<String, String> notifyMap = WXPayUtil.xmlToMap(xml);
        if (StringUtils.equals(notifyMap.get("result_code"), "SUCCESS") &&
                StringUtils.equals(notifyMap.get("appid"), SystemConstUtil.wechatPayAppId) &&
                StringUtils.equals(notifyMap.get("mch_id"), SystemConstUtil.wechatPayMchId)) {
            if (!WXPayUtil.isSignatureValid(notifyMap, SystemConstUtil.wechatPayApiKey, WXPayConstants.SignType.HMACSHA256)) {
                responseWechatPay(response,false,"签名错误");
                return;
            }
            //获取订单号
            String orderId = OrderUtil.removeOrderSuffix(notifyMap.get("out_trade_no"));
            //已支付
            if (baseOrderService.checkOrderPay(orderId)) {
                responseWechatPay(response,true,"支付成功");
                return;
            }
            try {
                //获取订单支付金额
                BigDecimal payAmount = NumberUtil.fromFen2Yuan(Integer.parseInt(notifyMap.get("total_fee")));
                baseOrderService.setOrderPaySuccess(orderId, payAmount);
                //支付成功
                responseWechatPay(response,true,"支付成功");
                return;
            }catch (Exception e){
                //支付失败
                responseWechatPay(response,false,e.getMessage());
                return;
            }
        }
        responseWechatPay(response,false,"支付返回失败");
    }

    @RequestMapping(value = "paySuccess")
    @UnRequiredLogin(checkSign = false)
    public void paySuccess(HttpServletRequest request) {
        if (!SystemConstUtil.productionState) {
            String orderId = request.getParameter("orderId");
            //已支付
            if (baseOrderService.checkOrderPay(orderId)) {
                return;
            }
            baseOrderService.setOrderPaySuccess(orderId, BigDecimal.ZERO);
        }
    }

    private static void responseWechatPay(HttpServletResponse response,boolean success,String message) throws Exception {
        Map<String, String> map = new HashMap<>();
        map.put("return_code", success ? WXPayConstants.SUCCESS : WXPayConstants.FAIL);
        map.put("return_msg", message);
        response.getWriter().print(WXPayUtil.mapToXml(map));
    }
}
