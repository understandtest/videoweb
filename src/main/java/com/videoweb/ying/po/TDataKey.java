package com.videoweb.ying.po;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.videoweb.base.BaseModel;
import java.io.Serializable;



@TableName("t_data_key")
@SuppressWarnings("serial")
public class TDataKey extends BaseModel {

      /**
     * 基础数据类型
     */
         	   @TableField("data_key_type")
        	private String dataKeyType;
      /**
     * 基础数据KEY值
     */
         	   @TableField("data_key")
        	private String dataKey;
      /**
     * 基础数据键值
     */
         	   @TableField("data_value")
        	private String dataValue;


	public String getDataKeyType() {
		return dataKeyType;
	}

	public void setDataKeyType(String dataKeyType) {
		this.dataKeyType = dataKeyType;
	}

	

	public String getDataKey() {
		return dataKey;
	}

	public void setDataKey(String dataKey) {
		this.dataKey = dataKey;
	}

	public String getDataValue() {
		return dataValue;
	}

	public void setDataValue(String dataValue) {
		this.dataValue = dataValue;
	}

}