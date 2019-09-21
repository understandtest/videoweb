package com.videoweb.utils;


import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import org.apache.commons.codec.Charsets;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class HttpUtil {
    private static final Logger logger = LogManager.getLogger();
    private static final MediaType CONTENT_TYPE_FORM = MediaType.parse("application/x-www-form-urlencoded");
    private static final MediaType CONTENT_TYPE_JSON = MediaType.parse("application/json");
    private static final String DEFAULT_USER_AGENT = "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.146 Safari/537.36";


    private HttpUtil() {
    }

    public static final String httpClientPost(String url) {
        String result = "";
        HttpClient client = new HttpClient();
        GetMethod getMethod = new GetMethod(url);
        try {
            client.executeMethod(getMethod);
            result = getMethod.getResponseBodyAsString();
        } catch (Exception e) {
            logger.error(e);
        } finally {
            getMethod.releaseConnection();
        }
        return result;
    }

    public static final String httpClientPost(String url, ArrayList<NameValuePair> list) {
        String result = "";
        HttpClient client = new HttpClient();
        PostMethod postMethod = new PostMethod(url);
        postMethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "utf-8");
        try {
            NameValuePair[] params = new NameValuePair[list.size()];
            for (int i = 0; i < list.size(); i++) {
                params[i] = list.get(i);
            }

            postMethod.addParameters(params);
            client.executeMethod(postMethod);
            result = postMethod.getResponseBodyAsString();
        } catch (Exception e) {
            logger.error(e);
        } finally {
            postMethod.releaseConnection();
        }
        return result;
    }

    public static String post(String url, String params) {
        RequestBody body = RequestBody.create(CONTENT_TYPE_FORM, params);
        Request request = new Request.Builder().url(url).post(body).build();
        return exec(request);
    }

    /**
     * 发送get请求
     * @param url
     * @return
     */
    public static String get(String url){
        Request request = new Request.Builder().url(url).get().build();
        return exec(request);
    }

    /**
     * 发送post请求，请求参数为json类型
     * @param url
     * @param paramJson
     * @return
     */
    public static String postParamType4Json(String url, String paramJson) {
        RequestBody body = RequestBody.create(CONTENT_TYPE_JSON, paramJson);
        Request request = new Request.Builder().url(url).post(body).build();
        return exec(request);
    }


    public static String HttpPostWithJson(String url, String json) {
        String returnValue = "这是默认返回值，接口调用失败";
        CloseableHttpClient httpClient = HttpClients.createDefault();
        ResponseHandler<String> responseHandler = new BasicResponseHandler();
        try{
            //第一步：创建HttpClient对象
            httpClient = HttpClients.createDefault();

            //第二步：创建httpPost对象
            HttpPost httpPost = new HttpPost(url);

            //第三步：给httpPost设置JSON格式的参数
            StringEntity requestEntity = new StringEntity(json,"utf-8");
            requestEntity.setContentEncoding("UTF-8");
            httpPost.setHeader("Content-type", "application/json");
            httpPost.setEntity(requestEntity);

            //第四步：发送HttpPost请求，获取返回值
            returnValue = httpClient.execute(httpPost,responseHandler); //调接口获取返回值时，必须用此方法

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        //第五步：处理返回值
        return returnValue;
    }



    private static String exec(okhttp3.Request request) {
        try {
            okhttp3.Response response = new OkHttpClient().newCall(request).execute();
            if (!response.isSuccessful())
                throw new RuntimeException("Unexpected code " + response);
            return response.body().string();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String postSSL(String url, String data, String certPath, String certPass) {
        HttpsURLConnection conn = null;
        OutputStream out = null;
        InputStream inputStream = null;
        BufferedReader reader = null;
        try {
            KeyStore clientStore = KeyStore.getInstance("PKCS12");
            clientStore.load(new FileInputStream(certPath), certPass.toCharArray());
            KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            kmf.init(clientStore, certPass.toCharArray());
            KeyManager[] kms = kmf.getKeyManagers();
            SSLContext sslContext = SSLContext.getInstance("TLSv1");

            sslContext.init(kms, null, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
            URL _url = new URL(url);
            conn = (HttpsURLConnection) _url.openConnection();

            conn.setConnectTimeout(25000);
            conn.setReadTimeout(25000);
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setDoInput(true);

            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("User-Agent", DEFAULT_USER_AGENT);
            conn.connect();

            out = conn.getOutputStream();
            out.write(data.getBytes(Charsets.UTF_8));
            out.flush();

            inputStream = conn.getInputStream();
            reader = new BufferedReader(new InputStreamReader(inputStream, Charsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            IOUtils.closeQuietly(out);
            IOUtils.closeQuietly(reader);
            IOUtils.closeQuietly(inputStream);
            if (conn != null) {
                conn.disconnect();
            }
        }
    }
}
