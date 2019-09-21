
package com.videoweb.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.InvalidSessionException;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.web.util.WebUtils;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;


public class WebUtil
{
    private static  Logger logger = Logger.getLogger("controller_info");
    
    private WebUtil() {
	}
    /**
     * 
     * Description: 创建新的Token
     *
     * @param userName 登录名称
     * @return
     * @throws Exception 
     * @see
     */
    public static String createNewToken(String userName) throws Exception
    {
        String token="";
        Map<String, Object> payload = new HashMap<String, Object>();
        Date date = new Date();
        payload.put("username", userName);
        payload.put("iat", date.getTime());// 生成时间
        payload.put("ext", date.getTime() + 2000 * 60 * 60);// 过期时间2小时
        token = Jwt.createToken(payload);
        return token;
    }
    
    /**
     * 
     * Description: 将实体以JSON格式传送
     * Implement: <br>
     *
     * @param t  实体
     * @param response  响应
     * @see
     */
    public static void apiObjectToJsonUtil(Object t,HttpServletResponse response)
    {
        response.setContentType("application/json");//设置response的传输格式为json 
        response.setCharacterEncoding("UTF-8");
        ObjectMapper mapper  = new ObjectMapper();
        try { 
            String result ="{\"retCode\":\"-1\",\"retMsg\":\"JSON数据转换异常!\",\"dataMap\":\"null\"}";
            if(t != null && !"".equals(t))
            {
                     
                    result = mapper.writeValueAsString(t);
                    logger.info("api传输数据，出参:"+result);
                   
            }
            PrintWriter out = response.getWriter(); 
            out.write(result);//给页面上传输json对象 
        } catch (IOException e) { 
            logger.error("传输数据转换异常,异常信息:"+e.getMessage());
        }  
        
    }
    
    
    /**
	 * 获取指定Cookie的值
	 * 
	 * @param cookies
	 *            cookie集合
	 * @param cookieName
	 *            cookie名字
	 * @param defaultValue
	 *            缺省值
	 * @return
	 */
	public static final String getCookieValue(HttpServletRequest request, String cookieName, String defaultValue) {
		Cookie cookie = WebUtils.getCookie(request, cookieName);
		if (cookie == null) {
			return defaultValue;
		}
		return cookie.getValue();
	}

	/** 保存当前用户 */
	public static final void saveCurrentUser(Object user) {
		setSession(SysConstants.CURRENT_USER, user);
	}

	/** 保存当前用户 */
	public static final void saveCurrentUser(HttpServletRequest request, Object user) {
		setSession(request, SysConstants.CURRENT_USER, user);
	}

	/** 获取当前用户 */
//	public static final SysLogin getCurrentUser() {
//		Subject currentUser = SecurityUtils.getSubject();
//		if (null != currentUser) {
//			try {
//				Session session = currentUser.getSession();
//				if (null != session) {
//					return (SysLogin) session.getAttribute(SysConstants.CURRENT_USER);
//				}
//			} catch (InvalidSessionException e) {
//				logger.error(e);
//			}
//		}
//		return null;
//	}

	/** 获取当前用户 */
	public static final Object getCurrentUser(HttpServletRequest request) {
		try {
			HttpSession session = request.getSession();
			if (null != session) {
				return session.getAttribute(SysConstants.CURRENT_USER);
			}
		} catch (InvalidSessionException e) {
			logger.error(e);
		}
		return null;
	}

	/**
	 * 将一些数据放到ShiroSession中,以便于其它地方使用
	 * 
	 * @see 比如Controller,使用时直接用HttpSession.getAttribute(key)就可以取到
	 */
	public static final void setSession(Object key, Object value) {
		Subject currentUser = SecurityUtils.getSubject();
		if (null != currentUser) {
			Session session = currentUser.getSession();
			if (null != session) {
				session.setAttribute(key, value);
			}
		}
	}

