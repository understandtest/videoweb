package com.videoweb.utils;

import java.util.ArrayList;

import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;

import com.alibaba.fastjson.JSONObject;

public class SmsSender
{
    private final static Logger logger = LoggerFactory.getLogger(SmsSender.class);
    
    private static String apikey=""; //用户唯一标识
    private static String url="https://sms.yunpian.com/v2/sms/single_send.json"; //单条请求路径
    /**
     * 
     * Description: 单条发送
     * Implement: <br>
     *
     * @param tel
     * @param text
     * @return 
     * @see
     */
    @Async
    public static String smsSenderTo(String tel, String code)
    {
        if(StringUtils.isNotEmpty(apikey)&&StringUtils.isNotEmpty(url)&&StringUtils.isNotEmpty(tel))
        {
            ArrayList<NameValuePair> list = new ArrayList<NameValuePair>();
            //apiKey
            NameValuePair params1 = new NameValuePair();
            params1.setName("apikey");
            params1.setValue(apikey);
            list.add(params1);
            //tel
            NameValuePair params2 = new NameValuePair();
            params2.setName("mobile");
            params2.setValue(tel);
            list.add(params2);
            //text
            NameValuePair params3 = new NameValuePair();
            String text = "【视频在线】您的验证码是"+code;
            params3.setName("text");
            params3.setValue(text);  
            list.add(params3);
            String res = HttpUtil.httpClientPost(url,list);
            JSONObject json = JSONObject.parseObject(res);
            logger.info("json:"+json);
            //获取返回值
            if (json.getInteger("code")==0 ) {
                //发送成功
                return json.getString("msg");
            }
            else
            {
                logger.info("短信消息发送失败，手机号："+tel+",消息内容："+text);
                //throw new IllegalArgumentException("短信消息发送失败，异常信息："+json.getString("msg"));
            }
        }
        return null;
    }
    
    
  

    
    public static void main(String[] args)
    {
       String tel="";
       String text="【视频在线】您的验证码是12345";
       System.out.println(smsSenderTo(tel,text));
      
    }
}
