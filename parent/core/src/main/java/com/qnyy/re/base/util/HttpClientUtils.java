package com.qnyy.re.base.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.collections.MapUtils;
import org.apache.http.Consts;
import org.apache.http.HttpHost;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by E_Iva on 2018.1.30.0030.
 */
public class HttpClientUtils {

    private static PoolingHttpClientConnectionManager connectionManager = null;
    private static HttpClientBuilder httpBuilder = null;
    private static RequestConfig requestConfig = null;


    private static final int MAX_CONNECTION = 100;

    private static final int DEFAULT_MAX_CONNECTION = 5;

    private static String IP = "cnivi.com.cn";
    private static int PORT = 80;

    static {
        //设置http的状态参数
        requestConfig = RequestConfig.custom()
                .setSocketTimeout(10000)
                .setConnectTimeout(10000)
                .setConnectionRequestTimeout(5000)
                .build();

        HttpHost target = new HttpHost(IP, PORT);
        connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setMaxTotal(MAX_CONNECTION);//客户端总并行链接最大数
        connectionManager.setDefaultMaxPerRoute(DEFAULT_MAX_CONNECTION);//每个主机的最大并行链接数
//        connectionManager.setMaxPerRoute(new HttpRoute(target), 20);
        httpBuilder = HttpClients.custom();
        httpBuilder.setConnectionManager(connectionManager);
    }

    public static CloseableHttpClient getHttpClient() {
        return httpBuilder.build();
    }

    /**
     * 通用键值对请求方式
     * @param paramMap
     * @param url
     * @param method
     * @return
     */
    public static HttpUriRequest getRequestMethod(Map<String, String> paramMap, String url, String method) {
        return getRequestMethod(paramMap, null,null, url, method);
    }

    /**
     * json请求方式
     * @param paramMap
     * @param jsonParams
     * @param url
     * @return
     */
    public static HttpUriRequest getRequestMethod(Map<String, String> paramMap, JSONObject jsonParams, String url) {
        return getRequestMethod(paramMap, jsonParams,null, url, "post");
    }

    public static HttpUriRequest getRequestMethod(Map<String, String> paramMap, Map<String, File> binaryParam, String url) {
        return getRequestMethod(paramMap, null,binaryParam, url,"post");
    }

    private static HttpUriRequest getRequestMethod(Map<String, String> paramMap, JSONObject jsonParams, Map<String, File> binaryParam, String url, String method) {
        List<NameValuePair> params = parseNameValuePair(paramMap);
        HttpUriRequest reqMethod = null;
        if ("post".equalsIgnoreCase(method)) {
            RequestBuilder requestBuilder = RequestBuilder.post().setUri(url)
                    .addParameters(params.toArray(new BasicNameValuePair[params.size()]))
                    .setConfig(requestConfig);
            if (jsonParams != null) {
                StringEntity entity = new StringEntity(jsonParams.toJSONString(), Consts.UTF_8);
                entity.setContentType("application/json");
                entity.setContentEncoding("UTF-8");
                requestBuilder.setEntity(entity);
            } else if (MapUtils.isNotEmpty(binaryParam)) {
                //创建二进制实体
                MultipartEntityBuilder builder = MultipartEntityBuilder.create();
                binaryParam.forEach(builder::addBinaryBody);
                requestBuilder.setEntity(builder.build());
            }
            reqMethod = requestBuilder.build();
        } else if ("get".equalsIgnoreCase(method)) {
            reqMethod = RequestBuilder.get().setUri(url)
                    .addParameters(params.toArray(new BasicNameValuePair[params.size()]))
                    .setConfig(requestConfig).build();
        }
        return reqMethod;
    }


    public static List<NameValuePair> parseNameValuePair(Map<String, String> map) {
        if (map == null) {
            return new ArrayList<>();
        }
        return map.entrySet().stream().map(entry -> new BasicNameValuePair(entry.getKey(), entry.getValue())).collect(Collectors.toList());
    }

    /**
     * 拼接url
     *
     * @param api
     * @param action
     */
    public static String jointUrl(String api, String action) {
        if (api == null || action == null) {
            return "";
        }
        String apiTrim = api.trim();
        String actionTrim = action.trim();
        Pattern pattern = Pattern.compile("^([hH][tT]{2}[pP]://|[hH][tT]{2}[pP][sS]://|[fF][tT][pP]://).*");
        if (!pattern.matcher(actionTrim).matches()) {
            apiTrim = "http://" + apiTrim;
        }
        boolean apiEnd = apiTrim.endsWith("/");
        boolean actionEnd = actionTrim.startsWith("/");
        String url;
        if (apiEnd) {//如果api存在斜杆
            if (actionEnd) {//如果action也存在斜杆
                //去掉action中的斜杆
                url = api + actionTrim.substring(1);
            } else {
                url = api + actionTrim;
            }
        } else {
            if (actionEnd) {//如果action存在斜杆
                //去掉action中的斜杆
                url = api + actionTrim;
            } else {
                url = api + "/" + actionTrim;
            }
        }
        return url;
    }
}
