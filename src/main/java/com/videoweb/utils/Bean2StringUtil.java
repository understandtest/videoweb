
package com.videoweb.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Bean2StringUtil {
	private static Logger logger = LogManager.getLogger();
	/**
	 * 实体字段toString
	 * @param obj
	 * @return
	 */
    public static StringBuffer fieldToString( Object obj) {
        StringBuffer sb = new StringBuffer();
        if(obj != null && !"".equals(obj))
        {
            Field [] fields = obj.getClass().getDeclaredFields();
            sb.append(obj.getClass().getName())
            .append("___");
             
            for(Field f:fields){
                f.setAccessible(true);
                try {
                    sb.append(f.getName())
                    .append("=")
                    .append(f.get(obj)).append(";");
                } catch (Exception e) {
                    logger.error("实体类 toString 错误");
                } 
            }  
        }
        return sb;
    }
    /**
     * 方法 toString
     * @param obj
     * @return
     */
    public static StringBuffer methodToString(Object obj){
    	StringBuffer sb = new StringBuffer();
        Method[] methods = obj.getClass().getMethods();
         
        for(Method m:methods){
            sb.append(m.getName()).append(";");
        }
        return sb;
    }
}
