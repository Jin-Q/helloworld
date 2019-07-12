package com.yucheng.cmis.biz01line.cus.cusindiv.domain;
/**
 * Title: CusIndiv.java
 * Description: 
 * @author：echow	heyc@yuchengtech.com
 * @create date：Mon Apr 13 19:20:11 CST 2009
 * @version：1.0
 */
public class CusIndiv  implements com.yucheng.cmis.pub.CMISDomain{
	private String cusId;
	private String indivSex;
	private String indivIdExpDt;
	private String agriFlg;
	private double comHoldStkAmt;
	private String bankDuty;
	private String indivNtn;
	private String indivBrtPlace;
	private String indivHouhRegAdd;
	private String indivDtOfBirth;
	private String indivPolSt;
	private String indivEdt;
	private String indivDgr;
	private String postAddr;
	private String postCode;
	private String areaCode;
	private String areaName;
	private String phone;
	private String fphone;
	private String mobile;
	private String faxCode;
	private String email;
	private String indivRsdAddr;
	private String indivZipCode;
	private String indivRsdSt;
	private String indivHobby;
	private String indivOcc;
	private String indivComName;
	private String indivComTyp;
	private String indivComFld;
	private String indivComPhn;
	private String indivComFax;
	private String indivComAddr;
	private String indivComZipCode;
	private String indivComCntName;
	private String indivWorkJobY;
	private String indivComJobTtl;
	private String indivCrtfctn;
	private double indivAnnIncm;
	private String indivSalAccBank;
	private String indivSalAccNo;
	private String comRelDgr;
	private String comInitLoanDate;
	private String indivHldAcnt;
	private String holdCard;
	private String passportFlg;
	private String remark;
	private String indivComFldName;
	private String workResume;
	private String stockholdCode;
	private String isRelaCust;
	private String indivIdStartDt;
	
	public String getIndivIdStartDt() {
		return indivIdStartDt;
	}

