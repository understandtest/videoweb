package com.videoweb.ying.po;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.videoweb.base.BaseModel;
import java.io.Serializable;



@TableName("t_banner")
@SuppressWarnings("serial")
public class TBanner extends BaseModel {

      /**
     * 类型 1=首页 2= 频道  3=启动页 
     */
         	   @TableField("pic_type")
        	private Integer picType;
      /**
     * 图片
     */
         	   @TableField("pic_url")
        	private String picUrl;
      /**
     * 链接类型 1=外部链接 2=内部影片
     */
         	   @TableField("link_type")
        	private Integer linkType;
      /**
     * 链接地址
     */
         	   @TableField("link_url")
        	private String linkUrl;
         	   /**
         	    * 是否有效 0=否 1=是
         	    */
         	  @TableField("is_show")
          	private Integer isShow;


	public Integer getPicType() {
		return picType;
	}

	public void setPicType(Integer picType) {
		this.picType = picType;
	}

	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

	public Integer getLinkType() {
		return linkType;
	}

	public void setLinkType(Integer linkType) {
		this.linkType = linkType;
	}

	public String getLinkUrl() {
		return linkUrl;
	}

	public void setLinkUrl(String linkUrl) {
		this.linkUrl = linkUrl;
	}

	public Integer getIsShow() {
		return isShow;
	}

	public void setIsShow(Integer isShow) {
		this.isShow = isShow;
	}
	
	

}