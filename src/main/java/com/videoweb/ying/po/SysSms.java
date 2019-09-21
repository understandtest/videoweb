package com.videoweb.ying.po;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.videoweb.base.BaseModel;


/**
 * <p>
 * 
 * </p>
 *
 * @author haha
 * @since 2019-03-24
 */
@TableName("sys_sms")
@SuppressWarnings("serial")
public class SysSms extends BaseModel {

	/**
	 * 用户id
	 */
	private String uid;
	/**
	 * 接口密码
	 */
	private String pwd;
	/**
	 * 接口模板
	 */
	private String template;

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}
}