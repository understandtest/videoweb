package com.videoweb.ying.po;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.videoweb.base.BaseModel;
import java.io.Serializable;



@TableName("t_view_rule")
@SuppressWarnings("serial")
public class TViewRule extends BaseModel {

      /**
     * 会员ID
     */
         	   @TableField("member_id")
        	private Integer memberId;
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


	public Integer getMemberId() {
		return memberId;
	}

	public void setMemberId(Integer memberId) {
		this.memberId = memberId;
	}

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