	/**
	 * 将一些数据放到ShiroSession中,以便于其它地方使用
	 * 
	 * @see 比如Controller,使用时直接用HttpSession.getAttribute(key)就可以取到
	 */
	public static final void setSession(HttpServletRequest request, String key, Object value) {
		HttpSession session = request.getSession();
		if (null != session) {
			session.setAttribute(key, value);
		}
	}

	/** 移除当前用户 */
	public static final void removeCurrentUser(HttpServletRequest request) {
		request.getSession().removeAttribute(SysConstants.CURRENT_USER);
	}

	/**
	 * 获得国际化信息
	 * 
	 * @param key
	 *            键
	 * @param request
	 * @return
	 */
	public static final String getApplicationResource(String key, HttpServletRequest request) {
		ResourceBundle resourceBundle = ResourceBundle.getBundle("ApplicationResources", request.getLocale());
		return resourceBundle.getString(key);
	}

	/**
	 * 获得参数Map
	 * 
	 * @param request
	 * @return
	 */
	public static final Map<String, Object> getParameterMap(HttpServletRequest request) {
		return WebUtils.getParametersStartingWith(request, null);
	}

	@SuppressWarnings("unchecked")
	public static Map<String, Object> getParameter(HttpServletRequest request) {
		String str, wholeStr = "";
		try {
			BufferedReader br = request.getReader();
			while ((str = br.readLine()) != null) {
				wholeStr += str;
			}
			if (StringUtils.isNotBlank(wholeStr)) {
				return JSON.parseObject(wholeStr, Map.class);
			}
		} catch (Exception e) {
			logger.error("", e);
		}
		return getParameterMap(request);
	}

	public static <T> T getParameter(HttpServletRequest request, Class<T> cls) {
		String str, wholeStr = "";
		try {
			BufferedReader br = request.getReader();
			while ((str = br.readLine()) != null) {
				wholeStr += str;
			}
			if (StringUtils.isNotBlank(wholeStr)) {
				return JSON.parseObject(wholeStr, cls);
			}
		} catch (Exception e) {
			logger.error("", e);
		}
		return Request2ModelUtil.covert(cls, request);
	}

	@SuppressWarnings("unchecked")
	public static <T> List<T> getParameters(HttpServletRequest request, Class<T> cls) {
		String str, wholeStr = "";
		try {
			BufferedReader br = request.getReader();
			while ((str = br.readLine()) != null) {
				wholeStr += str;
			}
			if (StringUtils.isNotBlank(wholeStr)) {
				return JSON.parseObject(wholeStr, List.class);
			}
		} catch (Exception e) {
			logger.error("", e);
		}
		return Request2ListUtil.covert(cls, request);
	}

	/** 获取客户端IP */
	public static final String getHost(HttpServletRequest request) {
		String ip = request.getHeader("X-Forwarded-For");
		if (ip != null && ip.indexOf(",") > 0) {
			// 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
			ip = ip.substring(0, ip.indexOf(","));
		}
		if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_CLIENT_IP");
		}
		if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("X-Real-IP");
		}
		if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		if ("127.0.0.1".equals(ip) || "0:0:0:0:0:0:0:1".equals(ip)) {
			InetAddress inet = null;
			try { // 根据网卡取本机配置的IP
				inet = InetAddress.getLocalHost();
			} catch (UnknownHostException e) {
				logger.error(SysConstants.Exception_Head, e);
			}
			ip = inet.getHostAddress();
		}
		logger.debug("getRemoteAddr ip: " + ip);
		return ip;
	}
	
	  
    /**
     * 
     * Description: 验证请求是否为ajax请求
     *
     * @param request
     * @return 
     * @see
     */
    public static boolean isAjax(HttpServletRequest request) {
        boolean ajax = "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
        String ajaxFlag = null == request.getParameter("ajax") ? "false" : request.getParameter("ajax");
        boolean isAjax = ajax || ajaxFlag.equalsIgnoreCase("true");
        return isAjax;
    }
}
