package com.qnyy.re.business.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.qnyy.re.base.enums.CommonErrorResultEnum;
import com.qnyy.re.base.util.HttpClientUtils;
import com.qnyy.re.base.util.exception.BusinessException;
import com.qnyy.re.business.entity.ShakearoundPage;
import com.qnyy.re.business.vo.WxMpAccessTokenVO;
import com.qnyy.re.business.vo.WxMpJSApiTicketVO;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 微信公共平台请求工具
 * Created by E_Iva on 2018.5.23 0023.
 */
@Component
public class WeiXinMpRequestUtil {
    private static String requestUrl;
    public static String appId;
    private static String secret;
    private static MongoTemplate template;


    @Value("${wxMpRequestUrl}")
    public void setRequestUrl(String requestUrl) {
        WeiXinMpRequestUtil.requestUrl = requestUrl;
    }

    @Value("${wxMpAppId}")
    public void setAppId(String appId) {
        WeiXinMpRequestUtil.appId = appId;
    }

    @Value("${wxMpSecret}")
    public void setSecret(String secret) {
        WeiXinMpRequestUtil.secret = secret;
    }

    @Autowired
    public void setTemplate(MongoTemplate template) {
        WeiXinMpRequestUtil.template = template;
    }

    private static String requestAccessToken() {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("grant_type", "client_credential");
        paramMap.put("appid", appId);
        paramMap.put("secret", secret);
        HttpUriRequest get = HttpClientUtils.getRequestMethod(paramMap, HttpClientUtils.jointUrl(requestUrl,"cgi-bin/token"), "get");
        CloseableHttpClient client = HttpClientUtils.getHttpClient();
        try (CloseableHttpResponse response = client.execute(get)) {
            String result = EntityUtils.toString(response.getEntity());
            System.out.println("result = " + result);
            WxMpAccessTokenVO vo = JSONObject.parseObject(result, WxMpAccessTokenVO.class);
            if (vo.getErrcode() != null && vo.getErrcode() != null) {
                throw new BusinessException(CommonErrorResultEnum.OPTIMISTIC_LOCK_ERROR);
            }
            vo.setCreated(new Date());
            template.insert(vo);
            return vo.getAccessToken();
        } catch (IOException e) {
            e.printStackTrace();
            throw new BusinessException(CommonErrorResultEnum.OPTIMISTIC_LOCK_ERROR);
        }
    }

    /**
     * 获取accessToken
     */
    public static String getAccessToken() {
        WxMpAccessTokenVO accessTokenVO = template.findOne(new Query().with(new Sort(Sort.Direction.DESC,"created")), WxMpAccessTokenVO.class);
        //测试accessToken有效性
        if (accessTokenVO == null || !checkAccessToken(accessTokenVO.getAccessToken()) || DateUtils.addSeconds(accessTokenVO.getCreated(), accessTokenVO.getExpiresIn() / 2).before(new Date())) {
            //accessToken过期，重新获取
            return requestAccessToken();
        }
        return accessTokenVO.getAccessToken();
    }

