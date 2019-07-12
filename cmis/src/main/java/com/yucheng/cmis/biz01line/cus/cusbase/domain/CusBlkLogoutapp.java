package com.yucheng.cmis.biz01line.cus.cusbase.domain;

/**
 * Title: CusBlkLogoutapp.java Description:
 * 
 * @author：echow heyc@yuchengtech.com
 * @create date：Sat Jun 27 09:31:27 CST 2009
 * @version：1.0
 */
public class CusBlkLogoutapp implements com.yucheng.cmis.pub.CMISDomain {
	private String serno;
	private String oldSerno;
	private String blackDate;
	private String cusName;
	private String certCode;
	private String certType;
	private String blackType;
	private String blackLevel;
	private String legalName;
	private String legalPhone;
	private String legalAddr;
	private String blackReason;
	private String logoutReason;
	private String remarks;
	private String approveStatus;
	private String inputId;
	private String inputBrId;
	private String inputDate;
	private String cusId;

	public String getCusId() {
		return cusId;
	}

	public void setCusId(String cusId) {
		this.cusId = cusId;
	}

	/**
	 * @调用父类clone方法
	 * @return Object
	 * @throws CloneNotSupportedException
	 */
	public Object clone() throws CloneNotSupportedException {
		Object result = super.clone();
		// TODO: 定制clone数据
		return result;
	}

	/**
	 * @return 返回 serno
	 */
	public String getSerno() {
		return serno;
	}

	/**
	 * @设置 serno
	 * @param serno
	 */
	public void setSerno(String serno) {
		this.serno = serno;
	}

	/**
	 * @return 返回 oldSerno
	 */
	public String getOldSerno() {
		return oldSerno;
	}

	/**
	 * @设置 oldSerno
	 * @param oldSerno
	 */
	public void setOldSerno(String oldSerno) {
		this.oldSerno = oldSerno;
	}

	/**
	 * @return 返回 blackDate
	 */
	public String getBlackDate() {
		return blackDate;
	}

	/**
	 * @设置 blackDate
	 * @param blackDate
	 */
	public void setBlackDate(String blackDate) {
		this.blackDate = blackDate;
	}

	/**
	 * @return 返回 cusName
	 */
	public String getCusName() {
		return cusName;
	}

	/**
	 * @设置 cusName
	 * @param cusName
	 */
	public void setCusName(String cusName) {
		this.cusName = cusName;
	}

	/**
	 * @return 返回 certCode
	 */
	public String getCertCode() {
		return certCode;
	}

	/**
	 * @设置 certCode
	 * @param certCode
	 */
	public void setCertCode(String certCode) {
		this.certCode = certCode;
	}

	/**
	 * @return 返回 certType
	 */
	public String getCertType() {
		return certType;
	}

	/**
	 * @设置 certType
	 * @param certType
	 */
	public void setCertType(String certType) {
		this.certType = certType;
	}

	/**
	 * @return 返回 blackType
	 */
	public String getBlackType() {
		return blackType;
	}

	/**
	 * @设置 blackType
	 * @param blackType
	 */
	public void setBlackType(String blackType) {
		this.blackType = blackType;
	}

	/**
	 * @return 返回 blackLevel
	 */
	public String getBlackLevel() {
		return blackLevel;
	}

	/**
	 * @设置 blackLevel
	 * @param blackLevel
	 */
	public void setBlackLevel(String blackLevel) {
		this.blackLevel = blackLevel;
	}

	/**
	 * @return 返回 legalName
	 */
	public String getLegalName() {
		return legalName;
	}

	/**
	 * @设置 legalName
	 * @param legalName
	 */
	public void setLegalName(String legalName) {
		this.legalName = legalName;
	}

	/**
	 * @return 返回 legalPhone
	 */
	public String getLegalPhone() {
		return legalPhone;
	}

	/**
	 * @设置 legalPhone
	 * @param legalPhone
	 */
	public void setLegalPhone(String legalPhone) {
		this.legalPhone = legalPhone;
	}

	/**
	 * @return 返回 legalAddr
	 */
	public String getLegalAddr() {
		return legalAddr;
	}

	/**
	 * @设置 legalAddr
	 * @param legalAddr
	 */
	public void setLegalAddr(String legalAddr) {
		this.legalAddr = legalAddr;
	}

	/**
	 * @return 返回 blackReason
	 */
	public String getBlackReason() {
		return blackReason;
	}

	/**
	 * @设置 blackReason
	 * @param blackReason
	 */
	public void setBlackReason(String blackReason) {
		this.blackReason = blackReason;
	}

	/**
	 * @return 返回 logoutReason
	 */
	public String getLogoutReason() {
		return logoutReason;
	}

	/**
	 * @设置 logoutReason
	 * @param logoutReason
	 */
	public void setLogoutReason(String logoutReason) {
		this.logoutReason = logoutReason;
	}

	/**
	 * @return 返回 remarks
	 */
	public String getRemarks() {
		return remarks;
	}

	/**
	 * @设置 remarks
	 * @param remarks
	 */
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	/**
	 * @return 返回 approveStatus
	 */
	public String getApproveStatus() {
		return approveStatus;
	}

	/**
	 * @设置 approveStatus
	 * @param approveStatus
	 */
	public void setApproveStatus(String approveStatus) {
		this.approveStatus = approveStatus;
	}

	/**
	 * @return 返回 inputId
	 */
	public String getInputId() {
		return inputId;
	}

	/**
	 * @设置 inputId
	 * @param inputId
	 */
	public void setInputId(String inputId) {
		this.inputId = inputId;
	}

	/**
	 * @return 返回 inputBrId
	 */
	public String getInputBrId() {
		return inputBrId;
	}

	/**
	 * @设置 inputBrId
	 * @param inputBrId
	 */
	public void setInputBrId(String inputBrId) {
		this.inputBrId = inputBrId;
	}

	/**
	 * @return 返回 inputDate
	 */
	public String getInputDate() {
		return inputDate;
	}

	/**
	 * @设置 inputDate
	 * @param inputDate
	 */
	public void setInputDate(String inputDate) {
		this.inputDate = inputDate;
	}
}