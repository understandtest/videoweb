package com.videoweb.ying.po;

import com.baomidou.mybatisplus.annotations.TableName;
import com.videoweb.base.BaseModel;
import java.io.Serializable;



@TableName("t_tag_type")
@SuppressWarnings("serial")
public class TTagType extends BaseModel {

      /**
     * 名称
     */
            	private String name;


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}