    public static boolean checkAccessToken(String accessToken) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("access_token", accessToken);
        HttpUriRequest get = HttpClientUtils.getRequestMethod(paramMap, HttpClientUtils.jointUrl(requestUrl ,"cgi-bin/getcallbackip"),"get");
        CloseableHttpClient client = HttpClientUtils.getHttpClient();
        try (CloseableHttpResponse response = client.execute(get)) {
            String result = EntityUtils.toString(response.getEntity());
            Integer errcode = JSONObject.parseObject(result).getInteger("errcode");
            return errcode == null || errcode == 0;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 获取微信公众号jsapi_ticket
     */
    public static String getJsapiTicket() {
        WxMpJSApiTicketVO mpJSApiTicketVO = template.findOne(new Query().with(new Sort(Sort.Direction.DESC, "created")), WxMpJSApiTicketVO.class);
        if (mpJSApiTicketVO == null || DateUtils.addSeconds(mpJSApiTicketVO.getCreated(), mpJSApiTicketVO.getExpiresIn() / 2).before(new Date())) {
            return requestJsapiTicket();
        }
        return mpJSApiTicketVO.getTicket();
    }

    private static String requestJsapiTicket() {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("access_token", getAccessToken());
        paramMap.put("type", "jsapi");
        HttpUriRequest post = HttpClientUtils.getRequestMethod(paramMap, HttpClientUtils.jointUrl(requestUrl ,"cgi-bin/ticket/getticket"),"get");
        CloseableHttpClient client = HttpClientUtils.getHttpClient();
        try (CloseableHttpResponse response = client.execute(post)) {
            String result = EntityUtils.toString(response.getEntity());
            System.out.println("result = " + result);
            WxMpJSApiTicketVO vo = JSONObject.parseObject(result, WxMpJSApiTicketVO.class);
            if (vo.getErrcode() != null && vo.getErrcode() != 0) {
                throw new BusinessException(CommonErrorResultEnum.OPTIMISTIC_LOCK_ERROR);
            }
            vo.setCreated(new Date());
            template.insert(vo);
            return vo.getTicket();
        } catch (IOException e) {
            e.printStackTrace();
            throw new BusinessException(CommonErrorResultEnum.OPTIMISTIC_LOCK_ERROR);
        }
    }

    /**
     * 摇一摇通用请求
     *
     * @param jsonParams
     * @param action
     * @return
     */
    private static JSONObject baseShakearoundRequest(JSONObject jsonParams, File file,String action) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("access_token", getAccessToken());
        HttpUriRequest post;
        if (file != null) {
            Map<String, File> fileMap = new HashMap<>();
            fileMap.put("media", file);
            post = HttpClientUtils.getRequestMethod(paramMap, fileMap, HttpClientUtils.jointUrl(requestUrl + "/shakearound", action));
        }else {
            post = HttpClientUtils.getRequestMethod(paramMap, jsonParams, HttpClientUtils.jointUrl(requestUrl + "/shakearound", action));
        }
        CloseableHttpClient client = HttpClientUtils.getHttpClient();
        try (CloseableHttpResponse response = client.execute(post)) {
            String result = EntityUtils.toString(response.getEntity());
            System.out.println("result = " + result);
            JSONObject jsonObject = JSONObject.parseObject(result);
            switch (jsonObject.getInteger("errcode")) {
                case 0:
                    try {
                        return jsonObject.getJSONObject("data");
                    } catch (Exception e) {
                        return new JSONObject();
                    }
                case 40001:
                case 42001:
                    throw new BusinessException(CommonErrorResultEnum.MP_ACCESS_TOKEN_EXPIRED);
            }
            throw new BusinessException(CommonErrorResultEnum.OPTIMISTIC_LOCK_ERROR);
        } catch (IOException e) {
            e.printStackTrace();
            throw new BusinessException(CommonErrorResultEnum.OPTIMISTIC_LOCK_ERROR);
        }
    }
    private static JSONObject baseShakearoundRequest(JSONObject jsonParams,String action) {
        return baseShakearoundRequest(jsonParams, null, action);
    }

    /**
     * 申请设备
     *
     * @param count
     * @param reason
     * @param comment
     * @return
     */
    public static JSONObject deviceApply(Integer count, String reason, String comment) {
        JSONObject json = new JSONObject();
        json.put("quantity", count);
        json.put("apply_reason", reason);
        json.put("comment", comment);
        return baseShakearoundRequest(json, "device/applyid");
    }

    /**
     * 查询设备 ID 申请审核状态
     */
    public static JSONObject deviceApplyStatus(Integer applyId) {
        JSONObject json = new JSONObject();
        json.put("apply_id", applyId);
        return baseShakearoundRequest(json, "device/applystatus");
    }

    /**
     * 通过申请 ID 查询设备列表
     */
    public static JSONObject deviceSearch(Integer applyId, Integer count) {
        JSONObject json = new JSONObject();
        json.put("type", 3);
        json.put("apply_id", applyId);
        json.put("begin", 0);
        json.put("count", count);
        return baseShakearoundRequest(json, "device/search");
    }

    /**
     * 获取摇周边的设备及用户信息
     */
    public static JSONObject getShakeInfo(String ticket) {
        JSONObject json = new JSONObject();
        json.put("ticket", ticket);
        json.put("need_id", 1);
        return baseShakearoundRequest(json, "user/getshakeinfo");
    }

    /**
     * 添加页面
     */
    public static Integer addDevicePage(ShakearoundPage page) {
        JSONObject json = JSONObject.parseObject(JSON.toJSONString(page));
        return baseShakearoundRequest(json, "page/add").getInteger("page_id");
    }

    /**
     * 设备绑定页面
     */
    public static void deviceBindPage(Integer deviceId, Integer pageId) {
        JSONObject json = new JSONObject();
        JSONObject deviceJson = new JSONObject();
        deviceJson.put("device_id", deviceId);
        json.put("device_identifier", deviceJson);
        json.put("page_ids", pageId != null ? new Integer[]{pageId} : new Integer[]{});
        System.out.println("json = " + json);
        baseShakearoundRequest(json, "device/bindpage");
    }

    /**
     * 设备编辑备注
     */
    public static void deviceUpdateComment(Integer deviceId, String comment) {
        JSONObject json = new JSONObject();
        JSONObject deviceJson = new JSONObject();
        deviceJson.put("device_id", deviceId);
        json.put("device_identifier", deviceJson);
        json.put("comment", comment);
        baseShakearoundRequest(json, "device/update");
    }

    /**
     * 删除页面
     */
    public static void delDevicePage(Integer pageId) {
        JSONObject json = new JSONObject();
        json.put("page_id", pageId);
        baseShakearoundRequest(json, "page/delete");
    }

    /**
     * 上传材料文件
     */
    public static String addMaterial(File file) {
        return baseShakearoundRequest(null, file, "/material/add").getString("pic_url");
    }

}
