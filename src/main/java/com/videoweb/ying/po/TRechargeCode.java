package com.videoweb.ying.po;

import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.videoweb.base.BaseModel;
import java.io.Serializable;


/**
 * <p>
 * 充值码
 * </p>
 *
 * @author haha
 * @since 2018-11-28
 */
@TableName("t_recharge_code")
@SuppressWarnings("serial")
public class TRechargeCode extends BaseModel {

      /**
     * 代理ID
     */
         	   @TableField("agent_id")
        	private Integer agentId;
      /**
     * 充值码编号
     */
         	   @TableField("recharge_code")
        	private String rechargeCode;
      /**
     * 生成时间
     */
         	   @TableField("generation_time")
        	private Date generationTime;
      /**
     * 售出时间
     */
         	   @TableField("sales_time")
        	private Date salesTime;
        /**
         * 卡片类型 充值卡类型 会员卡类型 1=月卡 2=季卡 3= 年卡
         */
         @TableField("card_type")
         private Integer cardType;
         
         /**
          * 充值卡ID
          */
         @TableField("vip_id")
         private Integer vipId;
         
     
      /**
     * 充值用户
     */
        @TableField("member_id")
        private Integer memberId;
      /**
     * 是否售出 1= 否 2=是
     */
        @TableField("is_sales")
        private Integer isSales;
      /**
     * 是否激活 1= 否 2=是
     */
         @TableField("is_activity")
         private Integer isActivity;
      /**
     * 激活时间
     */
         @TableField("activity_time")
        private Date activityTime;


	public Integer getAgentId() {
		return agentId;
	}

	public void setAgentId(Integer agentId) {
		this.agentId = agentId;
	}

	public String getRechargeCode() {
		return rechargeCode;
	}

	public void setRechargeCode(String rechargeCode) {
		this.rechargeCode = rechargeCode;
	}

	public Date getGenerationTime() {
		return generationTime;
	}

	public void setGenerationTime(Date generationTime) {
		this.generationTime = generationTime;
	}

	public Date getSalesTime() {
		return salesTime;
	}

	public void setSalesTime(Date salesTime) {
		this.salesTime = salesTime;
	}
	

	public Integer getCardType() {
		return cardType;
	}

	public void setCardType(Integer cardType) {
		this.cardType = cardType;
	}

	public Integer getVipId() {
		return vipId;
	}

	public void setVipId(Integer vipId) {
		this.vipId = vipId;
	}

	public Integer getMemberId() {
		return memberId;
	}

	public void setMemberId(Integer memberId) {
		this.memberId = memberId;
	}

	public Integer getIsSales() {
		return isSales;
	}

	public void setIsSales(Integer isSales) {
		this.isSales = isSales;
	}

	public Integer getIsActivity() {
		return isActivity;
	}

	public void setIsActivity(Integer isActivity) {
		this.isActivity = isActivity;
	}

	public Date getActivityTime() {
		return activityTime;
	}

	public void setActivityTime(Date activityTime) {
		this.activityTime = activityTime;
	}

}