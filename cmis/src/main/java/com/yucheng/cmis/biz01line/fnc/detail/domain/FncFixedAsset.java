package com.yucheng.cmis.biz01line.fnc.detail.domain;


import com.yucheng.cmis.biz01line.cus.cuscom.domain.CusCom;
import com.yucheng.cmis.pub.CMISDomain;

/**
 *@Classname	FncFixedAsset.java
 *@Version 1.0	
 *@Since   1.0 	2008-10-7 下午02:12:07  
 *@Copyright 	2008 yucheng Co. Ltd.
 *@Author 		biwq
 *@Description：
 *@Lastmodified 
 *@Author
 */

public class FncFixedAsset implements CMISDomain{
	private CusCom cusCom	; //	客户基本信息
	private String cusId	;	//	客户代码
	private String fncYm ;	//	年月
	private String fncTyp;	//	报表周期类型
	private int seq ;	//	序号
	private String fncInvtToward	;	//	投向
	private String fncAssetName;
	private int fncAssetAmt;
	private String fncAssetPlace;
	private String fncAssetWrrId;
	private int fncAssetNetVal;
	private String fncAssetObtMth;
	private String fncAssetPldDesc;
	private String fncAssetPurcDt;
	//private double fncInvtAmt ;	//	投入金额
	//private double fncCurVal ;	//	现价值
	//private double fncLastInvtIncm;	//	上年投资收益
	//private double fncAvgInvtIncm;	//	平均投资收益
	private String fncPriceTyp;	//	计价方法
	private String fncInvtDt	;	//	投资日期
	private String regBchId	;	//	登记机构
	private String crtUsrId ;	//	登记人
	private String crtDt	;	//	登记日期
	private String lastUpdTm;	//	更新时间
	private String editUpdUsrId;	//	当前编辑人
	private String txBchId;	//	主办机构
	private String mngBchId;	//	管理机构
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

	public String getFncInvtDt() {
		return fncInvtDt;
	}
	public void setFncInvtDt(String fncInvtDt) {
		this.fncInvtDt = fncInvtDt;
	}
	public String getFncInvtToward() {
		return fncInvtToward;
	}
	public void setFncInvtToward(String fncInvtToward) {
		this.fncInvtToward = fncInvtToward;
	}
	public String getFncPriceTyp() {
		return fncPriceTyp;
	}
	public void setFncPriceTyp(String fncPriceTyp) {
		this.fncPriceTyp = fncPriceTyp;
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
	public int getFncAssetAmt() {
		return fncAssetAmt;
	}
	public void setFncAssetAmt(int fncAssetAmt) {
		this.fncAssetAmt = fncAssetAmt;
	}
	public String getFncAssetName() {
		return fncAssetName;
	}
	public void setFncAssetName(String fncAssetName) {
		this.fncAssetName = fncAssetName;
	}
	public int getFncAssetNetVal() {
		return fncAssetNetVal;
	}
	public void setFncAssetNetVal(int fncAssetNetVal) {
		this.fncAssetNetVal = fncAssetNetVal;
	}
	public String getFncAssetObtMth() {
		return fncAssetObtMth;
	}
	public void setFncAssetObtMth(String fncAssetObtMth) {
		this.fncAssetObtMth = fncAssetObtMth;
	}
	public String getFncAssetPlace() {
		return fncAssetPlace;
	}
	public void setFncAssetPlace(String fncAssetPlace) {
		this.fncAssetPlace = fncAssetPlace;
	}
	public String getFncAssetPldDesc() {
		return fncAssetPldDesc;
	}
	public void setFncAssetPldDesc(String fncAssetPldDesc) {
		this.fncAssetPldDesc = fncAssetPldDesc;
	}
	public String getFncAssetPurcDt() {
		return fncAssetPurcDt;
	}
	public void setFncAssetPurcDt(String fncAssetPurcDt) {
		this.fncAssetPurcDt = fncAssetPurcDt;
	}
	public String getFncAssetWrrId() {
		return fncAssetWrrId;
	}
	public void setFncAssetWrrId(String fncAssetWrrId) {
		this.fncAssetWrrId = fncAssetWrrId;
	}
	
	public/*protected*/ Object clone() throws CloneNotSupportedException { 

		// call父类的clone方法 

		Object result = super.clone(); 



		//TODO: 定制clone数据 

		return result; 

		} 

}
