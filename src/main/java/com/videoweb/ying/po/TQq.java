package com.videoweb.ying.po;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.videoweb.base.BaseModel;
import java.io.Serializable;



@TableName("t_qq")
@SuppressWarnings("serial")
public class TQq extends BaseModel {

      /**
     * 名称
     */
            	private String name;
      /**
     * 链接地址
     */
         	   @TableField("link_url")
        	private String linkUrl;


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLinkUrl() {
		return linkUrl;
	}

	public void setLinkUrl(String linkUrl) {
		this.linkUrl = linkUrl;
	}

}