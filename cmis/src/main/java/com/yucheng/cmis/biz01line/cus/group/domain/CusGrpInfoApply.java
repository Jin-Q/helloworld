package com.yucheng.cmis.biz01line.cus.group.domain;
/**
 * Title: CusGrpInfoApply.java
 * Description: 
 * @author：g	
 * @create date：Mar 9 CST 2010
 * @version：1.0
 */
public class CusGrpInfoApply implements com.yucheng.cmis.pub.CMISDomain {
	
	private String serno;
	private String grpNo;
	private String grpName;
	private String parentCusId;
	private String parentCusName;
	private String parentOrgCode;
	private String parentLoanCard;
	private String grpFinanceType;
	private String grpDetail;
	private String managerBrId;
	private String managerId;
	private String inputId;
	private String inputDate;
	private String inputBrId;
	private String approveStatus;
	private String isChange;
	
	public String getManagerBrId() {
		return managerBrId;
	}

	public void setManagerBrId(String managerBrId) {
		this.managerBrId = managerBrId;
	}

	public String getManagerId() {
		return managerId;
	}

	public void setManagerId(String managerId) {
		this.managerId = managerId;
	}

	public String getInputId() {
		return inputId;
	}

	public void setInputId(String inputId) {
		this.inputId = inputId;
	}

	public String getIsChange() {
		return isChange;
	}

	public void setIsChange(String isChange) {
		this.isChange = isChange;
	}

	/**
	 * @调用父类clone方法
	 * @return Object
	 * @throws CloneNotSupportedException
	 */
	public Object clone()throws CloneNotSupportedException{
		Object result = super.clone();
		//TODO: 定制clone数据
		return result;
	}
	
	public String getSerno() {
		return serno;
	}

	public void setSerno(String serno) {
		this.serno = serno;
	}

	public String getGrpNo() {
		return grpNo;
	}
	public void setGrpNo(String grpNo) {
		this.grpNo = grpNo;
	}
	public String getGrpName() {
		return grpName;
	}
	public void setGrpName(String grpName) {
		this.grpName = grpName;
	}
	public String getParentCusId() {
		return parentCusId;
	}
	public void setParentCusId(String parentCusId) {
		this.parentCusId = parentCusId;
	}
	public String getParentCusName() {
		return parentCusName;
	}
	public void setParentCusName(String parentCusName) {
		this.parentCusName = parentCusName;
	}
	public String getParentOrgCode() {
		return parentOrgCode;
	}
	public void setParentOrgCode(String parentOrgCode) {
		this.parentOrgCode = parentOrgCode;
	}
	public String getParentLoanCard() {
		return parentLoanCard;
	}
	public void setParentLoanCard(String parentLoanCard) {
		this.parentLoanCard = parentLoanCard;
	}
	public String getGrpFinanceType() {
		return grpFinanceType;
	}
	public void setGrpFinanceType(String grpFinanceType) {
		this.grpFinanceType = grpFinanceType;
	}
	public String getGrpDetail() {
		return grpDetail;
	}
	public void setGrpDetail(String grpDetail) {
		this.grpDetail = grpDetail;
	}

	public String getInputDate() {
		return inputDate;
	}
	public void setInputDate(String inputDate) {
		this.inputDate = inputDate;
	}
	public String getInputBrId() {
		return inputBrId;
	}
	public void setInputBrId(String inputBrId) {
		this.inputBrId = inputBrId;
	}
	public String getApproveStatus() {
		return approveStatus;
	}
	public void setApproveStatus(String approveStatus) {
		this.approveStatus = approveStatus;
	}
}
