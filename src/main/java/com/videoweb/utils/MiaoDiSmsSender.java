/*
 * 文件名：MiaoDiSmsSender.java
 * 版权：Copyright by www.taohuakeji.com
 * 描述：
 * 修改人：ying
 * 修改时间：2018-1-3
 */

package com.videoweb.utils;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;

import java.util.Map;

/**
 * 秒滴短信发送
 *
 * @author ying
 * @version 2018-1-3
 * @see MiaoDiSmsSender
 * @since
 */

public class MiaoDiSmsSender {
    private final static Logger logger = LoggerFactory.getLogger(MiaoDiSmsSender.class);
    private static String ACCOUNT_SID;
    private static String AUTH_TOKEN;
    private static String URL;

    static {
        URL = Resources.SMS.getString("miaodi.url");
        ACCOUNT_SID = Resources.SMS.getString("miaodi.accountSid");
        AUTH_TOKEN = Resources.SMS.getString("miaodi.authToken");
    }

    @Async
    public static String smsSenderTo(String tel, String code, Map<String, Object> result) {

        String uid = result.get("uid").toString();
        String pwd = result.get("pwd").toString();
        String template = result.get("template").toString();

        String params = "ac=send&uid=" + uid + "&pwd=" + pwd + "&template=" + template + "&mobile=" + tel + "&content={\"code\":\"" + code + "\"}";
        String res = HttpUtil.get(URL + "?" + params);
        JSONObject json = JSONObject.parseObject(res);
        logger.info("json:" + json);
        if (json.getString("stat").equals("100")) {
            //发送成功
            return json.getString("stat");
        } else {
            logger.info("短信发送失败，失败原因:" + json.getString("respDesc"));
        }
        return null;
    }



}
