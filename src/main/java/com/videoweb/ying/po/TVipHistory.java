package com.videoweb.ying.po;

import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.videoweb.base.BaseModel;
import java.io.Serializable;


@TableName("t_vip_history")
@SuppressWarnings("serial")
public class TVipHistory extends BaseModel {

      /**
     * 会员ID
     */
         	   @TableField("member_id")
        	private Integer memberId;
      /**
     * VIP信息ID
     */
         	   @TableField("vip_id")
        	private Integer vipId;
      /**
     * 支付时间
     */
         	   @TableField("pay_time")
        	private Date payTime;
      /**
     * 支付流水
     */
         	   @TableField("pay_no")
        	private String payNo;
      /**
     * 支付金额
     */
         	   @TableField("pay_price")
        	private Double payPrice;
      /**
     * 有效期
     */
         	   @TableField("date_validity")
        	private Integer dateValidity;
         	   
         		/**
            	 * 支付状态 1=待支付 2=已支付
            	 */
            	@TableField("pay_status")
            	private Integer payStatus;

            	/**
             	 * 支付方式
             	 */
             	@TableField("pay_type")
             	private Integer payType;
             	/**
             	 *  外部支付流水号
             	 */
             	@TableField("pay_out_no")
             	private String payOutNo;
             	/**
             	 * 是否使用钻石抵扣
             	 */
             	@TableField("is_use_cron")
             	private Integer isUseCron;
             	/**
             	 * 使用钻石数
             	 */
             	@TableField("use_cron")
             	private Integer useCron;
             	
             	
             	
             	
             	
             	public Integer getIsUseCron() {
					return isUseCron;
				}

				public void setIsUseCron(Integer isUseCron) {
					this.isUseCron = isUseCron;
				}

				public Integer getUseCron() {
					return useCron;
				}

				public void setUseCron(Integer useCron) {
					this.useCron = useCron;
				}

				public Integer getPayType() {
					return payType;
				}

				public void setPayType(Integer payType) {
					this.payType = payType;
				}

				public String getPayOutNo() {
					return payOutNo;
				}

				public void setPayOutNo(String payOutNo) {
					this.payOutNo = payOutNo;
				}

	public Integer getMemberId() {
		return memberId;
	}

	public void setMemberId(Integer memberId) {
		this.memberId = memberId;
	}

	public Integer getVipId() {
		return vipId;
	}

	public void setVipId(Integer vipId) {
		this.vipId = vipId;
	}

	public Date getPayTime() {
		return payTime;
	}

	public void setPayTime(Date payTime) {
		this.payTime = payTime;
	}

	public String getPayNo() {
		return payNo;
	}

	public void setPayNo(String payNo) {
		this.payNo = payNo;
	}

	public Double getPayPrice() {
		return payPrice;
	}

	public void setPayPrice(Double payPrice) {
		this.payPrice = payPrice;
	}

	public Integer getDateValidity() {
		return dateValidity;
	}

	public void setDateValidity(Integer dateValidity) {
		this.dateValidity = dateValidity;
	}

	public Integer getPayStatus() {
		return payStatus;
	}

	public void setPayStatus(Integer payStatus) {
		this.payStatus = payStatus;
	}
	
	

}