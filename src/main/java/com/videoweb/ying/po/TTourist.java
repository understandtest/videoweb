package com.videoweb.ying.po;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.videoweb.base.BaseModel;
import java.io.Serializable;


@TableName("t_tourist")
@SuppressWarnings("serial")
public class TTourist extends BaseModel {

      /**
     * 观影次数
     */
         	   @TableField("view_num")
        	private Integer viewNum;
      /**
     * 缓存次数
     */
         	   @TableField("cache_num")
        	private Integer cacheNum;


	public Integer getViewNum() {
		return viewNum;
	}

	public void setViewNum(Integer viewNum) {
		this.viewNum = viewNum;
	}

	public Integer getCacheNum() {
		return cacheNum;
	}

	public void setCacheNum(Integer cacheNum) {
		this.cacheNum = cacheNum;
	}

}