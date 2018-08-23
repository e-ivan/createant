package com.qnyy.re.business.util;

import org.apache.commons.lang3.time.DateFormatUtils;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Date;

/**
 * 支付宝工具类
 * Created by hh on 2017.6.27 0027.
 */

public class AlipayUtil {

    private AlipayUtil(){}
    private static final String NOTIFY_URL = "http://112.74.188.232:8111/qnyy/alipay/notifyUrl";
    private static final String PARTNER = "2088521488533083";
    private static final String SELLER = "2867195069@qq.com";
    private static final String RSA_PRIVATE = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCIFOQ9HK7AYcHNuLazu3BRC59Qh2/3gO+hx/DZvIqLaQgaVT+dC03nZOrxL1f85si7G0UQGdgm9qYpzdIQfEpPF7ChP+W7I1CKK5pfvYoIihYitf0F53ry2yRSyctuUlWen4vZ3+38ljoPhHBJ9r9umHGKljo0flPRn0V0ol930EqKQhXLkTDghm6hTbeZnSU69XW9UgeZ9M4zGe4pyOVy5XnGqQUJHOEnBEVeRvwVo91aA4aKn0n6PPYCMoeJJBqOcmwRhhpMZhBLVVDi3NXTZ0tfNJtuY/Fk6AscO60XnmkSaqNRnwxLCR4RLb2I3Mwy37VnuWIcoZXgLudaIHOPAgMBAAECggEAVzx1WIvzpxbEI/p+/Vdy2DHILtq0N4lrlV3Uk/DfLCHn9OGjhyyBA47ZYkZEdLcKBZCkxH6c59WEjhYIzVKwXGXGcIjC4CXycrEjJK8gNvs214etLcTIN7muXnhItm+Hvclv4PtB2IQBsoLnbTMGY4LMfthxdcdqOl6NKwkQhICOihDcwEbHIkktc/16f4d0oMIMZMlRAXOWHCBiWfTWGKnIgaph6u/e9YPc3OTsDN3YDsJLemy8IB4mG5JR6Ov+hronTxh8ZXe4HkDDMKw/9aZ0OWdaJyv5QCYZ4wxe96yR8w7KTV/bo1P870sFlq+BL5afZvdCWiT7PGacnaLEgQKBgQC68Hgyvdx4WUmTFWEGU+BIlOEMd5L6MTCQliVhEoNX34ZX9wQFeJOFWaxfuZbH4Fwgjyha1S9JBRqwgDMMiHj/zxNgG/DepJ+EaJf8numZqWA9CRiyQFXzuO4J8QTSE4ONTMc0AXLOk8hiEqqevipRc4suKUoIw7ezLGRjJAdOTwKBgQC6Wp9bQAo20ZY/qMoJXrui3TuN0kXgCiCKEwE/LFK1UD97b3xrSwl9gImoveK32aYniDYD3CQk7XFAjjymEaawrqi+3uxKtlszznSdT1RwO4NRiOMz5kSiLepIk21kFGIRVCnbqgY/u0n4Y8ix5KeAAu/lioAV5DYyReeNGZp2wQKBgDBEyyR+9aB1nZPG32ic27qiBoILlf3YGawbuMcZ2qQj6TJpa7Zl7eFuyB1ndFFj0yXtEQRzK2R1VvDgCU5hJq0WsPAOBuHWLAIqZ6Qo/DRbEahw4tmT1x25UG1suEvuEfe4LW92OEiyy/jWzQt0GsUxSmy+6Ije0F4JRlp7AQ/tAoGBALnJCSBW+xfl4W3739hxmo1nFC1y2fje/8vH4LFThe1/YJq17gulloOJdzREgtgZ4917naZBfcNW+VaLt4k1BcMVjeQkqNxfaxN+G6KNaTgvDN9nomOmiPxK3EyYtjFVZOOzzbtSicJV/Qvmpzo7JIap5vMoHkQ07Zc3O/7e7LDBAoGAJTgl92/zx3ztXffCMNUaOXCStGmqKHJgFxQ4M5V57T4yp4m9BcXEWEKSLDqIl619ld6mDVpz5BLgYKsBPcOpFj7oWEYiZb+50QwnpAz1v6KnRjOAgfrVmWiLeJ9bQhHoRJPHXxInwMnptYGYk6i/rys8XZkXSwlkpdVf3CwW7cM=";
    private static final String alipay_public_key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAz2ChzlnvV3zPMPuIbQNJzjd3H+BVWOHIhACyjmPYt+4gzeg4EVkkP4LxmENzaNGRq2tCMeRdfNGQZFUpzR9vjAl0IpYRyqx08wsTSu46OdmDDzXoPcPbps3wScRLJr/3FDUD7RiO0hQtO1CpwE8dVlAOgSnMFD7ocTEIvZ2vdlUbRgt3XjdsvakJA12CEvtjEP0F1vxFULofG/1+SAqSC9euUFdqtqNhclEoV08VaVwRh1IFGT8lWT1UBl1uw7t6e2zGgcEUxitqe43NyyI/jbmi8xZREwEqGz3BcQ0WFhW/IewzPFodXcbQbGJgBr7rAU7ZXEWwqF0FD7lx0y6oBwIDAQAB";
    private static final String app_id = "2017120100296524";

