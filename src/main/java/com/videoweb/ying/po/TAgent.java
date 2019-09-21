package com.videoweb.ying.po;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.videoweb.base.BaseModel;
import java.io.Serializable;


/**
 * <p>
 * 代理表
 * </p>
 *
 * @author haha
 * @since 2018-11-28
 */
@TableName("t_agent")
@SuppressWarnings("serial")
public class TAgent extends BaseModel {

      /**
     * 代理名称
     */
            	private String name;
      /**
     * 联系方式
     */
            	private String tel;
      /**
     * 备注
     */
         	   @TableField("remark_note")
        	private String remarkNote;


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getRemarkNote() {
		return remarkNote;
	}

	public void setRemarkNote(String remarkNote) {
		this.remarkNote = remarkNote;
	}

}