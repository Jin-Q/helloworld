package com.yucheng.cmis.biz01line.fnc.detail.domain;


import com.yucheng.cmis.biz01line.cus.cuscom.domain.CusCom;
import com.yucheng.cmis.pub.CMISDomain;
  /**
 *@Classname	FncOthReceive.java
 *@Version 1.0	
 *@Since   1.0 	Oct 7, 2008 2:01:51 PM  
 *@Copyright 	yuchengtech
 *@Author 		Administrator
 *@Description：
 *@Lastmodified 
 *@Author
 */
public class FncOthReceive implements CMISDomain{
	private	CusCom	cusCom;	        //客户基本信息
	private	String	cusId;	        //客户代码
	private	String	fncYm;	        //年月
	private	String	fncTyp;	        //报表周期类型
	private	int	seq;	            //序号
	private	String	fncConCusId;    //对方客户代码
	private	String	fncCurTyp;	    //货币种类
	private	Double	fncSumAmt;	    //应收款余额合计
	private	Double	fncImmTrmAmt;	//即期应收款
	private	Double	fncShortTrmAmt;	//短期应收款
	private	Double	fncInterTrmAmt;	//中期应收款
	private	Double	fncLongTrmAmt;	//长期应收款
	private	String	relFlg;	        //是否关联企业
	private	String	regBchId;	    //登记机构
	private	String	crtUsrId;	    //登记人
	private	String	crtDt;	        //登记日期
	private	String	lastUpdTm;	    //更新时间
	private	String	editUpdUsrId;	//当前编辑人
	private	String	txBchId;	    //主办机构
	private	String	mngBchId;	    //管理机构
	private String guarSt;
	public String getGuarSt() {
		return guarSt;
	}
	public void setGuarSt(String guarSt) {
		this.guarSt = guarSt;
	}
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
	public CusCom getCusCom() {
		return cusCom;
	}
	public void setCusCom(CusCom cusCom) {
		this.cusCom = cusCom;
	}
	public String getCusId() {
		return cusId;
	}
	public void setCusId(String cusId) {
		this.cusId = cusId;
	}
	public String getEditUpdUsrId() {
		return editUpdUsrId;
	}
	public void setEditUpdUsrId(String editUpdUsrId) {
		this.editUpdUsrId = editUpdUsrId;
	}
	public String getFncConCusId() {
		return fncConCusId;
	}
	public void setFncConCusId(String fncConCusId) {
		this.fncConCusId = fncConCusId;
	}
	public String getFncCurTyp() {
		return fncCurTyp;
	}
	public void setFncCurTyp(String fncCurTyp) {
		this.fncCurTyp = fncCurTyp;
	}
	public Double getFncImmTrmAmt() {
		return fncImmTrmAmt;
	}
	public void setFncImmTrmAmt(Double fncImmTrmAmt) {
		this.fncImmTrmAmt = fncImmTrmAmt;
	}
	public Double getFncInterTrmAmt() {
		return fncInterTrmAmt;
	}
	public void setFncInterTrmAmt(Double fncInterTrmAmt) {
		this.fncInterTrmAmt = fncInterTrmAmt;
	}
	public Double getFncLongTrmAmt() {
		return fncLongTrmAmt;
	}
	public void setFncLongTrmAmt(Double fncLongTrmAmt) {
		this.fncLongTrmAmt = fncLongTrmAmt;
	}
	public Double getFncShortTrmAmt() {
		return fncShortTrmAmt;
	}
	public void setFncShortTrmAmt(Double fncShortTrmAmt) {
		this.fncShortTrmAmt = fncShortTrmAmt;
	}
	public Double getFncSumAmt() {
		return fncSumAmt;
	}
	public void setFncSumAmt(Double fncSumAmt) {
		this.fncSumAmt = fncSumAmt;
	}
	public String getFncTyp() {
		return fncTyp;
	}
	public void setFncTyp(String fncTyp) {
		this.fncTyp = fncTyp;
	}
	public String getFncYm() {
		return fncYm;
	}
	public void setFncYm(String fncYm) {
		this.fncYm = fncYm;
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
	public String getRelFlg() {
		return relFlg;
	}
	public void setRelFlg(String relFlg) {
		this.relFlg = relFlg;
	}
	public int getSeq() {
		return seq;
	}
	public void setSeq(int seq) {
		this.seq = seq;
	}
	public String getTxBchId() {
		return txBchId;
	}
	public void setTxBchId(String txBchId) {
		this.txBchId = txBchId;
	}

	public/*protected*/ Object clone() throws CloneNotSupportedException { 

		// call父类的clone方法 

		Object result = super.clone(); 



		//TODO: 定制clone数据 

		return result; 

		} 

}
