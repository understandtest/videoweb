
package com.videoweb.interceptor;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bouncycastle.cert.ocsp.Req;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.videoweb.utils.HttpCode;
import com.videoweb.utils.InstanceUtil;
import com.videoweb.utils.Jwt;
import sun.misc.Request;

public class TokenInterceptor implements HandlerInterceptor {

    @Override
    public void afterCompletion(HttpServletRequest arg0,
                                HttpServletResponse arg1, Object arg2, Exception arg3)
            throws Exception {
        // TODO Auto-generated method stub

    }


    @Override
    public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1,
                           Object arg2, ModelAndView arg3) throws Exception {
        // TODO Auto-generated method stub

    }


    @Override
    public boolean preHandle(HttpServletRequest arg0, HttpServletResponse arg1,
                             Object arg2) throws Exception {
        System.out.println(arg0.getRequestURI());
        arg1.setCharacterEncoding("utf-8");
        String token = arg0.getHeader("token");
        // System.out.print("-----------1--------");
        if (token != null && !"".equals(token)) {
            //System.out.print("-----------2--------");
            Map<String, Object> resultMap = new HashMap<String, Object>();
            resultMap = Jwt.validToken(token);
            if (resultMap != null && !"".equals(resultMap)) {
                boolean flag = (boolean) resultMap.get("isSuccess");
                if (flag) {
                    //System.out.print("-----------3--------");
                    return true;
                } else {
                    arg1.setContentType("text/html; charset=UTF-8");
                    Map<String, Object> modelMap = InstanceUtil.newLinkedHashMap();
                    modelMap.put("httpCode", HttpCode.TIMEOUTTOKEN.value());
                    modelMap.put("msg", HttpCode.TIMEOUTTOKEN.msg());
                    modelMap.put("timestamp", System.currentTimeMillis());
                    PrintWriter out = arg1.getWriter();
                    out.println(JSON.toJSONString(modelMap));
                    out.flush();
                    out.close();
                    return false;
                }
            }

        }
        arg1.setContentType("text/html; charset=UTF-8");
        Map<String, Object> modelMap = InstanceUtil.newLinkedHashMap();
        modelMap.put("httpCode", HttpCode.LOSTTOKEN.value());
        modelMap.put("msg", HttpCode.LOSTTOKEN.msg());
        modelMap.put("timestamp", System.currentTimeMillis());
        PrintWriter out = arg1.getWriter();
        out.println(JSON.toJSONString(modelMap));
        out.flush();
        out.close();
        return false;
    }

}
