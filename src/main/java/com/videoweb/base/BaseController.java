
package com.videoweb.base;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.minidev.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.baomidou.mybatisplus.plugins.Page;
import com.videoweb.exception.BaseException;
import com.videoweb.exception.IllegalParameterException;
import com.videoweb.utils.DoubleUtil;
import com.videoweb.utils.HttpCode;
import com.videoweb.utils.InstanceUtil;
import com.videoweb.utils.Jwt;
import com.videoweb.utils.SysConstants;
import com.videoweb.utils.WebUtil;
import com.videoweb.utils.redis.CacheUtil;

public class BaseController
{
    protected final Logger logger = LogManager.getLogger(this.getClass());
    
    /** 获取当前用户Id */
    protected Integer getCurrUser(HttpServletRequest request) {
    	Integer memberId =null;
    	String token = request.getHeader("token");
    	if(token != null && !"".equals(token))
    	{
    		Map<String,Object> resultMap = new HashMap<String,Object>();
    		resultMap= Jwt.validToken(token);
    		if(resultMap.get("data") != null && !"".equals(resultMap.get("data")))
    		{
    			JSONObject obj= (JSONObject) resultMap.get("data");
    			if(obj.get("uid") != null && !"".equals(obj.get("uid")))
    			{
    				memberId = Integer.valueOf(obj.get("uid").toString());
    			}
    		}
    	}
        return memberId;
    }
    
    /** 设置成功响应代码 */
    protected ModelMap setSuccessModelMap(ModelMap modelMap) {
        return setSuccessModelMap(modelMap, null);
    }

    protected ResponseEntity<ModelMap> setSuccessResponse(ModelMap modelMap) {
        return setSuccessResponse(modelMap, null);
    }
    
    /** 设置成功响应代码 */
    protected ModelMap setSuccessModelMap(ModelMap modelMap, Object data) {
        return setModelMap(modelMap, HttpCode.OK, data);
    }
    
   protected ResponseEntity<ModelMap> setSuccessResponse(ModelMap modelMap, Object data) {
        return setSuccessResponse(modelMap, HttpCode.OK, data);
    }

    /** 设置响应代码 */
    protected ModelMap setModelMap(ModelMap modelMap, HttpCode code) {
        return setModelMap(modelMap, code, null);
    }
    
    protected ResponseEntity<ModelMap> setResponse(ModelMap modelMap, HttpCode code) {
        return setSuccessResponse(modelMap, code, null);
    }

    /** 设置响应代码 */
    protected ModelMap setModelMap(ModelMap modelMap, HttpCode code, Object data) {
        Map<String, Object> map = InstanceUtil.newLinkedHashMap();
        map.putAll(modelMap);
        modelMap.clear();
        for (Iterator<String> iterator = map.keySet().iterator(); iterator.hasNext();) {
            String key = iterator.next();
            if (!key.startsWith("org.springframework.validation.BindingResult") && !key.equals("void")) {
                modelMap.put(key, map.get(key));
            }
        }
        if (data != null) {
            if (data instanceof Page) {
                Page<?> page = (Page<?>) data;
                modelMap.put("data", page.getRecords());
                modelMap.put("current", page.getCurrent());
                modelMap.put("size", page.getSize());
                modelMap.put("pages", page.getPages());
                modelMap.put("total", page.getTotal());
                modelMap.put("iTotalRecords", page.getTotal());
                modelMap.put("iTotalDisplayRecords", page.getTotal());
            } else if (data instanceof List<?>) {
                modelMap.put("data", data);
                modelMap.put("iTotalRecords", ((List<?>) data).size());
                modelMap.put("iTotalDisplayRecords", ((List<?>) data).size());
            } else {
                modelMap.put("data", data);
            }
        }
        modelMap.put("httpCode", code.value());
        modelMap.put("msg", code.msg());
        modelMap.put("timestamp", System.currentTimeMillis());
        return modelMap;
    }

    protected ResponseEntity<ModelMap> setSuccessResponse(ModelMap modelMap, HttpCode code, Object data) {
        Map<String, Object> map = InstanceUtil.newLinkedHashMap();
        map.putAll(modelMap);
        modelMap.clear();
        for (Iterator<String> iterator = map.keySet().iterator(); iterator.hasNext();) {
            String key = iterator.next();
            if (!key.startsWith("org.springframework.validation.BindingResult") && !key.equals("void")) {
                modelMap.put(key, map.get(key));
            }
        }
        if (data != null) {
            if (data instanceof Page) {
                Page<?> page = (Page<?>) data;
                modelMap.put("data", page.getRecords());
                modelMap.put("current", page.getCurrent());
                modelMap.put("size", page.getSize());
                modelMap.put("pages", page.getPages());
                modelMap.put("total", page.getTotal());
                modelMap.put("iTotalRecords", page.getTotal());
                modelMap.put("iTotalDisplayRecords", page.getTotal());
            } else if (data instanceof List<?>) {
                modelMap.put("data", data);
                modelMap.put("iTotalRecords", ((List<?>) data).size());
                modelMap.put("iTotalDisplayRecords", ((List<?>) data).size());
            } else {
                modelMap.put("data", data);
            }
        }
        modelMap.put("httpCode", code.value());
        modelMap.put("msg", code.msg());
        modelMap.put("timestamp", System.currentTimeMillis());
        return ResponseEntity.ok(modelMap);
    }
    
