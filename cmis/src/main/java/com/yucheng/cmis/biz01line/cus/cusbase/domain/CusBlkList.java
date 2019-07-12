package com.yucheng.cmis.biz01line.cus.cusbase.domain;

/**
 * Title: CusBlkList.java Description:
 * 
 * @author：echow heyc@yuchengtech.com
 * @create date：Thu May 28 17:12:00 CST 2009
 * @version：1.0
 */
public class CusBlkList implements com.yucheng.cmis.pub.CMISDomain {
	private String serno;
	private String blackDate;
	private String cusName;
	private String certType;
	private String certCode;
	private String blackType;
	private String blackLevel;
	private String dataSource;
	private String legalName;
	private String legalPhone;
	private String legalAddr;
	private String blackReason;
	private String logoutReason;
	private String logoutDate;
	private String status;
	private String inputId;
	private String inputBrId;
	private String inputDate;
	private String logoutId;
	private String logoutBrId;
	private String apprId;
	private String apprBrId;
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
	 * @return 返回 dataSource
	 */
	public String getDataSource() {
		return dataSource;
	}

	/**
	 * @设置 dataSource
	 * @param dataSource
	 */
	public void setDataSource(String dataSource) {
		this.dataSource = dataSource;
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
	 * @return 返回 logoutDate
	 */
	public String getLogoutDate() {
		return logoutDate;
	}

	/**
	 * @设置 logoutDate
	 * @param logoutDate
	 */
	public void setLogoutDate(String logoutDate) {
		this.logoutDate = logoutDate;
	}

	/**
	 * @return 返回 status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @设置 status
	 * @param status
	 */
	public void setStatus(String status) {
		this.status = status;
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

	/**
	 * @return 返回 logoutId
	 */
	public String getLogoutId() {
		return logoutId;
	}

	/**
	 * @设置 logoutId
	 * @param logoutId
	 */
	public void setLogoutId(String logoutId) {
		this.logoutId = logoutId;
	}

	/**
	 * @return 返回 logoutBrId
	 */
	public String getLogoutBrId() {
		return logoutBrId;
	}

	/**
	 * @设置 logoutBrId
	 * @param logoutBrId
	 */
	public void setLogoutBrId(String logoutBrId) {
		this.logoutBrId = logoutBrId;
	}

	/**
	 * @return 返回 apprId
	 */
	public String getApprId() {
		return apprId;
	}

	/**
	 * @设置 apprId
	 * @param apprId
	 */
	public void setApprId(String apprId) {
		this.apprId = apprId;
	}

	/**
	 * @return 返回 apprBrId
	 */
	public String getApprBrId() {
		return apprBrId;
	}

	/**
	 * @设置 apprBrId
	 * @param apprBrId
	 */
	public void setApprBrId(String apprBrId) {
		this.apprBrId = apprBrId;
	}
}