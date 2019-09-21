package com.videoweb.ying.po;

import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.videoweb.base.BaseModel;



@TableName("t_member_login")
@SuppressWarnings("serial")
public class TMemberLogin extends BaseModel {

      /**
     * 手机号
     */
            	private String tel;
      /**
     * 密码
     */
            	private String pwd;
      /**
     * 会员ID
     */
         	   @TableField("member_id")
        	private Integer memberId;
      /**
     * 是否可用
     */
         	   @TableField("is_enable")
        	private Integer isEnable;
         	   
         	   
         	@TableField("device_code")   
         	private String deviceCode;
         	/**
         	 * 设备是否可用
         	 */
         	@TableField("is_device_enable") 
         	private Integer isDeviceEnable;
         	/**
         	 * 登录时间
         	 */
         	@TableField("login_time")
         	private Date loginTime;
         	
         	/**
         	 * 渠道包
         	 */
        	@TableField("from_code")
         	private String fromCode;
         	

        	public Integer getIsDeviceEnable() {
				return isDeviceEnable;
			}

			public void setIsDeviceEnable(Integer isDeviceEnable) {
				this.isDeviceEnable = isDeviceEnable;
			}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public Integer getMemberId() {
		return memberId;
	}

	public void setMemberId(Integer memberId) {
		this.memberId = memberId;
	}

	public Integer getIsEnable() {
		return isEnable;
	}

	public void setIsEnable(Integer isEnable) {
		this.isEnable = isEnable;
	}

	public String getDeviceCode() {
		return deviceCode;
	}

	public void setDeviceCode(String deviceCode) {
		this.deviceCode = deviceCode;
	}

	public Date getLoginTime() {
		return loginTime;
	}

	public void setLoginTime(Date loginTime) {
		this.loginTime = loginTime;
	}

	public String getFromCode() {
		return fromCode;
	}

	public void setFromCode(String fromCode) {
		this.fromCode = fromCode;
	}

	
	
}