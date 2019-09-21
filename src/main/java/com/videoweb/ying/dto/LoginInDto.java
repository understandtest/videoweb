package com.videoweb.ying.dto;

public class LoginInDto {

	/**
	 * 用户登录ID
	 */
	private Integer loginId;
	/**
	 * 用户类型 1=员工 2=商家
	 */
	private Integer userType;
	/**
	 * 是否是商家 0=否 1=是
	 */
	private Integer isBusiness;
	/**
	 * 商家Id
	 */
	private Integer businessId;
	/**
	 * 成员ID
	 */
	private Integer employeeId;
	/**
	 * 成员名称
	 */
	private String employeeName;
	/**
	 * 商户名称
	 */
	private String businessName;
	
	/**
	 * 成员工号
	 */
	private String workNo;

	public Integer getLoginId() {
		return loginId;
	}

	public void setLoginId(Integer loginId) {
		this.loginId = loginId;
	}

	public Integer getUserType() {
		return userType;
	}

	public void setUserType(Integer userType) {
		this.userType = userType;
	}

	public Integer getIsBusiness() {
		return isBusiness;
	}

	public void setIsBusiness(Integer isBusiness) {
		this.isBusiness = isBusiness;
	}

	public Integer getBusinessId() {
		return businessId;
	}

	public void setBusinessId(Integer businessId) {
		this.businessId = businessId;
	}

	public Integer getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Integer employeeId) {
		this.employeeId = employeeId;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public String getWorkNo() {
		return workNo;
	}

	public void setWorkNo(String workNo) {
		this.workNo = workNo;
	}

	public String getBusinessName() {
		return businessName;
	}

	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}
	
	
	
	
}
