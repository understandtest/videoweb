package com.videoweb.ying.po;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.videoweb.base.BaseModel;
import java.io.Serializable;



@TableName("t_search")
@SuppressWarnings("serial")
public class TSearch extends BaseModel {

      /**
     * 搜索关键字
     */
         	   @TableField("search_name")
        	private String searchName;
         	
            @TableField("search_num")
         	private Integer searchNum;


	public String getSearchName() {
		return searchName;
	}

	public void setSearchName(String searchName) {
		this.searchName = searchName;
	}

	public Integer getSearchNum() {
		return searchNum;
	}

	public void setSearchNum(Integer searchNum) {
		this.searchNum = searchNum;
	}
	
	
}