	public void setIndivIdStartDt(String indivIdStartDt) {
		this.indivIdStartDt = indivIdStartDt;
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

	public String getCusId() {
		return cusId;
	}

	public void setCusId(String cusId) {
		this.cusId = cusId;
	}

	public String getIndivSex() {
		return indivSex;
	}

	public void setIndivSex(String indivSex) {
		this.indivSex = indivSex;
	}

	public String getIndivIdExpDt() {
		return indivIdExpDt;
	}

	public void setIndivIdExpDt(String indivIdExpDt) {
		this.indivIdExpDt = indivIdExpDt;
	}

	public String getAgriFlg() {
		return agriFlg;
	}

	public void setAgriFlg(String agriFlg) {
		this.agriFlg = agriFlg;
	}

	public double getComHoldStkAmt() {
		return comHoldStkAmt;
	}

	public void setComHoldStkAmt(double comHoldStkAmt) {
		this.comHoldStkAmt = comHoldStkAmt;
	}

	public String getBankDuty() {
		return bankDuty;
	}

	public void setBankDuty(String bankDuty) {
		this.bankDuty = bankDuty;
	}

	public String getIndivNtn() {
		return indivNtn;
	}

	public void setIndivNtn(String indivNtn) {
		this.indivNtn = indivNtn;
	}

	public String getIndivBrtPlace() {
		return indivBrtPlace;
	}

	public void setIndivBrtPlace(String indivBrtPlace) {
		this.indivBrtPlace = indivBrtPlace;
	}

	public String getIndivHouhRegAdd() {
		return indivHouhRegAdd;
	}

	public void setIndivHouhRegAdd(String indivHouhRegAdd) {
		this.indivHouhRegAdd = indivHouhRegAdd;
	}

	public String getIndivDtOfBirth() {
		return indivDtOfBirth;
	}

	public void setIndivDtOfBirth(String indivDtOfBirth) {
		this.indivDtOfBirth = indivDtOfBirth;
	}

	public String getIndivPolSt() {
		return indivPolSt;
	}

	public void setIndivPolSt(String indivPolSt) {
		this.indivPolSt = indivPolSt;
	}

	public String getIndivEdt() {
		return indivEdt;
	}

	public void setIndivEdt(String indivEdt) {
		this.indivEdt = indivEdt;
	}

	public String getIndivDgr() {
		return indivDgr;
	}

	public void setIndivDgr(String indivDgr) {
		this.indivDgr = indivDgr;
	}

	public String getPostAddr() {
		return postAddr;
	}

	public void setPostAddr(String postAddr) {
		this.postAddr = postAddr;
	}

	public String getPostCode() {
		return postCode;
	}

	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getFphone() {
		return fphone;
	}

	public void setFphone(String fphone) {
		this.fphone = fphone;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getFaxCode() {
		return faxCode;
	}

	public void setFaxCode(String faxCode) {
		this.faxCode = faxCode;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getIndivRsdAddr() {
		return indivRsdAddr;
	}

	public void setIndivRsdAddr(String indivRsdAddr) {
		this.indivRsdAddr = indivRsdAddr;
	}

	public String getIndivZipCode() {
		return indivZipCode;
	}

	public void setIndivZipCode(String indivZipCode) {
		this.indivZipCode = indivZipCode;
	}

	public String getIndivRsdSt() {
		return indivRsdSt;
	}

	public void setIndivRsdSt(String indivRsdSt) {
		this.indivRsdSt = indivRsdSt;
	}

	public String getIndivHobby() {
		return indivHobby;
	}

	public void setIndivHobby(String indivHobby) {
		this.indivHobby = indivHobby;
	}

	public String getIndivOcc() {
		return indivOcc;
	}

	public void setIndivOcc(String indivOcc) {
		this.indivOcc = indivOcc;
	}

	public String getIndivComName() {
		return indivComName;
	}

	public void setIndivComName(String indivComName) {
		this.indivComName = indivComName;
	}

	public String getIndivComTyp() {
		return indivComTyp;
	}

	public void setIndivComTyp(String indivComTyp) {
		this.indivComTyp = indivComTyp;
	}

	public String getIndivComFld() {
		return indivComFld;
	}

	public void setIndivComFld(String indivComFld) {
		this.indivComFld = indivComFld;
	}

	public String getIndivComPhn() {
		return indivComPhn;
	}

	public void setIndivComPhn(String indivComPhn) {
		this.indivComPhn = indivComPhn;
	}

	public String getIndivComFax() {
		return indivComFax;
	}

	public void setIndivComFax(String indivComFax) {
		this.indivComFax = indivComFax;
	}

	public String getIndivComAddr() {
		return indivComAddr;
	}

	public void setIndivComAddr(String indivComAddr) {
		this.indivComAddr = indivComAddr;
	}

	public String getIndivComZipCode() {
		return indivComZipCode;
	}

	public void setIndivComZipCode(String indivComZipCode) {
		this.indivComZipCode = indivComZipCode;
	}

	public String getIndivComCntName() {
		return indivComCntName;
	}

	public void setIndivComCntName(String indivComCntName) {
		this.indivComCntName = indivComCntName;
	}

	public String getIndivWorkJobY() {
		return indivWorkJobY;
	}

	public void setIndivWorkJobY(String indivWorkJobY) {
		this.indivWorkJobY = indivWorkJobY;
	}

	public String getIndivComJobTtl() {
		return indivComJobTtl;
	}

	public void setIndivComJobTtl(String indivComJobTtl) {
		this.indivComJobTtl = indivComJobTtl;
	}

	public String getIndivCrtfctn() {
		return indivCrtfctn;
	}

	public void setIndivCrtfctn(String indivCrtfctn) {
		this.indivCrtfctn = indivCrtfctn;
	}

	public double getIndivAnnIncm() {
		return indivAnnIncm;
	}

	public void setIndivAnnIncm(double indivAnnIncm) {
		this.indivAnnIncm = indivAnnIncm;
	}

	public String getIndivSalAccBank() {
		return indivSalAccBank;
	}

	public void setIndivSalAccBank(String indivSalAccBank) {
		this.indivSalAccBank = indivSalAccBank;
	}

	public String getIndivSalAccNo() {
		return indivSalAccNo;
	}

	public void setIndivSalAccNo(String indivSalAccNo) {
		this.indivSalAccNo = indivSalAccNo;
	}

	public String getComRelDgr() {
		return comRelDgr;
	}

	public void setComRelDgr(String comRelDgr) {
		this.comRelDgr = comRelDgr;
	}

	public String getComInitLoanDate() {
		return comInitLoanDate;
	}

	public void setComInitLoanDate(String comInitLoanDate) {
		this.comInitLoanDate = comInitLoanDate;
	}

	public String getIndivHldAcnt() {
		return indivHldAcnt;
	}

	public void setIndivHldAcnt(String indivHldAcnt) {
		this.indivHldAcnt = indivHldAcnt;
	}

	public String getHoldCard() {
		return holdCard;
	}

	public void setHoldCard(String holdCard) {
		this.holdCard = holdCard;
	}

	public String getPassportFlg() {
		return passportFlg;
	}

	public void setPassportFlg(String passportFlg) {
		this.passportFlg = passportFlg;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getIndivComFldName() {
		return indivComFldName;
	}

	public void setIndivComFldName(String indivComFldName) {
		this.indivComFldName = indivComFldName;
	}

	public String getWorkResume() {
		return workResume;
	}

	public void setWorkResume(String workResume) {
		this.workResume = workResume;
	}

	public String getStockholdCode() {
		return stockholdCode;
	}

	public void setStockholdCode(String stockholdCode) {
		this.stockholdCode = stockholdCode;
	}

	public String getIsRelaCust() {
		return isRelaCust;
	}

	public void setIsRelaCust(String isRelaCust) {
		this.isRelaCust = isRelaCust;
	}
	
}