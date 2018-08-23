package com.qnyy.re.base.util;

import com.alibaba.fastjson.JSONObject;
import com.qnyy.re.base.enums.CommonErrorResultEnum;
import com.qnyy.re.base.util.exception.BusinessException;
import com.qnyy.re.base.vo.AddressComponentVO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by E_Iva on 2017.12.13.0013.
 */
@Component
public class AmapUtil {

    private static String amapAppKey;//高德地图appKey
    private static String amapAppSecret;//高德地图请求密钥
    private static String amapRequestUrl;//高德地图请求url

    @Getter@AllArgsConstructor
    public enum COORDSYS{
        GPS("gps"),
        MAPBAR("mapbar"),
        BAIDU("baidu"),
        ;
        String value;
    }

    @Value("${amapAppKey}")
    public void setAmapAppKey(String amapAppKey) {
        AmapUtil.amapAppKey = amapAppKey;
    }


    @Value("${amapAppSecret}")
    public void setAmapAppSecret(String amapAppSecret) {
        AmapUtil.amapAppSecret = amapAppSecret;
    }
    @Value("${amapRequestUrl}")
    public void setAmapRequestUrl(String amapRequestUrl) {
        AmapUtil.amapRequestUrl = amapRequestUrl;
    }

    private static String sortMap2BuildParams(Map<String, String> map, String secret) {
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
        for (int i = 0; i < infoIds.size(); i++) {
            Map.Entry<String, String> entry = infoIds.get(i);
            String value = entry.getValue();
            if (StringUtils.isNoneEmpty(value) && !StringUtils.equals("sig", entry.getKey())) {
                strBuild.append(entry.getKey()).append("=").append(value);
            }
            if (i != infoIds.size() - 1) {
                strBuild.append("&");
            }
        }
        return strBuild.append(secret).toString();
    }

    private static JSONObject baseAmapRequest(Map<String, String> paramMap, String action) {
        paramMap.put("key", amapAppKey);

        //加密签名
        String format = sortMap2BuildParams(paramMap,amapAppSecret);
        String sig = MD5Util.encode(format);
        paramMap.put("sig", sig);
        HttpUriRequest post = HttpClientUtils.getRequestMethod(paramMap, HttpClientUtils.jointUrl(amapRequestUrl, action), "post");
        CloseableHttpClient client = HttpClientUtils.getHttpClient();
        try (CloseableHttpResponse response = client.execute(post)) {
            if (response.getStatusLine().getStatusCode() == 200) {
                String result = EntityUtils.toString(response.getEntity());
                System.out.println("result = " + result);
                JSONObject jsonObject = JSONObject.parseObject(result);
                if (!jsonObject.getBoolean("status")) {
                    throw new BusinessException(CommonErrorResultEnum.OPTIMISTIC_LOCK_ERROR);
                }
                return jsonObject;
            } else {
                throw new BusinessException(CommonErrorResultEnum.REQUEST_TIMEOUT);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new BusinessException(CommonErrorResultEnum.OPTIMISTIC_LOCK_ERROR);
        }
    }

    public static AddressComponentVO getAddressByLocation(String lng, String lat) {
        if (StringUtils.isAnyBlank(lng,lat)) {
            return null;
        }
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("location", coordinateConvert(COORDSYS.BAIDU,lng + "," + lat));
        try {
            return baseAmapRequest(paramMap, "/geocode/regeo").getJSONObject("regeocode").getObject("addressComponent", AddressComponentVO.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static AddressComponentVO getAddressByIp(String ip) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("ip", ip);
        try {
            return baseAmapRequest(paramMap, "/ip").toJavaObject(AddressComponentVO.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 坐标转换
     */
    public static String coordinateConvert(COORDSYS coordsys,String... locations) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("coordsys", coordsys.getValue());
        paramMap.put("locations", StringUtils.join(locations,"|"));
        try {
            return baseAmapRequest(paramMap, "/assistant/coordinate/convert").getString("locations");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public static void main(String[] args) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("address", "北京市");
        System.out.println(baseAmapRequest(paramMap, "/geocode/geo"));
    }
}
