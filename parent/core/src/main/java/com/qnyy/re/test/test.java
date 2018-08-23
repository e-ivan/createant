package com.qnyy.re.test;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class test {

    public static void main(String[] args) throws Exception {

        // HostnameVerifier hnv = new HostnameVerifier() {
        //     public boolean verify(String hostname, SSLSession session) {
        //         // Always return true，接受任意域名服务器
        //         return true;
        //     }
        // };
        // HttpsURLConnection.setDefaultHostnameVerifier(hnv);

        String UTF8 = "UTF-8";
        String reqBody = "<xml>\n" +
                "<appid>wxfbde3ae5ed267764</appid>\n" +
                "<body>鸿运当头-发红包</body>\n" +
                "<mch_id>1493807632</mch_id>\n" +
                "<nonce_str>f7c8afc556e4495a9c504c7e01cdf3b2</nonce_str>\n" +
                "<notify_url>http://rpapi.yuewo365.com/pay/wechatPayCallBack</notify_url>\n" +
                "<out_trade_no>RM151557470988901976T1515574711</out_trade_no>\n" +
                "<sign>53F0B9F0819D6F7F6B0BB97439BE280CAD7D87DD14E2D1D9447BF2C8A896874C</sign>\n" +
                "<sign_type>HMAC-SHA256</sign_type>\n" +
                "<spbill_create_ip>192.168.1.35</spbill_create_ip>\n" +
                "<total_fee>1</total_fee>\n" +
                "<trade_type>APP</trade_type></xml>";
        URL httpUrl = new URL("http://192.168.1.150:8088/pay/testNotify");
        HttpURLConnection httpURLConnection = (HttpURLConnection) httpUrl.openConnection();
        httpURLConnection.setRequestProperty("Host", "api.mch.weixin.qq.com");
        httpURLConnection.setDoOutput(true);
        httpURLConnection.setRequestMethod("POST");
        httpURLConnection.setConnectTimeout(10*1000);
        httpURLConnection.setReadTimeout(10*1000);
        httpURLConnection.connect();
        OutputStream outputStream = httpURLConnection.getOutputStream();
        outputStream.write(reqBody.getBytes(UTF8));

        //获取内容
        InputStream inputStream = httpURLConnection.getInputStream();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, UTF8));
        final StringBuffer stringBuffer = new StringBuffer();
        String line = null;
        while ((line = bufferedReader.readLine()) != null) {
            stringBuffer.append(line);
        }
        String resp = stringBuffer.toString();
        if (stringBuffer!=null) {
            try {
                bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (inputStream!=null) {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (outputStream!=null) {
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println(resp);

    }

}
