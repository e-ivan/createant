package com.qnyy.re.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.*;


/**
 * Created by E_Iva on 2018.2.11.0011.
 */
@Slf4j
public class SignUtil {

    public static final String SIGN_KEY_NAME = "signature";

    public static boolean checkSign(IdentityHashMap<String, String> map, String secret) {
        String signature = map.get(SIGN_KEY_NAME);
        //如果不对，再判断一次不替换+的情况
        return StringUtils.equals(encrypt(sortMap2BuildParams(map, secret, true)), signature)
                || StringUtils.equals(encrypt(sortMap2BuildParams(map, secret, false)), signature);
    }

    //排序和组装字符串
    public static String sortMap2BuildParams(IdentityHashMap<String, String> map, String secret, boolean replace) {
        StringBuilder strBuild = new StringBuilder();
        List<Map.Entry<String, String>> infoIds = new ArrayList<>(map.entrySet());
        //排序
        infoIds.sort((map1, map2) -> {
            if (StringUtils.equals(map1.getKey(), map2.getKey())) {
                return StringUtils.isAnyEmpty(map1.getValue(),map2.getValue()) ? 0 : (map1.getValue()).compareTo(map2.getValue());
            } else {
                return (map1.getKey()).compareTo(map2.getKey());
            }
        });
        infoIds.forEach(entry -> {
            String value = entry.getValue();
            if (StringUtils.isNoneEmpty(value) && !StringUtils.equals(SIGN_KEY_NAME, entry.getKey())) {
                try {
                    String encode = URLEncoder.encode(value, "UTF-8");
                    strBuild.append(replace ? encode.replaceAll("\\+", "%20") : encode);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });
        return strBuild.append(secret).toString();
    }

    public static Map<String,String> signWxJSApiParam(String jsapiTicket, String url) {
        Map<String, String> map = new HashMap<>();
        String nonce_str = UUID.randomUUID().toString();
        String timestamp = Long.toString(System.currentTimeMillis() / 1000);
        String string1;

        //注意这里参数名必须全部小写，且必须有序
        string1 = "jsapi_ticket=" + jsapiTicket +
                "&noncestr=" + nonce_str +
                "&timestamp=" + timestamp +
                "&url=" + url;
        map.put("url", url);
        map.put("jsapi_ticket", jsapiTicket);
        map.put("nonceStr", nonce_str);
        map.put("timestamp", timestamp);
        map.put("signature", SHA1Util.Encrypt(string1));
        return map;
    }

    private static String encrypt(String strSrc) {
        MessageDigest md;
        String strDes;
        try {
            byte[] bt = strSrc.getBytes("UTF-8");
            md = MessageDigest.getInstance("SHA-1");
            md.update(bt);
            strDes = bytes2Hex(md.digest());
        } catch (Exception ex) {
            return null;
        }
        return strDes.toUpperCase();
    }

    private static String bytes2Hex(byte[] bytes) {
        String des = "";
        String tmp;
        for (byte aByte : bytes) {
            tmp = (Integer.toHexString(aByte & 0xFF));
            if (tmp.length() == 1) {
                des += "0";
            }
            des += tmp;
        }
        return des;
    }
}
