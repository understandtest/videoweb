package com.videoweb.ying.po;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.videoweb.base.BaseModel;
import java.io.Serializable;



@TableName("t_classify")
@SuppressWarnings("serial")
public class TClassify extends BaseModel {

      /**
     * 分类名称
     */
            	private String name;
      /**
     * 分类图标
     */
         	   @TableField("classify_icon")
        	private String classifyIcon;


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getClassifyIcon() {
		return classifyIcon;
	}

	public void setClassifyIcon(String classifyIcon) {
		this.classifyIcon = classifyIcon;
	}

}