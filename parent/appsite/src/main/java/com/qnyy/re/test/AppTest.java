package com.qnyy.re.test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.qnyy.re.base.enums.CommonErrorResultEnum;
import com.qnyy.re.base.util.HttpClientUtils;
import com.qnyy.re.base.util.exception.BusinessException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by E_Iva on 2017/11/23.
 */
public class AppTest {
    @Test
    public void testSS() throws Exception {
        Map<String, File> map = new HashMap<>();
        File file = new File("C:\\Users\\E_Iva\\Pictures\\Saved Pictures\\BFBadger_ZH-CN8490916760_1920x1080.jpg");
        map.put("file", file);
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("reAmount", "100");
        paramMap.put("reNum", "10");
        paramMap.put("userLng", "113.334158");
        paramMap.put("userLat", "23.149638");
        paramMap.put("reLng", "113.334158");
        paramMap.put("reLat", "23.149638");
        paramMap.put("scope", "1");
        paramMap.put("crowd", "0");
        HttpUriRequest post = HttpClientUtils.getRequestMethod(paramMap, map, "http://localhost:8088/reMoment/createReMoment");
        post.addHeader("token","8e6f64cb65b0135d98c966ba885a3f72");
        CloseableHttpClient client = HttpClientUtils.getHttpClient();
        try (CloseableHttpResponse response = client.execute(post)) {
            String result = EntityUtils.toString(response.getEntity());
            System.out.println("result = " + result);
            if (response.getStatusLine().getStatusCode() == 200) {
            } else {
                throw new BusinessException(CommonErrorResultEnum.REQUEST_TIMEOUT);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new BusinessException(CommonErrorResultEnum.OPTIMISTIC_LOCK_ERROR);
        }
    }
    @Test
    public void testTTT() throws Exception {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("grant_type", "client_credential");
        paramMap.put("appid", "wx2bc35b845e75ef06");
        paramMap.put("secret", "80655d59feaddc59e98def548f143ac5");
        HttpUriRequest post = HttpClientUtils.getRequestMethod(paramMap, "https://api.weixin.qq.com/cgi-bin/token", "get");
        doRequest(post);
    }

    @Test
    public void testShake() throws Exception {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("access_token", "10_JfW59BOiv9uXNmAZWdNJwTGvYc93EXaawVQu5E5wzj1KWb-hPqc48O0GrXmfU2zAylI9mV5kQmc3Mtk3cBNNV7wg_OjnaVAK17mmhYpB2sYk1r_Cn5Ob3vTwjeZLQaopkojTYOm_w_6hWJPNXXVgADAKKI");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("ticket", "b7f849a03b1d4ecf66c694efc1677e78");
        jsonObject.put("need_poi", 1);
        HttpUriRequest post = HttpClientUtils.getRequestMethod(paramMap, jsonObject, "https://api.weixin.qq.com/shakearound/user/getshakeinfo?");
        doRequest(post);
    }
    @Test
    public void testQueryDevice() throws Exception {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("access_token", "10_JfW59BOiv9uXNmAZWdNJwTGvYc93EXaawVQu5E5wzj1KWb-hPqc48O0GrXmfU2zAylI9mV5kQmc3Mtk3cBNNV7wg_OjnaVAK17mmhYpB2sYk1r_Cn5Ob3vTwjeZLQaopkojTYOm_w_6hWJPNXXVgADAKKI");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type", 2);
        jsonObject.put("begin", 0);
        jsonObject.put("count", 3);
        HttpUriRequest post = HttpClientUtils.getRequestMethod(paramMap, jsonObject, "https://api.weixin.qq.com/shakearound/device/search");
        doRequest(post);
    }
    @Test
    public void testQueryPage() throws Exception {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("access_token", "10_JfW59BOiv9uXNmAZWdNJwTGvYc93EXaawVQu5E5wzj1KWb-hPqc48O0GrXmfU2zAylI9mV5kQmc3Mtk3cBNNV7wg_OjnaVAK17mmhYpB2sYk1r_Cn5Ob3vTwjeZLQaopkojTYOm_w_6hWJPNXXVgADAKKI");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type", 2);
        jsonObject.put("begin", 0);
        jsonObject.put("count", 10);
        HttpUriRequest post = HttpClientUtils.getRequestMethod(paramMap, jsonObject, "https://api.weixin.qq.com/shakearound/page/search");
        doRequest(post);
    }
    @Test
    public void testAddPage() throws Exception {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("access_token", "10_JfW59BOiv9uXNmAZWdNJwTGvYc93EXaawVQu5E5wzj1KWb-hPqc48O0GrXmfU2zAylI9mV5kQmc3Mtk3cBNNV7wg_OjnaVAK17mmhYpB2sYk1r_Cn5Ob3vTwjeZLQaopkojTYOm_w_6hWJPNXXVgADAKKI");
        JSONObject jsonObject = new JSONObject();
        JSONObject jsonObject1 = new JSONObject();
        jsonObject1.put("device_id", 19798480);
        jsonObject.put("device_identifier", jsonObject1);
        jsonObject.put("page_ids", new int[]{6640769,6657324});
        System.out.println(jsonObject);
        HttpUriRequest post = HttpClientUtils.getRequestMethod(paramMap, jsonObject, "https://api.weixin.qq.com/shakearound/device/bindpage");
//        doRequest(post);
    }
    @Test
    public void testUpload() throws Exception {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("access_token", "10_gFRkZdMjXea9DS6uLByiPO6EX8_H8HLkyG7Sdpwj93uS5D0kvacJM48iZhfzKTltWv3gZfv1te_X2Ed0t_miMwSOSkreAz7YnJTFNVvyRi7vVI0C8nW8iQgKt83axPn9Y_26cPZ0qx15E39sDQVeAGAWJO");
        Map<String, File> map= new HashMap<>();
        File file = new File("C:\\Users\\E_Iva\\Desktop\\test.png");
        map.put("media", file);
//        JSONObject jsonObject = HttpClientUtils.uploadMedia(file, "10_gFRkZdMjXea9DS6uLByiPO6EX8_H8HLkyG7Sdpwj93uS5D0kvacJM48iZhfzKTltWv3gZfv1te_X2Ed0t_miMwSOSkreAz7YnJTFNVvyRi7vVI0C8nW8iQgKt83axPn9Y_26cPZ0qx15E39sDQVeAGAWJO",
//                "icon");
//        System.out.println(jsonObject);

        HttpUriRequest post = HttpClientUtils.getRequestMethod(paramMap, map, "https://api.weixin.qq.com/shakearound/material/add");
        doRequest(post);
    }

    private void doRequest(HttpUriRequest post){
        CloseableHttpClient client = HttpClientUtils.getHttpClient();
        try (CloseableHttpResponse response = client.execute(post)) {
            String result = EntityUtils.toString(response.getEntity());
            System.out.println(JSON.parse(result));
        } catch (IOException e) {
            e.printStackTrace();
            throw new BusinessException(CommonErrorResultEnum.OPTIMISTIC_LOCK_ERROR);
        }
    }
    @Test
    public void testDGD() throws Exception {
        System.out.println(new Random().nextInt(5) + 5);
    }


}
