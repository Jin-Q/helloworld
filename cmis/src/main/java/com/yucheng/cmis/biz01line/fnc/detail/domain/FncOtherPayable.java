package com.yucheng.cmis.biz01line.fnc.detail.domain;

import com.yucheng.cmis.biz01line.cus.cuscom.domain.CusCom;
import com.yucheng.cmis.pub.CMISDomain;


public class FncOtherPayable implements CMISDomain {
	
	//客户基本信息
	private CusCom cusCom;
	//客户代码
	private String cusId;
	//年月
	private String fncYm;
	//报表周期类型
	private String fncTyp;
	//序号
	private int seq;
	//对方客户代码
	private String fncConCusId;
	//货币种类
	private String fncCurTyp;
	//应付款余额合计
	private double fncSumAmt;
	//即期应付款
	private double fncImmTrmAmt;
	//短期应付款
	private double fncShortTrmAmt;
	//中期应付款
	private double fncInterTrmAmt;
	//长期应付款
	private double fncLongTrmAmt;
	//是否关联企业
	private String relFlg;
	//登记机构
	private String regBchId;
	//登记人
	private String crtUsrId;
	//登记日期
	private String crtDt;
	//更新时间
	private String lastUpdTm;
	//当前编辑人
	private String editUpdUsrId;
	//主办机构
	private String txBchId;
	//管理机构
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
	public double getFncImmTrmAmt() {
		return fncImmTrmAmt;
	}
	public void setFncImmTrmAmt(double fncImmTrmAmt) {
		this.fncImmTrmAmt = fncImmTrmAmt;
	}
	public double getFncInterTrmAmt() {
		return fncInterTrmAmt;
	}
	public void setFncInterTrmAmt(double fncInterTrmAmt) {
		this.fncInterTrmAmt = fncInterTrmAmt;
	}
	public double getFncLongTrmAmt() {
		return fncLongTrmAmt;
	}
	public void setFncLongTrmAmt(double fncLongTrmAmt) {
		this.fncLongTrmAmt = fncLongTrmAmt;
	}
	public double getFncShortTrmAmt() {
		return fncShortTrmAmt;
	}
	public void setFncShortTrmAmt(double fncShortTrmAmt) {
		this.fncShortTrmAmt = fncShortTrmAmt;
	}
	public double getFncSumAmt() {
		return fncSumAmt;
	}
	public void setFncSumAmt(double fncSumAmt) {
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
