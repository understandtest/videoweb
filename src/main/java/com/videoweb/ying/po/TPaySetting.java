package com.videoweb.ying.po;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.videoweb.base.BaseModel;
import java.io.Serializable;



@TableName("t_pay_setting")
@SuppressWarnings("serial")
public class TPaySetting extends BaseModel {

      /**
     * 类型 1=支付宝 2=微信
     */
         	   @TableField("pay_type")
        	private Integer payType;
      /**
     * 支付名称
     */
         	   @TableField("pay_name")
        	private String payName;
      /**
     * 是否有效 1=有效 2= 无效
     */
         	   @TableField("is_enable")
        	private Integer isEnable;
      /**
     * 图片
     */
         	   @TableField("pay_img")
        	private String payImg;
      /**
     * 排序
     */
         	   @TableField("sort_no")
        	private Integer sortNo;
      /**
     * 描述
     */
            	private String dex;
      /**
     * URL
     */
         	   @TableField("pay_url")
        	private String payUrl;
      /**
     * 账户
     */
         	   @TableField("pay_account")
        	private String payAccount;
      /**
     * 秘钥
     */
         	   @TableField("pay_key")
        	private String payKey;


	public Integer getPayType() {
		return payType;
	}

	public void setPayType(Integer payType) {
		this.payType = payType;
	}

	public String getPayName() {
		return payName;
	}

	public void setPayName(String payName) {
		this.payName = payName;
	}

	public Integer getIsEnable() {
		return isEnable;
	}

	public void setIsEnable(Integer isEnable) {
		this.isEnable = isEnable;
	}

	public String getPayImg() {
		return payImg;
	}

	public void setPayImg(String payImg) {
		this.payImg = payImg;
	}

	public Integer getSortNo() {
		return sortNo;
	}

	public void setSortNo(Integer sortNo) {
		this.sortNo = sortNo;
	}

	public String getDex() {
		return dex;
	}

	public void setDex(String dex) {
		this.dex = dex;
	}

	public String getPayUrl() {
		return payUrl;
	}

	public void setPayUrl(String payUrl) {
		this.payUrl = payUrl;
	}

	public String getPayAccount() {
		return payAccount;
	}

	public void setPayAccount(String payAccount) {
		this.payAccount = payAccount;
	}

	public String getPayKey() {
		return payKey;
	}

	public void setPayKey(String payKey) {
		this.payKey = payKey;
	}

}