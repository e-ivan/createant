package com.qnyy.re.base.util;

import com.alibaba.fastjson.JSONObject;
import com.qnyy.re.base.enums.CommonErrorResultEnum;
import com.qnyy.re.base.util.exception.BusinessException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 融云工具类
 * Created by E_Iva on 2017.12.7.0007.
 */
@Component
public class RongYunUtil {
    private static final String APP_KEY = "App-Key";
    private static final String NONCE = "Nonce";
    private static final String TIMESTAMP = "Timestamp";
    private static final String SIGNATURE = "Signature";

    private static String appKey;
    private static String appSecret;
    private static String api;

    @Value("${rongYunApi}")
    public void setApi(String api) {
        RongYunUtil.api = api;
    }

    @Value("${rongYunAppKey}")
    public void setAppKey(String appKey) {
        RongYunUtil.appKey = appKey;
    }

    @Value("${rongYunAppSecret}")
    public void setAppSecret(String appSecret) {
        RongYunUtil.appSecret = appSecret;
    }

    public static String registerUserToken(Long userId, String name, String portraitUri) throws Exception {
        if (userId == null) {
            throw new BusinessException(CommonErrorResultEnum.REQUEST_PARAM_LACK, "userId");
        }
        if (name == null) {
            throw new BusinessException(CommonErrorResultEnum.REQUEST_PARAM_LACK, "name");
        }
        if (portraitUri == null) {
            throw new BusinessException(CommonErrorResultEnum.REQUEST_PARAM_LACK, "portraitUri");
        }
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("userId", userId.toString());
        paramMap.put("name", name);
        paramMap.put("portraitUri", portraitUri);
        return baseRongYunRequest(paramMap, "/user/getToken").getString("token");
    }

    private static JSONObject baseRongYunRequest(Map<String, String> paramMap, String action) {
        String nonce = String.valueOf(Math.random() * 1000000);
        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
        HttpUriRequest post = HttpClientUtils.getRequestMethod(paramMap, HttpClientUtils.jointUrl(api, action) + ".json", "post");
        post.addHeader(APP_KEY, appKey);
        post.addHeader(NONCE, nonce);
        post.addHeader(TIMESTAMP, timestamp);
        post.addHeader(SIGNATURE, CodeUtil.hexSHA1(appSecret + nonce + timestamp));
        CloseableHttpClient client = HttpClientUtils.getHttpClient();
        try (CloseableHttpResponse response = client.execute(post)) {
            if (response.getStatusLine().getStatusCode() == 200) {
                String result = EntityUtils.toString(response.getEntity());
                JSONObject jsonObject = JSONObject.parseObject(result);
                Integer code = jsonObject.getInteger("code");
                if (code == null || code != 200) {
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
}
