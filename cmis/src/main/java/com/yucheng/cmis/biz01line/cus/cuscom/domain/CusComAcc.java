package com.yucheng.cmis.biz01line.cus.cuscom.domain;

public class CusComAcc implements com.yucheng.cmis.pub.CMISDomain {

	String cusId;  //客户码
	String cusName; //  客户日期
	String accNo; // 账号
	String accName;//帐户名称
	String accDate; // 开户日期
	String accOpenOrg; // 开户机构
	String accOpenOrgname; // 开户机构名称
	String accOrg; // 核算机构
	String accOrgname; // 核算机构名称
	String accType; //账户类型
    
	
	

	public String getAccType() {
		return accType;
	}



	public void setAccType(String accType) {
		this.accType = accType;
	}



	public String getAccName() {
		return accName;
	}



	public void setAccName(String accName) {
		this.accName = accName;
	}



	public String getCusId() {
		return cusId;
	}



	public void setCusId(String cusId) {
		this.cusId = cusId;
	}



	public String getCusName() {
		return cusName;
	}



	public void setCusName(String cusName) {
		this.cusName = cusName;
	}



	public String getAccNo() {
		return accNo;
	}



	public void setAccNo(String accNo) {
		this.accNo = accNo;
	}



	public String getAccDate() {
		return accDate;
	}



	public void setAccDate(String accDate) {
		this.accDate = accDate;
	}



	public String getAccOpenOrg() {
		return accOpenOrg;
	}



	public void setAccOpenOrg(String accOpenOrg) {
		this.accOpenOrg = accOpenOrg;
	}



	public String getAccOpenOrgname() {
		return accOpenOrgname;
	}



	public void setAccOpenOrgname(String accOpenOrgname) {
		this.accOpenOrgname = accOpenOrgname;
	}



	public String getAccOrg() {
		return accOrg;
	}



	public void setAccOrg(String accOrg) {
		this.accOrg = accOrg;
	}



	public String getAccOrgname() {
		return accOrgname;
	}



	public void setAccOrgname(String accOrgname) {
		this.accOrgname = accOrgname;
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
}
