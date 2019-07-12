package com.yucheng.cmis.biz01line.cus.cuscom.domain;

/**
 * Title: CusComRelInvest.java Description:
 * 
 * @author：echow heyc@yuchengtech.com
 * @create date：Sat May 09 15:05:20 CST 2009
 * @version：1.0
 */
public class CusCliRel implements com.yucheng.cmis.pub.CMISDomain {
	private String cusid;
	private String custyperel;
	private String cusnamerel;
	private String cusidrel;
	private String certtype;
	private String certcode;
	
	

	public String getCerttype() {
		return certtype;
	}



	public void setCerttype(String certtype) {
		this.certtype = certtype;
	}



	public String getCertcode() {
		return certcode;
	}



	public void setCertcode(String certcode) {
		this.certcode = certcode;
	}



	public String getCusid() {
		return cusid;
	}



	public void setCusid(String cusid) {
		this.cusid = cusid;
	}



	public String getCustyperel() {
		return custyperel;
	}



	public void setCustyperel(String custyperel) {
		this.custyperel = custyperel;
	}



	public String getCusnamerel() {
		return cusnamerel;
	}



	public void setCusnamerel(String cusnamerel) {
		this.cusnamerel = cusnamerel;
	}



	public String getCusidrel() {
		return cusidrel;
	}



	public void setCusidrel(String cusidrel) {
		this.cusidrel = cusidrel;
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