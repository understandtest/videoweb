

package com.videoweb.utils;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.util.Base64;
import net.minidev.json.JSONObject;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Jwt {
    /**
     * 秘钥
     */
    private static final String SECRET = "8752a82e5bca727b7985145b70e126df"; // ying Md5

    /**
     * token过期(token失效了)
     */
    public static final Integer EXPIRED = -1;

    /**
     * 校验失败（token不一致）
     */
    public static final Integer FAIL = 0;

    /**
     * 校验成功
     */
    public static final Integer SUCCESS = 1;

    /**
     * 代码抛异常（校验token时代码出错）
     */
    public static final Integer EXCEPT = 2;

    /**
     * 生成token，该方法只在用户登录成功后调用
     *
     * @param Map集合，主要存储用户id，token生成时间，token过期时间等
     * @return token字符串
     * @throws KeyLengthException
     */
    public static String createToken(Map<String, Object> playLoad) {
        ///B
        JSONObject userInfo = new JSONObject(playLoad);
        Payload payload = new Payload(userInfo);

        ///A
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS256);
        // 创建一个 JWS object
        JWSObject jwsObject = new JWSObject(header, payload);

        try {
            //创建 HMAC signer
            JWSSigner signer = new MACSigner(SECRET.getBytes());
            jwsObject.sign(signer);
        } catch (JOSEException e) {
            System.err.println("签名失败" + e.getMessage());
            e.printStackTrace();
        }
        return jwsObject.serialize();
    }


    /**
     * 校验token是否合法，返回Map集合,集合中主要包含  isSuccess是否成功  status状态码   data鉴权成功后从token中提取的数据
     * 该方法在过滤器中调用，每次请求API时都校验
     *
     * @param token
     * @return
     * @throws KeyLengthException
     */
    public static Map<String, Object> validToken(String token) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        JWSObject jwsObject = null;
        Payload payload = null;
        try {
            String[] tokenArr = token.split("\\.");
            payload = new Payload(new Base64(tokenArr[1]).decodeToString());
            ///A
            JWSHeader header = new JWSHeader(JWSAlgorithm.HS256);
            // 创建一个 JWS object
            jwsObject = new JWSObject(header, payload);

            //创建 HMAC signer
            JWSSigner signer = new MACSigner(SECRET.getBytes());
            jwsObject.sign(signer);
        } catch (Exception e) {
            e.printStackTrace();
            //异常
            resultMap.put("isSuccess", false);
            resultMap.put("status", EXCEPT);
            return resultMap;
        }


        if (jwsObject.serialize().equals(token)) {
            JSONObject jsonOBj = payload.toJSONObject();
            long extTime = (long) jsonOBj.get("ext");
            long curTime = new Date().getTime();
            if (curTime > extTime) {
                //过期了
                resultMap.put("isSuccess", false);
                resultMap.put("status", EXPIRED);
                System.out.println("token过期");
                return resultMap;
            } else {
                //没有过期
                resultMap.put("isSuccess", true);
                resultMap.put("status", SUCCESS);
                resultMap.put("data", payload.toJSONObject());
                return resultMap;
            }
        } else {
            //校验失败
            resultMap.put("isSuccess", false);
            resultMap.put("status", FAIL);
        }

        return resultMap;
    }

    //测试
    public static void main(String[] args) throws ParseException {

        String token = "";
        //正常生成token----------------------------------------------------------------------------------------------------
        // String token="eyJhbGciOiJIUzI1NiJ9.eyJleHQiOjE1Mzc0Njk3OTIxMTUsInVpZCI6MiwiaWF0IjoxNTM5MDk3NjYxMjk5fQ.IrZ61peTLIYo-xzAago-PP97wFc7Z2Xx2MuAM2A60qw";
        Map<String, Object> payload = new HashMap<String, Object>();
        Date date = new Date();
        payload.put("uid", "815");// 用户id
        payload.put("iat", date.getTime());// 生成时间
        payload.put("ext", date.getTime() + 1000 * 60 * 60 * 5);// 过期时间2小时
        try {
            token = Jwt.createToken(payload);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("新生成的token是：" + token);
        System.out.println("将新生成的token马上进行校验");
        Map<String, Object> resultMap = Jwt.validToken(token);
        System.out.println("校验结果是:" + resultMap.get("isSuccess"));
        JSONObject obj = (JSONObject) resultMap.get("data");
        System.out.println("从token中取出的数据是：" + obj.toJSONString());


//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


//        //校验过期----------------------------------------------------------------------------------------------------
//        String token=null;
//        Map<String, Object> payload = new HashMap<String, Object>();
//        Date date = new Date();
//        payload.put("uid", "291969452");// 用户id
//        payload.put("iat", format.parse("2019-07-27 17:40:47").getTime());// 生成时间
//        payload.put("ext", format.parse("2019-07-27 17:52:47").getTime());// 过期时间就是当前
//        try {
//            token = Jwt.createToken(payload);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        System.out.println("新生成的token是："+token);
//        System.out.println("将新生成的token马上进行校验");
//        Map<String,Object> resultMap= Jwt.validToken("eyJhbGciOiJIUzI1NiJ9.eyJleHQiOjE1Njc0MzI2NDI3NjYsInVpZCI6NTQ3LCJpYXQiOjE1Njc0MTQ2NDI3NjZ9.AY1pGP9De7lrLib-lvwY1YVd51Neq5j_Fso6CxKl9MQ");
//        System.out.println(resultMap);
//        System.out.println("校验结果是:"+resultMap.get("isSuccess"));
//        System.out.println("false的原因是（-1标识过期）:"+resultMap.get("status"));

    }
}
