package com.qnyy.re.base.enums;

import com.qnyy.re.base.util.HttpClientUtils;
import lombok.AllArgsConstructor;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.concurrent.FutureCallback;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lianghaifeng
 * @version Id: DataServerEnum.java, v 0.1 2019.5.31 031 14:32 lianghaifeng Exp $$
 */
@AllArgsConstructor
public enum DataAsyncServerEnum implements DataAsyncServer {
    /**
     *
     */
    SERVER {
        @Override
        public void asyncOperate(MethodUrlEnum methodUrl, String key, String value, String type, boolean replace) {
            String serverUrl = "http://39.108.178.8/m-wap/MapRedPacket/PostRedirect";
            Map<String, String> map = new HashMap<>(5);
            map.put("url", methodUrl.getUrl());
            map.put("key", replaceString(key));
            if (value != null) {
                map.put("value", replaceString(value));
            }
            map.put("replace", replace ? "1" : "0");
            map.put("type", type);
            doRequest(serverUrl, map);
        }
    },
    SLAVE_SERVER {
        @Override
        public void asyncOperate(MethodUrlEnum methodUrl, String key, String value, String type, boolean replace) {
            String serverUrl = "http://218.205.113.10/data";
            Map<String, String> map = new HashMap<>(5);
            map.put("key", key);
            map.put("value", value);
            map.put("type", type);
            map.put("replace", replace ? "1" : "0");
            doRequest(serverUrl, map);
        }
    };

    private static void doRequest(String serverUrl, Map<String, String> map) {
        HttpUriRequest request = HttpClientUtils.getRequestMethod(map, serverUrl, "post");
        HttpClientUtils.sendAsync(request, new FutureCallback<HttpResponse>() {
            @Override
            public void completed(HttpResponse result) {
            }

            @Override
            public void failed(Exception ex) {

            }

            @Override
            public void cancelled() {

            }
        });
    }

    private static String replaceString(String string) {
        return string.replaceAll("\\[", "%5B").replaceAll("]", "%5D");
    }
}