    /** 异常处理 */
    @ExceptionHandler(Exception.class)
    public void exceptionHandler(HttpServletRequest request, HttpServletResponse response, Exception ex)
            throws Exception {
        logger.error(SysConstants.Exception_Head, ex);
        ModelMap modelMap = new ModelMap();
        //当是api请求时
        if(request.getHeader("token") != null && !"".equals(request.getHeader("token")))
        {
            modelMap.put("httpCode", HttpCode.INTERNAL_SERVER_ERROR.value());
            modelMap.put("msg", "服务异常!");
        }
        else
        {
            //当不是api请求时
            if (ex instanceof BaseException) {
                ((BaseException) ex).handler(modelMap);
            } else if (ex instanceof IllegalArgumentException) {
                if(WebUtil.isAjax(request))
                {
                    //ajax情况请求参数
                    modelMap.put("httpCode", HttpCode.BAD_REQUEST.value());
                    modelMap.put("msg", "您没有操作的权限！");
                }
                else
                {
                    new IllegalParameterException(ex.getMessage()).handler(modelMap);
                }
                
            } else if (ex instanceof UnauthorizedException) {
                if(WebUtil.isAjax(request)){
                    //ajax情况 判断后  返回json数据  TODO 异常信息处理
                    modelMap.put("httpCode", HttpCode.FORBIDDEN.value());
                    modelMap.put("msg", "您没有操作的权限！");
                }else{
                    //没有权限跳转的页面
                    response.sendRedirect("/admin/forbidden");
                }
            }else{
                //如果是ajax请求
                if(WebUtil.isAjax(request))
                {
                    modelMap.put("httpCode", HttpCode.INTERNAL_SERVER_ERROR.value());
                    String msg = StringUtils.defaultIfBlank(ex.getMessage(), HttpCode.INTERNAL_SERVER_ERROR.msg());
                    modelMap.put("msg", msg.length() > 100 ? "系统走神了,请稍候再试." : msg);
                }
                else
                {
                  //没有权限跳转的页面
                  response.sendRedirect("/admin/500");
                }
            }
        }
        response.setContentType("application/json;charset=UTF-8");
        modelMap.put("timestamp", System.currentTimeMillis());
        logger.info(JSON.toJSON(modelMap));
        byte[] bytes = JSON.toJSONBytes(modelMap, SerializerFeature.DisableCircularReferenceDetect);
        response.getOutputStream().write(bytes);
    }
    
    public void getReportCache(int type,String fromCode,String price)
    {
    	SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
    	String date = format.format(new Date());
    	String key="tongji-"+type+"-"+date+"-"+fromCode;
    	List<String> fromCodeList = new ArrayList<String>();
    	if(CacheUtil.getCache().get("fromCode") !=null && !"".equals(CacheUtil.getCache().get("fromCode")))
    	{
    		fromCodeList = (List<String>) CacheUtil.getCache().get("fromCode");
    		if(!fromCodeList.contains(fromCode))
    		{
    			fromCodeList.add(fromCode);
    			CacheUtil.getCache().set("fromCode", (Serializable) fromCodeList);
    		}
    	}
    	else
    	{
    		fromCodeList.add(fromCode);
    		CacheUtil.getCache().set("fromCode", (Serializable) fromCodeList);
    	}
    	
    	if(CacheUtil.getLock("LOCK_"+key))
    	{
    		logger.info("获取的key值为:"+key);
    		String value="";
    		if(CacheUtil.getCache().get(key)!= null && !"".equals(CacheUtil.getCache().get(key)))
    		{
    			//当是充值状态的情况下
    			if(type == 3)
    			{
    				String value1 = (String) CacheUtil.getCache().get(key);
    				logger.info("获取的key为:"+key+"   内容为:"+value1);
    				String[] vl = value1.split(",");
    				Integer num = Integer.valueOf(vl[0]);
    				num++;
    				Double p = Double.valueOf(vl[1]);
    				Double totalPrice = DoubleUtil.addForRoundHalfUp(p, Double.valueOf(price),2);
    				value = num+","+totalPrice;
    			}
    			else
    			{
    				String value1 = (String) CacheUtil.getCache().get(key);
    				logger.info("获取的key为:"+key+"   内容为:"+value1);
    				Integer num = Integer.valueOf(value1);
    				num++;
    				value = String.valueOf(num);
    			}
    		}
    		else
    		{
    			if(type == 3)
    			{
    				value = "1,"+price;
    			}
    			else
    			{
    				Integer num = 1;
    				value = String.valueOf(num);
    			}
    		}
    		CacheUtil.getCache().set(key, value);
    		CacheUtil.unlock("LOCK_"+key);
    	}
    }
    
}
