package com.videoweb.ying.po;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.videoweb.base.BaseModel;
import java.io.Serializable;



@TableName("t_version")
@SuppressWarnings("serial")
public class TVersion extends BaseModel {

      /**
     * 版本类型
     */
         	   @TableField("version_type")
        	private Integer versionType;
      /**
     * 链接地址
     */
         	   @TableField("version_url")
        	private String versionUrl;
      /**
     * 版本号
     */
         @TableField("version_code")
          private String versionCode;
         
         /**
          * 是否强制更新 0=否 1=是
          */
     	  @TableField("is_update")
     	   private Integer isUpdate;


	public Integer getVersionType() {
		return versionType;
	}

	public void setVersionType(Integer versionType) {
		this.versionType = versionType;
	}

	public String getVersionUrl() {
		return versionUrl;
	}

	public void setVersionUrl(String versionUrl) {
		this.versionUrl = versionUrl;
	}

	public String getVersionCode() {
		return versionCode;
	}

	public void setVersionCode(String versionCode) {
		this.versionCode = versionCode;
	}

}