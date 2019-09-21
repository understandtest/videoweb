package com.videoweb.utils;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;

/**
 * @Author lbh
 * @Date 2019/8/10
 */
public class HttpUtils2 {


    /**
     * * 发送Post请求绕过SSL安全证书验证
     * * @param url
     * * @param param
     * * @return
     */
    public static String sendPost(String url, String param) {
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            SslUtils.ignoreSsl();
            HttpsURLConnection conn = (HttpsURLConnection) realUrl.openConnection();
            SSLSocketFactory ssf = BZX509TrustManager.getSSFactory();
            //设置安全认证
            conn.setSSLSocketFactory(ssf);
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0;Windows NT 5.1;SV1)");
            conn.setRequestMethod("POST");
            //发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 设置通用的请求属性
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream());
            out.write(param);
            out.flush();
            out.close();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！：" + e);
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }
}
