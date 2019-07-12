package com.yucheng.cmis.biz01line.fnc.detail.domain;


import com.yucheng.cmis.biz01line.cus.cuscom.domain.CusCom;
import com.yucheng.cmis.pub.CMISDomain;

/**
 *@Classname	FncInventory.java
 *@Version 1.0	
 *@Since   1.0 	2008-10-7 下午01:47:42  
 *@Copyright 	2008 yucheng Co. Ltd.
 *@Author 		biwq
 *@Description：
 *@Lastmodified 
 *@Author
 */

public class FncInventory implements CMISDomain{
	
	private CusCom cusCom ; //客户基本信息
	private int seq ; //序号
	private String cusId ; //客户代码
	private String fncYm ; //年月
	private String fncTyp ; //报表周期类型
	private String fncInvyName ; //存货名称
	private String fncPrcTyp ; //计价方式
	private String fncInvyTyp ; // 存货种类
	private int fncInvyAmt ; //数量
	private double fncInvyVal ; //价值
	private String fncInvyDt ; //库存日期
	private String regBchId ; //登记机构
	private String crtUsrId ; //登记人
	private String crtDt ; //登记日期
	private String lastUpdTm ; //更新时间
	private String editUpdUsrId ; //当前编辑人
	private String txBchId ; //主办机构
	private String mngBchId ; //管理机构
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
	public int getFncInvyAmt() {
		return fncInvyAmt;
	}
	public void setFncInvyAmt(int fncInvyAmt) {
		this.fncInvyAmt = fncInvyAmt;
	}
	public String getFncInvyDt() {
		return fncInvyDt;
	}
	public void setFncInvyDt(String fncInvyDt) {
		this.fncInvyDt = fncInvyDt;
	}
	public String getFncInvyName() {
		return fncInvyName;
	}
	public void setFncInvyName(String fncInvyName) {
		this.fncInvyName = fncInvyName;
	}
	public String getFncInvyTyp() {
		return fncInvyTyp;
	}
	public void setFncInvyTyp(String fncInvyTyp) {
		this.fncInvyTyp = fncInvyTyp;
	}
	public double getFncInvyVal() {
		return fncInvyVal;
	}
	public void setFncInvyVal(double fncInvyVal) {
		this.fncInvyVal = fncInvyVal;
	}
	public String getFncPrcTyp() {
		return fncPrcTyp;
	}
	public void setFncPrcTyp(String fncPrcTyp) {
		this.fncPrcTyp = fncPrcTyp;
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