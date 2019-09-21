package com.videoweb.ying.po;

import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.videoweb.base.BaseModel;
import java.io.Serializable;



@TableName("t_extension_history")
@SuppressWarnings("serial")
public class TExtensionHistory extends BaseModel {

      /**
     * 会员ID
     */
         	   @TableField("member_id")
        	private Integer memberId;
         	
           @TableField("extend_id")
         	private Integer extendId;
      /**
     * 用户名
     */
         	   @TableField("nick_name")
        	private String nickName;
      /**
     * 手机号
     */
            	private String tel;
      /**
     * 注册时间
     */
         	   @TableField("regedit_time")
        	private Date regeditTime;


	public Integer getMemberId() {
		return memberId;
	}

	public void setMemberId(Integer memberId) {
		this.memberId = memberId;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public Date getRegeditTime() {
		return regeditTime;
	}

	public void setRegeditTime(Date regeditTime) {
		this.regeditTime = regeditTime;
	}

	public Integer getExtendId() {
		return extendId;
	}

	public void setExtendId(Integer extendId) {
		this.extendId = extendId;
	}

	
	
}