    private static final String testAppId = "2016080400162613";
    private static final String testAlipayPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAqB66tuLECtAt3u7hTLCoJPkZq4/c4O+CVkeiWRh2CfXjcL0xVCloS+ddaby9un8Z4CngUfUJ32lUovjbvuwo+EdBJwq/3ittkUxorklPI7cnjCguuH9tjgRtY1UQWPI5NdyB8pvVZbFHz4gpND3Ja6BpKwieaDrUdRRZXg1XeWf7+kNWaGUSw3QveEBqLbR+k0b8apRu+ZpoNM6lze6qtAo7/v6z/M9/lvTjNR8oe0KUpoxp4Q+PXFNyidQQlTw5Jf2aqazOkeBnrVbEwI69epA1Mi6mooBX25RNdf90kVvPUkKm1bK6TuREbKmNcSkZ6Y98HwsGYYOYcBrt/C6fVwIDAQAB";
    private static final String testPrivateKey = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCoHrq24sQK0C3e7uFMsKgk+Rmrj9zg74JWR6JZGHYJ9eNwvTFUKWhL511pvL26fxngKeBR9QnfaVSi+Nu+7Cj4R0EnCr/eK22RTGiuSU8jtyeMKC64f22OBG1jVRBY8jk13IHym9VlsUfPiCk0PclroGkrCJ5oOtR1FFleDVd5Z/v6Q1ZoZRLDdC94QGottH6TRvxqlG75mmg0zqXN7qq0Cjv+/rP8z3+W9OM1Hyh7QpSmjGnhD49cU3KJ1BCVPDkl/ZqprM6R4GetVsTAjr16kDUyLqaigFfblE11/3SRW89SQqbVsrpO5ERsqY1xKRnpj3wfCwZhg5hwGu38Lp9XAgMBAAECggEAe3gOAnCqL5LSjW580njPFFBJdQEz45Ki80Cqz6oyToBiaBLaXogGi/BAX355DLV5yHKYxzgh5w0RVptTr3T7ZdPW2ph/bugQEOy/2HgIgh9qEPoVzfPv/qPrvk7jsZ87Ov5WBVpDL+sx5Q2HzVEvUza85hjvVXcXP2ERfQxpWMQ9Ec6dWCh348JFM4mLq6phW9inqAnQYtWJejUtrfXq8Vid8AcEvAqc5g7TTfcslE3+wuzCy9vsB8qUbDIuRNQa++XO8mRcGuwJR2xstVr0qrKeQ1eNWLtHRFiWZXKPZKIJ5RVeD0/jCucFXU0IMdJf5sTsveJ5azaSimC0lAi/EQKBgQDtAhvH6RRa+NJtLmvVwhM9mqWlSJswRcLewXcrXJief7JW1W+jEZu+NwdLSGgRsh3C74YiICHEAAg4Sj/gPr2Q43KRHiO24VJwzUExmLa4ns8Y/tM6npOtICithzpQaFmvQjKPkAoR1XuI5yzAI9ytewpVM9XgNMDkkpFkOmJBfwKBgQC1l3nergE/zZlS0VFmkjOiAaq1nikBcpM8f8+T6e6LQ6iXO6CKJ9L+6km4AOncCsp6jk/AJwOyppxGR9zEVXtwvJ3MLkjNJRrkAynYGW0hxJ2v4hNwT7mVwxMwrGmp7A7D6IQcCL4g1xgsGmbSbLwrBoRTLB9kv0BgfkExGVreKQKBgQCrYLPv6SUz5upZ0pBdIBnDmPigPJEi5b7UTja6nxkouxOc4Hb6BjiE4cufbzr5WL9WftaSBIQF8zP+aAoqP5PZme3060NN8T4onPLARzoQ45zC6TiaEaKdNDzVxYuwKtR2+zoOP0xYuvK5+teMFCJ1Oxwsoe/MULkOTsFdJS55RwKBgDxBIO0kq8FyBTgXwHzackuFZhGRg2W5kOV4O3aHHuIS9zQHUwPEpxxyvTdTBkY/RmRYKFpIF9AFvMENQ1HaU3VbALkpenqnnz6dVhsghLe5t+EOkQipS5lH+2whKYZ6j4Ot+Xgb5oqbFKg9snSMI/va07mNYHVbiI78FcGm+iHRAoGBAN4MElFsvFhvQXzLqygdwiSAuVWh3v+o08GV6/T5WAbVOhzfMVBpLGadq255YQ+ukCkQ+WvJoxmpEoD0WbjbnQXA3X4ZrQYXx+1hQ7eTmXpob4+xCwt/9kxaW/8NRyzM2ml4EV9zkD5dBmxddtg8CC73Np/pM8xrl3tVVdZ6soEU";

