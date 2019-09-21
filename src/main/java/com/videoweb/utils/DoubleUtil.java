package com.videoweb.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class DoubleUtil {
	/**
	 * 
	 * @Method: addForRoundHalfUp 
	 * @Description: 两个小数相加，四舍五入
	 * @param d1 
	 * @param d2
	 * @param scale 小数位数
	 * @return double
	 * @throws
	 */
	public static double addForRoundHalfUp(Double d1,Double d2,int scale){
		return (new BigDecimal(d1+d2).setScale(scale, BigDecimal.ROUND_HALF_UP)).doubleValue();
	}
	/**
	 * 
	 * @Method: addForRoundHalfUp 
	 * @Description: 多个小数相加，四舍五入
	 * @param d1 小数数组
	 * @param scale 小数位数
	 * @return double
	 * @throws
	 */
	public static double addForRoundHalfUp(Double[] d1,int scale){
		double db = 0;
		for (Double double1 : d1) {
			db =   (new BigDecimal(db+double1).setScale(scale, BigDecimal.ROUND_HALF_UP)).doubleValue();
		}
		return db;
	}
	/**
	 * 
	 * Description:  double相减，四舍五入
	 *
	 * @param d1
	 * @param d2
	 * @param scale
	 * @return 
	 * @see
	 */
	public static double subtractForRoundHalfUp(Double d1,Double d2,int scale){
	    BigDecimal b1 = new BigDecimal(d1.toString());
	    BigDecimal b2 = new BigDecimal(d2.toString());
	    return  b1.subtract(b2).setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}
	/**
	 * 
	 * @Method: multiplyForRoundHalfUp 
	 * @Description: 两个小数相乘，四舍五入
	 * @param d1
	 * @param d2
	 * @param scale 小数位数
	 * @return double
	 * @throws
	 */
	public static double multiplyForRoundHalfUp(Double d1,Double d2,int scale){
		return (new BigDecimal(d1*d2).setScale(scale, BigDecimal.ROUND_HALF_UP)).doubleValue();
	}
	/**
	 * 
	 * @Method: multiplyForRoundHalfUp 
	 * @Description: 两个小数相乘，四舍五入
	 * @param d1
	 * @param d2
	 * @param scale 小数位数
	 * @return double
	 * @throws
	 */
	public static double multiplyForRoundHalfUp(Integer d1,Double d2,int scale){
		return (new BigDecimal(d1*d2).setScale(scale, BigDecimal.ROUND_HALF_UP)).doubleValue();
	}
	/**
	 * 
	 * @Method: divideForRoundHalfUp 
	 * @Description: 两个小数相除，四舍五入
	 * @param d1
	 * @param d2
	 * @param scale 小数位数
	 * @return double
	 * @throws
	 */
	public static double divideForRoundHalfUp(Double d1,Double d2,int scale){
		return (new BigDecimal(d1/d2).setScale(scale, BigDecimal.ROUND_HALF_UP)).doubleValue();
	}
	 /**金额为分的格式 */    
    public static final String CURRENCY_FEN_REGEX = "\\-?[0-9]+";    
        
    /**   
     * 将分为单位的转换为元并返回金额格式的字符串 （除100）  
     *   
     * @param amount  
     * @return  
     * @throws Exception   
     */    
    public static String changeF2Y(Long amount) throws Exception{    
        if(!amount.toString().matches(CURRENCY_FEN_REGEX)) {    
            throw new Exception("金额格式有误");    
        }    
            
        int flag = 0;    
        String amString = amount.toString();    
        if(amString.charAt(0)=='-'){    
            flag = 1;    
            amString = amString.substring(1);    
        }    
        StringBuffer result = new StringBuffer();    
        if(amString.length()==1){    
            result.append("0.0").append(amString);    
        }else if(amString.length() == 2){    
            result.append("0.").append(amString);    
        }else{    
            String intString = amString.substring(0,amString.length()-2);    
            for(int i=1; i<=intString.length();i++){    
                if( (i-1)%3 == 0 && i !=1){    
                    result.append(",");    
                }    
                result.append(intString.substring(intString.length()-i,intString.length()-i+1));    
            }    
            result.reverse().append(".").append(amString.substring(amString.length()-2));    
        }    
        if(flag == 1){    
            return "-"+result.toString();    
        }else{    
            return result.toString();    
        }    
    }    
        
    /**  
     * 将分为单位的转换为元 （除100）  
     *   
     * @param amount  
     * @return  
     * @throws Exception   
     */    
    public static String changeF2Y(String amount) throws Exception{  
        if(!amount.matches(CURRENCY_FEN_REGEX)) {    
        	System.out.println(amount+"-----------转换的时候");
            throw new Exception("金额格式有误");    
        }    
        return BigDecimal.valueOf(Long.valueOf(amount)).divide(new BigDecimal(100)).toString();    
    }    
        
    /**   
     * 将元为单位的转换为分 （乘100）  
     *   
     * @param amount  
     * @return  
     */    
    public static String changeY2F(Long amount){    
        return BigDecimal.valueOf(amount).multiply(new BigDecimal(100)).toString();    
    }    
        
    /**   
     * 将元为单位的转换为分 替换小数点，支持以逗号区分的金额  
     *   
     * @param amount  
     * @return  
     */    
    public static String changeY2F(String amount){    
        String currency =  amount.replaceAll("\\$|\\￥|\\,", "");  //处理包含, ￥ 或者$的金额    
        int index = currency.indexOf(".");    
        int length = currency.length();    
        Long amLong = 0l;    
        if(index == -1){    
            amLong = Long.valueOf(currency+"00");    
        }else if(length - index >= 3){    
            amLong = Long.valueOf((currency.substring(0, index+3)).replace(".", ""));    
        }else if(length - index == 2){    
            amLong = Long.valueOf((currency.substring(0, index+2)).replace(".", "")+0);    
        }else{    
            amLong = Long.valueOf((currency.substring(0, index+1)).replace(".", "")+"00");    
        }    
        return amLong.toString();    
    } 
    
    /**
	 * 字符串转化为double
	 * @param str
	 * @param defaultValue
	 * @return
	 */
	public static double getDoubleValueOfString(String str, double defaultValue) {
		try {
			return Double.parseDouble(str);
		} catch (Exception e) {
			return defaultValue;
		}
	}
	
	public static Boolean isDouble(String str){
		try {
			Double.parseDouble(str);
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	/**
	 * @Method: getIntValueOfString 
	 * @Description: 字符串类型转换成int类型
	 * @param str
	 * @param defaultValue 转换失败，返回的默认值
	 * @return int
	 * @throws
	 */
	public static int getIntValueOfString(String str, int defaultValue) {
		try {
			return Integer.parseInt(str.trim());
		} catch (Exception e) {
			return defaultValue;
		}
	}
	
	/**将整数转化成小数**/
    public static Double getStringToDecimal(String code)
    {
         Double result = null;
        if(code != null &&!"".equals(code))
        {
            Double tt = Double.valueOf(code);
            result = tt/100;
        }
        return result;
    }
    
    
    /**
     *  
     * Description: 小数转换成百分比
     *
     * @param decimal
     * @return 
     * @see
     */
    public static String formattedDecimalToPercentage(double decimal)  
    {  
        //获取格式化对象  
        NumberFormat nt = NumberFormat.getPercentInstance();  
        //设置百分数精确度2即保留两位小数  
        nt.setMinimumFractionDigits(2);  
        String result = nt.format(decimal).substring(0, nt.format(decimal).length()-1);
        return result;  
    }  
          
    
    /**
     * 提供精确的加法运算
     * @param value1 被加数
     * @param value2 加数
     * @return 两个参数的和
     */
    public static Double add(Double value1, Double value2) {
    	BigDecimal b1 = new BigDecimal(Double.toString(value1));
    	BigDecimal b2 = new BigDecimal(Double.toString(value2));
    	return b1.add(b2).doubleValue();
    }
	
    public static double getTwoDecimal(double num) {         
    	DecimalFormat dFormat=new DecimalFormat("#.00");        
    	String yearString=dFormat.format(num);        
    	Double temp= Double.valueOf(yearString);        
    	return temp;   
    }


    
    
    public static void main(String[] args) throws Exception {
//    	System.out.println(getStringToDecimal("60"));
//    	System.out.println(formattedDecimalToPercentage(getStringToDecimal("60")));
//    	
//    	Double tt = 0.0009d;
//    	String ss = String.format("%.2f",tt);
//    	System.out.println(ss);
    	Double tf =1000.00;
    	Double tf2 =300.00;
    	Double tt = multiplyForRoundHalfUp(tf2,tf,0);
    	int yh = tt.intValue();
    	System.out.println(yh);
	}
}
 