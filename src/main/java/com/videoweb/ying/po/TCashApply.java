package com.videoweb.ying.po;

import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.videoweb.base.BaseModel;
import java.io.Serializable;


/**
 * <p>
 * 提现申请表
 * </p>
 *
 * @author haha
 * @since 2018-11-28
 */
@TableName("t_cash_apply")
@SuppressWarnings("serial")
public class TCashApply extends BaseModel {

      /**
     * 用户ID
     */
         	   @TableField("member_id")
        	private Integer memberId;
      /**
     * 当前钻石数
     */
         	   @TableField("cron_num")
        	private Integer cronNum;
      /**
     * 钻石折算比例
     */
         	   @TableField("cron_rate")
        	private Double cronRate;
      /**
     * 提现手续费
     */
         	   @TableField("charge_fee")
        	private Double chargeFee;
      /**
     * 可提现金额
     */
         	   @TableField("use_price")
        	private Double usePrice;
      /**
     * 提现金额
     */
         	   @TableField("get_price")
        	private Double getPrice;
      /**
     * 收款人姓名
     */
         	   @TableField("payee_name")
        	private String payeeName;
      /**
     * 银行卡账号
     */
         	   @TableField("card_no")
        	private String cardNo;
      /**
     * 提现状态  1=待审核 2=已支付
     */
         	   @TableField("pay_status")
        	private Integer payStatus;
      /**
     * 提现时间
     */
         	   @TableField("approve_time")
        	private Date approveTime;
      /**
     * 审批时间
     */
         	   @TableField("apply_time")
        	private Date applyTime;


	public Integer getMemberId() {
		return memberId;
	}

	public void setMemberId(Integer memberId) {
		this.memberId = memberId;
	}

	public Integer getCronNum() {
		return cronNum;
	}

	public void setCronNum(Integer cronNum) {
		this.cronNum = cronNum;
	}

	public Double getCronRate() {
		return cronRate;
	}

	public void setCronRate(Double cronRate) {
		this.cronRate = cronRate;
	}

	public Double getChargeFee() {
		return chargeFee;
	}

	public void setChargeFee(Double chargeFee) {
		this.chargeFee = chargeFee;
	}

	public Double getUsePrice() {
		return usePrice;
	}

	public void setUsePrice(Double usePrice) {
		this.usePrice = usePrice;
	}

	public Double getGetPrice() {
		return getPrice;
	}

	public void setGetPrice(Double getPrice) {
		this.getPrice = getPrice;
	}

	public String getPayeeName() {
		return payeeName;
	}

	public void setPayeeName(String payeeName) {
		this.payeeName = payeeName;
	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public Integer getPayStatus() {
		return payStatus;
	}

	public void setPayStatus(Integer payStatus) {
		this.payStatus = payStatus;
	}

	public Date getApproveTime() {
		return approveTime;
	}

	public void setApproveTime(Date approveTime) {
		this.approveTime = approveTime;
	}

	public Date getApplyTime() {
		return applyTime;
	}

	public void setApplyTime(Date applyTime) {
		this.applyTime = applyTime;
	}

}