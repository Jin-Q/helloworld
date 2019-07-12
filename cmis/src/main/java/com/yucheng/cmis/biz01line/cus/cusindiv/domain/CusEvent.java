package com.yucheng.cmis.biz01line.cus.cusindiv.domain;

import java.util.Date;

import com.yucheng.cmis.pub.CMISDomain;

//客户大事记表
public class CusEvent  implements CMISDomain{
	private CusIndiv cusIndiv;
	
	private double seq;

	private String cusId;

	private Date eventTm;

	private String eventTyp;

	private String eventKind;

	private String eventName;

	private String eventDesc;

	private double eventAmt;

	private String eventExpsCom;

	private String eventBankFlg;

	private String eventBchName;

	private String validFlg;

	private String cusSt;

	private String regBchId;

	private String crtUsrId;

	private String crtDt;

	private String lastUpdTm;

	private String editUpdUsrId;

	private String txBchId;

	private String mngBchId;

	public String getCrtDt() {
		return crtDt;
	}

	public void setCrtDt(String crtDt) {
		this.crtDt = crtDt;
	}

	public String getCrtUsrId() {
		return crtUsrId;
	}

	public void setCrtUsrId(String crtUsrId) {
		this.crtUsrId = crtUsrId;
	}

	public String getCusId() {
		return cusId;
	}

	public void setCusId(String cusId) {
		this.cusId = cusId;
	}

	public String getCusSt() {
		return cusSt;
	}

	public void setCusSt(String cusSt) {
		this.cusSt = cusSt;
	}

	public String getEditUpdUsrId() {
		return editUpdUsrId;
	}

	public void setEditUpdUsrId(String editUpdUsrId) {
		this.editUpdUsrId = editUpdUsrId;
	}

	public double getEventAmt() {
		return eventAmt;
	}

	public void setEventAmt(double eventAmt) {
		this.eventAmt = eventAmt;
	}

	public String getEventBankFlg() {
		return eventBankFlg;
	}

	public void setEventBankFlg(String eventBankFlg) {
		this.eventBankFlg = eventBankFlg;
	}

	public String getEventBchName() {
		return eventBchName;
	}

	public void setEventBchName(String eventBchName) {
		this.eventBchName = eventBchName;
	}

	public String getEventDesc() {
		return eventDesc;
	}

	public void setEventDesc(String eventDesc) {
		this.eventDesc = eventDesc;
	}

	public String getEventExpsCom() {
		return eventExpsCom;
	}

	public void setEventExpsCom(String eventExpsCom) {
		this.eventExpsCom = eventExpsCom;
	}

	public String getEventKind() {
		return eventKind;
	}

	public void setEventKind(String eventKind) {
		this.eventKind = eventKind;
	}

	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	public Date getEventTm() {
		return eventTm;
	}

	public void setEventTm(Date eventTm) {
		this.eventTm = eventTm;
	}

	public String getEventTyp() {
		return eventTyp;
	}

	public void setEventTyp(String eventTyp) {
		this.eventTyp = eventTyp;
	}

	public String getLastUpdTm() {
		return lastUpdTm;
	}

	public void setLastUpdTm(String lastUpdTm) {
		this.lastUpdTm = lastUpdTm;
	}

	public String getMngBchId() {
		return mngBchId;
	}

	public void setMngBchId(String mngBchId) {
		this.mngBchId = mngBchId;
	}

	public String getRegBchId() {
		return regBchId;
	}

	public void setRegBchId(String regBchId) {
		this.regBchId = regBchId;
	}

	public double getSeq() {
		return seq;
	}

	public void setSeq(double seq) {
		this.seq = seq;
	}

	public String getTxBchId() {
		return txBchId;
	}

	public void setTxBchId(String txBchId) {
		this.txBchId = txBchId;
	}

	public String getValidFlg() {
		return validFlg;
	}

	public void setValidFlg(String validFlg) {
		this.validFlg = validFlg;
	}

	

	public CusIndiv getCusIndiv() {
		return cusIndiv;
	}

	public void setCusIndiv(CusIndiv cusIndiv) {
		this.cusIndiv = cusIndiv;
	}
	
	public/*protected*/ Object clone() throws CloneNotSupportedException { 

		// call父类的clone方法 

		Object result = super.clone(); 



		//TODO: 定制clone数据 

		return result; 

		} 

}