    /**
     * create the order info. 创建订单信息
     */
    private static String getOrderInfo(String subject, String body, String price,String outTradeNo) {
        // 签约合作者身份ID
        String orderInfo = "partner=" + "\"" + PARTNER + "\"";
        // 签约卖家支付宝账号
        orderInfo += "&seller_id=" + "\"" + SELLER + "\"";
        // 商户网站唯一订单号
        orderInfo += "&out_trade_no=" + "\"" + outTradeNo + "\"";
        // 商品名称
        orderInfo += "&subject=" + "\"" + subject + "\"";
        // 商品详情
        orderInfo += "&body=" + "\"" + body + "\"";
        // 商品金额
        orderInfo += "&total_fee=" + "\"" + price + "\"";
        // 服务器异步通知页面路径
        orderInfo += "&notify_url=" + "\"" + NOTIFY_URL + "\"";
        // 服务接口名称， 固定值
        orderInfo += "&service=\"mobile.securitypay.pay\"";
        // 支付类型， 固定值
        orderInfo += "&payment_type=\"1\"";
        // 参数编码， 固定值
        orderInfo += "&_input_charset=\"utf-8\"";
        // 设置未付款交易的超时时间
        // 默认30分钟，一旦超时，该笔交易就会自动被关闭。
        // 取值范围：1m～15d。
        // m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
        // 该参数数值不接受小数点，如1.5h，可转换为90m。
        orderInfo += "&it_b_pay=\"30m\"";
        // extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
        // orderInfo += "&extern_token=" + "\"" + extern_token + "\"";
        // 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
        orderInfo += "&return_url=\"m.alipay.com\"";
        // 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
        // orderInfo += "&paymethod=\"expressGateway\"";
        return orderInfo;
    }

    private static String getNewOrderInfo(String subject, String body, String price,String outTradeNo) {
        // 签约合作者身份ID
        String orderInfo = "app_id=" + app_id;
        // 签约卖家支付宝账号
        orderInfo += "&biz_content=";
        String bizContent = "{" +
                "\"body\":\""+body+"\"," +
                "\"subject\":\""+subject+"\"," +
                "\"out_trade_no\":\"" + outTradeNo + "\"," +
                "\"timeout_express\":\"15m\"," +
                "\"total_amount\":\""+price+"\"," +
                "\"product_code\":\"QUICK_MSECURITY_PAY\"" +
                "}";
        orderInfo += bizContent;
        orderInfo += "&charset=utf-8";
        orderInfo += "&method=alipay.trade.app.pay";
        orderInfo += "&notify_url=" + NOTIFY_URL;
        // 商户网站唯一订单号
        // 商品名称
        orderInfo += "&sign_type=RSA2";
        // 商品详情
        orderInfo += "&timestamp=" + DateFormatUtils.format(new Date(),"yyyy-MM-dd HH:mm:ss");
        // 商品金额
        orderInfo += "&version=1.0";
        // 服务器异步通知页面路径
        // 服务接口名称， 固定值


        return orderInfo;
    }

    /**
     * 获取支付宝订单支付明细
     * @param subject   商品描述  对应title
     * @param body      商品详情  对应detail
     * @param price     商品价格  对应totalFee
     * @param outTradeNo
     * @return
     */
    public static String getPayInfo(String subject, String body, BigDecimal price, String outTradeNo){
        String orderInfo = getOrderInfo(subject, body, price.toString(), outTradeNo);
        String sign = sign(orderInfo);
        try {
            /**
             * 仅需对sign 做URL编码
             */
            sign = URLEncoder.encode(sign, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        /**
         * 完整的符合支付宝参数规范的订单信息
         */
        final String payInfo = orderInfo + "&sign=\"" + sign + "\"&" + getSignType();
        return payInfo;
    }

    /**
     * sign the order info. 对订单信息进行签名
     *
     * @param content 待签名订单信息
     */
    private static String sign(String content) {
        return sign(content, RSA_PRIVATE);
    }


    /**
     * get the sign type we use. 获取签名方式
     */
    private static String getSignType() {
        return "sign_type=\"RSA\"";
    }


    private static final String ALGORITHM = "RSA";

    private static final String SIGN_ALGORITHMS = "SHA1WithRSA";

    private static final String DEFAULT_CHARSET = "UTF-8";

    private static String sign(String content, String privateKey) {
        try {
            PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(Base64.decode(privateKey));
            KeyFactory keyf = KeyFactory.getInstance(ALGORITHM);
            PrivateKey priKey = keyf.generatePrivate(priPKCS8);

            java.security.Signature signature = java.security.Signature
                    .getInstance(SIGN_ALGORITHMS);

            signature.initSign(priKey);
            signature.update(content.getBytes(DEFAULT_CHARSET));

            byte[] signed = signature.sign();

            return Base64.encode(signed);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

}
