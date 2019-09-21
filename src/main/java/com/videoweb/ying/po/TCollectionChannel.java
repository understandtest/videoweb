package com.videoweb.ying.po;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.videoweb.base.BaseModel;
import java.io.Serializable;



@TableName("t_collection_channel")
@SuppressWarnings("serial")
public class TCollectionChannel extends BaseModel {

      /**
     * 渠道名称
     */
         	   @TableField("channel_name")
        	private String channelName;


	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

}