package com.videoweb.ying.po;

import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.videoweb.base.BaseModel;
import java.io.Serializable;


/**
 * <p>
 * 推广充值收益
 * </p>
 *
 * @author haha
 * @since 2018-11-28
 */
@TableName("t_extend_price")
@SuppressWarnings("serial")
public class TExtendPrice extends BaseModel {

      /**
     * 充值流水ID
     */
         	   @TableField("vip_history_id")
        	private Integer vipHistoryId;
      /**
     * 充值人ID
     */
         	   @TableField("member_id")
        	private Integer memberId;
      /**
     * 充值人姓名
     */
            	private String name;
      /**
     * 充值人手机号
     */
            	private String tel;
      /**
     * 充值时间
     */
         	   @TableField("recharge_time")
        	private Date rechargeTime;
      /**
     * 充值金额
     */
            	private Double price;
      /**
     * 奖励比率
     */
         	   @TableField("recharge_rate")
        	private Double rechargeRate;
      /**
     * 获取砖石数
     */
         	   @TableField("cron_num")
        	private Integer cronNum;
      /**
     * 推广人ID
     */
         	   @TableField("extend_id")
        	private Integer extendId;


	public Integer getVipHistoryId() {
		return vipHistoryId;
	}

	public void setVipHistoryId(Integer vipHistoryId) {
		this.vipHistoryId = vipHistoryId;
	}

	public Integer getMemberId() {
		return memberId;
	}

	public void setMemberId(Integer memberId) {
		this.memberId = memberId;
	}

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

	public Date getRechargeTime() {
		return rechargeTime;
	}

	public void setRechargeTime(Date rechargeTime) {
		this.rechargeTime = rechargeTime;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Double getRechargeRate() {
		return rechargeRate;
	}

	public void setRechargeRate(Double rechargeRate) {
		this.rechargeRate = rechargeRate;
	}

	public Integer getCronNum() {
		return cronNum;
	}

	public void setCronNum(Integer cronNum) {
		this.cronNum = cronNum;
	}

	public Integer getExtendId() {
		return extendId;
	}

	public void setExtendId(Integer extendId) {
		this.extendId = extendId;
	}

}