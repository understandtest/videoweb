package com.videoweb.ying.po;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.videoweb.base.BaseModel;


/**
 * <p>
 * 汇率设置
 * </p>
 *
 * @author haha
 * @since 2018-11-28
 */
@TableName("t_rate")
@SuppressWarnings("serial")
public class TRate extends BaseModel {

      /**
     * 充值奖励比例
     */
         	   @TableField("recharge_rate")
        	private Double rechargeRate;
      /**
     * 钻石兑换比例
     */
         	   @TableField("cron_rate")
        	private Double cronRate;
      /**
     * 提现钻石基数
     */
         	   @TableField("cron_num")
        	private Double cronNum;
      /**
     * 最多抵扣金额
     */
         	   @TableField("most_price")
        	private Double mostPrice;
      /**
     * 提现手续费用
     */
         	   @TableField("charge_fee")
        	private Double chargeFee;


	public Double getRechargeRate() {
		return rechargeRate;
	}

	public void setRechargeRate(Double rechargeRate) {
		this.rechargeRate = rechargeRate;
	}

	

	public Double getCronRate() {
		return cronRate;
	}

	public void setCronRate(Double cronRate) {
		this.cronRate = cronRate;
	}

	public Double getCronNum() {
		return cronNum;
	}

	public void setCronNum(Double cronNum) {
		this.cronNum = cronNum;
	}

	public Double getMostPrice() {
		return mostPrice;
	}

	public void setMostPrice(Double mostPrice) {
		this.mostPrice = mostPrice;
	}

	public Double getChargeFee() {
		return chargeFee;
	}

	public void setChargeFee(Double chargeFee) {
		this.chargeFee = chargeFee;
	}

}