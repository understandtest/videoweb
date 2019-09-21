package com.videoweb.ying.po;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.videoweb.base.BaseModel;
import java.io.Serializable;



@TableName("t_extension_info")
@SuppressWarnings("serial")
public class TExtensionInfo extends BaseModel {

      /**
     * 推广链接
     */
         	   @TableField("extension_url")
        	private String extensionUrl;
      /**
     * 推广内容
     */
         	   @TableField("extension_context")
        	private String extensionContext;
      /**
     * 是否上架 0=否 1=是
     */
         	   @TableField("is_show")
        	private Integer isShow;


	public String getExtensionUrl() {
		return extensionUrl;
	}

	public void setExtensionUrl(String extensionUrl) {
		this.extensionUrl = extensionUrl;
	}

	public String getExtensionContext() {
		return extensionContext;
	}

	public void setExtensionContext(String extensionContext) {
		this.extensionContext = extensionContext;
	}

	public Integer getIsShow() {
		return isShow;
	}

	public void setIsShow(Integer isShow) {
		this.isShow = isShow;
	}

}