

package com.videoweb.utils;

import java.util.Map;

public class SysConstants
{
	public static final String Exception_Head = "抱歉！系统发生了异常，请联系管理员:";
	
    public static String AUTHORIZATION ="Token"; //客户端定义标识头部的Token
    
    public static String MD5_SALT="ying"; //MD5加盐
    
    public static String USER_ACTIVITY_INFO="USER_ACTIVITY";  //Session标识
    
	/** 当前用户 */
	public static final String CURRENT_USER = "CURRENT_USER";
	
	/** 缓存命名空间 */
	public static final String CACHE_NAMESPACE = "Ying:";
	
	/** 缓存键值 */
	public static final Map<Class<?>, String> cacheKeyMap = InstanceUtil.newHashMap();
	
	/** 验证码 */
    public static final String VERICODE_KEY =  "VERICODE_KEY";

    
    
    /**
    * 菜单类型
    */
   public enum MenuType {
       /**
        * 目录
        */
       CATALOG(0),
       /**
        * 菜单
        */
       MENU(1),
       /**
        * 按钮
        */
       BUTTON(2);

       private int value;

       private MenuType(int value) {
           this.value = value;
       }

       public int getValue() {
           return value;
       }
   }
   
   /**
    * 定时任务状态
    * 
    */
   public enum ScheduleStatus {
       /**
        * 正常
        */
       NORMAL(0),
       /**
        * 暂停
        */
       PAUSE(1);

       private int value;

       private ScheduleStatus(int value) {
           this.value = value;
       }
       
       public int getValue() {
           return value;
       }
   }
}
