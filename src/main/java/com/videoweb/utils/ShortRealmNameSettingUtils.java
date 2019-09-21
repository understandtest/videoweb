package com.videoweb.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.videoweb.ying.po.ShortRealmNameSetting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author lbh
 * Date 2019-07-24
 */
public class ShortRealmNameSettingUtils {

    private final static Logger logger = LoggerFactory.getLogger(ShortRealmNameSettingUtils.class);

    private static String URL;


    static {
        URL = Resources.SMS.getString("short.realmServerUrl");
    }


    /**
     * 获取短域名工具方法
     * @param url
     * @return
     */
    public static String getShortRealmNameSetting(String url, ShortRealmNameSetting shortRealmNameSetting){

        try {

            logger.info("准备开始获取短域名");
            //请求url添加参数
            String key = shortRealmNameSetting.getKey();
            String secret = shortRealmNameSetting.getSecret();
            String actionUrl = URL + "?apikey=" + key + "&apisecret=" + secret ;

            logger.info("获取短域名url为:{}",actionUrl);

            //构建短域名json参数
            String paramJson = getParamJson(url,shortRealmNameSetting.getShortRealmNameDesc());

            logger.info("获取短域名json参数为:{}",paramJson);

//            String responseJson = HttpUtil.postParamType4Json(actionUrl, paramJson);
//            String responseJson = HttpUtil.postParamType4Json(actionUrl, paramJson);
//            String responseJson = HttpUtil.postParamType4Json(actionUrl, paramJson);
//            String responseJson = HttpUtil.postSSL(actionUrl, paramJson,"","");
            String responseJson = HttpUtils2.sendPost(actionUrl, paramJson);

            logger.info("获取短域名响应体数据{}",responseJson);

            //开启解析数据
            Map<String,Object> responseMap = JSON.parseObject(responseJson,Map.class); //json数据转换为Map

            //获取result
            Integer result = (Integer) responseMap.get("result");

            if(result == 0){ //如果为0则成功

                //解析短域名
                Map<String,Object> dataMap = (Map<String, Object>) responseMap.get("data");

                //获取短域名
                String shortUrl = (String) dataMap.get("shorturl");

                logger.info("获取短域名成功，短域名为:{}",shortUrl);

                return shortUrl;

            }
        }catch (Exception e){
            e.printStackTrace();
            logger.info("获取短连接出错,错误信息" + e.getMessage());
        }

        return null;

    }

    /**
     * 构建短域名需要的json数据
     * @param url
     * @param desc
     * @return
     */
    private static String getParamJson(String url, String desc) {

        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("link",url);
        paramMap.put("info",desc);

        //转换为json并返回
        return JSON.toJSONString(paramMap);
    }



    public static void main(String[] args){

        ShortRealmNameSetting shortRealmNameSetting = new ShortRealmNameSetting();
        shortRealmNameSetting.setKey("798E66-88DA-88C");
        shortRealmNameSetting.setSecret("9225d74ebba783c9060ed5d340e3dd17");
        shortRealmNameSetting.setShortRealmNameDesc("气球视频");
        String url = getShortRealmNameSetting("https://qqsp.app/?code=DHDZJE",shortRealmNameSetting);
        System.out.println(url);


    }

}
