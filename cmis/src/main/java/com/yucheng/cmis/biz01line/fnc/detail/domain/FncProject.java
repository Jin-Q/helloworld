package com.yucheng.cmis.biz01line.fnc.detail.domain;


import com.yucheng.cmis.biz01line.cus.cuscom.domain.CusCom;
import com.yucheng.cmis.pub.CMISDomain;


public class FncProject implements CMISDomain {
	
	private CusCom cusCom;		//	客户基本信息
	private String cusId;		//	客户代码
	private String fncYm;		//	年月
	private String fncTyp;		//	报表周期类型
	private int seq;	//	序号
	private String fncProName;		//	项目名称
	private String fncConstLoc;		//	施工地点
	private String fncConstDepnt;		//	施工单位
	private double fncInvtAmt;		//	投资额
	private double fncInvtAmted;		//	已完成投资额
	private String fncPrg;		//	形象进度
	private String remark;	//	备注
	private String regBchId;	//	登记机构
	private String crtUsrId;	//	登记人
	private String crtDt;	//	登记日期
	private String lastUpdTm;	//	更新时间        
	private String editUpdUsrId;	//	当前编辑人    
	private String txBchId ; //	主办机构          
	private String 	mngBchId;	//	管理机构    
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
	public String getFncConstDepnt() {
		return fncConstDepnt;
	}
	public void setFncConstDepnt(String fncConstDepnt) {
		this.fncConstDepnt = fncConstDepnt;
	}
	public String getFncConstLoc() {
		return fncConstLoc;
	}
	public void setFncConstLoc(String fncConstLoc) {
		this.fncConstLoc = fncConstLoc;
	}
	public double getFncInvtAmt() {
		return fncInvtAmt;
	}
	public void setFncInvtAmt(double fncInvtAmt) {
		this.fncInvtAmt = fncInvtAmt;
	}
	public double getFncInvtAmted() {
		return fncInvtAmted;
	}
	public void setFncInvtAmted(double fncInvtAmted) {
		this.fncInvtAmted = fncInvtAmted;
	}
	public String getFncPrg() {
		return fncPrg;
	}
	public void setFncPrg(String fncPrg) {
		this.fncPrg = fncPrg;
	}
	public String getFncProName() {
		return fncProName;
	}
	public void setFncProName(String fncProName) {
		this.fncProName = fncProName;
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
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
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
