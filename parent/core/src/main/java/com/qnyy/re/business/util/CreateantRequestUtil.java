package com.qnyy.re.business.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.qnyy.re.base.entity.UserAccount;
import com.qnyy.re.base.entity.UserAccountStatement;
import com.qnyy.re.base.enums.CommonErrorResultEnum;
import com.qnyy.re.base.util.AccountUtil;
import com.qnyy.re.base.util.HttpClientUtils;
import com.qnyy.re.base.util.MD5Util;
import com.qnyy.re.base.util.exception.BusinessException;
import com.qnyy.re.business.vo.StoreVO;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 创蚁请求工具类
 * Created by E_Iva on 2017.12.13.0013.
 */
@Slf4j
@Component
public class CreateantRequestUtil {

    private static String appSecret;//appSecret
    private static String appKey;//appKey
    private static String requestUrl;//请求url

    @Value("${createantAppSecret}")
    public void setAppSecret(String appSecret) {
        CreateantRequestUtil.appSecret = appSecret;
    }

    @Value("${createantAppKey}")
    public void setAppKey(String appKey) {
        CreateantRequestUtil.appKey = appKey;
    }


    @Value("${createantRequestUrl}")
    public void setRequestUrl(String requestUrl) {
        CreateantRequestUtil.requestUrl = requestUrl;
    }

    private static JSONObject baseRequest(Map<String, String> paramMap, String action) {
        paramMap.put("app_key", appKey);
        paramMap.put("timestamp", String.valueOf(System.currentTimeMillis()));
        //加密签名
        paramMap.put("sign", buildSignParam(paramMap));
        HttpUriRequest post = HttpClientUtils.getRequestMethod(paramMap, HttpClientUtils.jointUrl(requestUrl, action), "post");
        CloseableHttpClient client = HttpClientUtils.getHttpClient();
        try (CloseableHttpResponse response = client.execute(post)) {
            String result = EntityUtils.toString(response.getEntity());
            if (response.getStatusLine().getStatusCode() == 200) {
                JSONObject jsonObject = JSONObject.parseObject(result);
                if (!BooleanUtils.toBoolean(jsonObject.getString("success"))) {
                    log.error("请求异常 {}，请求接口{}，请求参数{}", result, action, paramMap);
                    throw new BusinessException(CommonErrorResultEnum.OPTIMISTIC_LOCK_ERROR);
                }
                return jsonObject;
            } else {
                log.error("请求异常{}，请求接口{}，请求参数{}", result, action, paramMap);
                throw new BusinessException(CommonErrorResultEnum.REQUEST_TIMEOUT);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("请求异常，请求接口{}，请求参数{}", action, paramMap);
            throw new BusinessException(CommonErrorResultEnum.REQUEST_TIMEOUT);
        }
    }

    private static String buildSignParam(Map<String, String> map) {
        List<Map.Entry<String, String>> entries = new ArrayList<>(map.entrySet());
        //排序
        entries.sort(Comparator.comparing(o -> o.getKey().toLowerCase()));
        StringBuilder strBuild = new StringBuilder();
        entries.forEach(e -> {
            String value = e.getValue();
            if (StringUtils.isNotEmpty(value)) {
                strBuild.append(e.getKey().toLowerCase()).append(value.toLowerCase());
            }
        });
        strBuild.append(appSecret.toLowerCase());
        String encode = MD5Util.encode(strBuild.toString());
        return StringUtils.isNotEmpty(encode) ? encode.toUpperCase() : "";
    }

    public static Map<String, Long> queryUserIdByToken(Collection<String> tokens) {
        Map<String, String> map = new HashMap<>();
        map.put("createant_openids",StringUtils.join(tokens,","));
        JSONArray data = baseRequest(map, "IMapRedPacket.BatchGetUserId").getJSONArray("data");
        System.out.println("data = " + data);
        List<UserTemp> userTemps = data.toJavaList(UserTemp.class);
        return userTemps.stream().collect(Collectors.toMap(UserTemp::getCreateantOpenId, UserTemp::getUserId));
    }

    @Getter@Setter
    public static class UserTemp {
        private String CreateantOpenId;
        private Long UserId;
    }


    /**
     * 获取用户账户
     *
     * @param createantToken
     * @return
     */
    public static UserAccount getUserAccount(String createantToken) {
        Map<String, String> map = new HashMap<>();
        map.put("createant_openid", createantToken);
        return baseRequest(map, "IMapRedPacket.GetAccountBalance").getObject("data", UserAccount.class);
    }

    /**
     * 更新用户账户
     *  @param createantToken
     * @param sourceCreateantToken
     * @param version
     * @param userAccountStatement
     */
    public static void updateUserAccount(String createantToken, String sourceCreateantToken, Integer version, UserAccountStatement userAccountStatement) {
        Map<String, String> map = new HashMap<>();
        map.put("createant_openid", createantToken);
        BigDecimal amount = userAccountStatement.getAmount();
        AccountUtil.AccountDealType dealType = AccountUtil.AccountDealType.getByCode(userAccountStatement.getDealType());
        Boolean symbol = AccountUtil.AccountDealType.getSymbol(userAccountStatement.getDealType());
        if (symbol == null) {
            throw new BusinessException(CommonErrorResultEnum.REJECT_USE_BALANCE);
        }
        map.put("amount", (symbol ? amount : amount.negate()).toString());
        map.put("settle_type", dealType.getCreateantCode().toString());
        map.put("sourc_bill_no", userAccountStatement.getId().toString());
        map.put("source_createant_openid", StringUtils.defaultString(sourceCreateantToken, ""));
        map.put("version", version.toString());
        map.put("remark", userAccountStatement.getRemark());
        baseRequest(map, "IMapRedPacket.AddAccountItem");
    }

    /**
     * 获取店铺信息
     */
    public static List<StoreVO> getStoreInfo(Collection<Long> storeIds, boolean isDetail) {
        if (storeIds == null || storeIds.isEmpty()) {
            return new ArrayList<>();
        }
        Map<String, String> map = new HashMap<>();
        map.put("ids", StringUtils.join(storeIds, ","));
        map.put("is_detail", isDetail ? "true" : "false");
        String data = baseRequest(map, "/IMapRedPacket.GetShopBranchName").getString("data");
        if (StringUtils.isBlank(data)) {
            return new ArrayList<>();
        }
        return JSON.parseArray(data, StoreVO.class);
    }

    public static StoreVO getStoreInfo(Long storeId, boolean isDetail) {
        try {
            List<StoreVO> storeInfo = getStoreInfo(Collections.singletonList(storeId), isDetail);
            if (!storeInfo.isEmpty()) {
                return storeInfo.get(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取公众号openId对应的创蚁token
     */
    public static String getCreateantTokenByMpOpenId(String openId) {
        Map<String, String> map = new HashMap<>();
        map.put("wechat_openid", openId);
        return baseRequest(map, "IMapRedPacket.GetCreateantOpenId").getString("data");
    }

}
