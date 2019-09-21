
package com.videoweb.utils;

import java.security.MessageDigest;

import org.apache.shiro.crypto.hash.Md5Hash;


public class MD5Util
{
    public static String md5(String str,String salt){
        return new Md5Hash(str,salt).toString() ;
    }
    
    public final static String md5String(String s) {  
        char hexDigits[] = { '0', '1', '2', '3', '4',  
                             '5', '6', '7', '8', '9',  
                             'a', 'b', 'c', 'd', 'e', 'f' };  
        try {  
            byte[] btInput = s.getBytes();  
            //获得MD5摘要算法的 MessageDigest 对象  
            MessageDigest mdInst = MessageDigest.getInstance("MD5");  
            //使用指定的字节更新摘要  
            mdInst.update(btInput);  
            //获得密文  
            byte[] md = mdInst.digest();  
            //把密文转换成十六进制的字符串形式  
            int j = md.length;  
            char str[] = new char[j * 2];  
            int k = 0;  
            for (int i = 0; i < j; i++) {  
                byte byte0 = md[i];  
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];  
                str[k++] = hexDigits[byte0 & 0xf];  
            }  
            return new String(str);  
        }  
        catch (Exception e) {  
            e.printStackTrace();  
            return null;  
        }  
    } 
    
    public static void main(String[] args) {
    	System.out.println(md5("abc123", SysConstants.MD5_SALT));
    	//System.out.print(md5String("ying"));
	}
}
