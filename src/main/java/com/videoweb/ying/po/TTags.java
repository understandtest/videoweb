package com.videoweb.ying.po;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.videoweb.base.BaseModel;
import java.io.Serializable;



@TableName("t_tags")
@SuppressWarnings("serial")
public class TTags extends BaseModel {

      /**
     * 标签类型ID
     */
         	@TableField("tag_type_id")
        	private Integer tagTypeId;
         	   
         	@TableField("pic_type")
         	private Integer picType;
         	   
         	public Integer getPicType() {
				return picType;
			}

			public void setPicType(Integer picType) {
				this.picType = picType;
			}

			@TableField("pic_url")
         	private String  picUrl;
		      /**
		     * 标签名称
		     */
            	private String name;


	public Integer getTagTypeId() {
		return tagTypeId;
	}

	public void setTagTypeId(Integer tagTypeId) {
		this.tagTypeId = tagTypeId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}
	
	

}