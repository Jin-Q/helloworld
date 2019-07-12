package com.yucheng.cmis.biz01line.cus.cuscom.domain;

import com.yucheng.cmis.pub.CMISDomain;

public class CusBlackList  implements CMISDomain{
	private CusCom cusCom;

	private String cusId;
	
	private int seq;

	private String blkTyp;

	private String blkDt;

	private String blkReason;

	private String blkCnlFlg;

	private String crtBchId;

	private String crtUsrId;

	private String crtDt;

	private String blkCnlBchId;

	private String blkCnlUsrId;

	private String blkCnlDt;

	public String getBlkCnlBchId() {
		return blkCnlBchId;
	}

	public void setBlkCnlBchId(String blkCnlBchId) {
		this.blkCnlBchId = blkCnlBchId;
	}

	public String getBlkCnlDt() {
		return blkCnlDt;
	}

	public void setBlkCnlDt(String blkCnlDt) {
		this.blkCnlDt = blkCnlDt;
	}

	public String getBlkCnlFlg() {
		return blkCnlFlg;
	}
   
	public int getSeq(){
		return seq;
	}
	
	public  void setSeq(int seq){
		this.seq = seq;
	}
	
	
	public void setBlkCnlFlg(String blkCnlFlg) {
		this.blkCnlFlg = blkCnlFlg;
	}

	public String getBlkCnlUsrId() {
		return blkCnlUsrId;
	}

	public void setBlkCnlUsrId(String blkCnlUsrId) {
		this.blkCnlUsrId = blkCnlUsrId;
	}

	public String getBlkDt() {
		return blkDt;
	}

	public void setBlkDt(String blkDt) {
		this.blkDt = blkDt;
	}

	public String getBlkReason() {
		return blkReason;
	}

	public void setBlkReason(String blkReason) {
		this.blkReason = blkReason;
	}

	public String getBlkTyp() {
		return blkTyp;
	}

	public void setBlkTyp(String blkTyp) {
		this.blkTyp = blkTyp;
	}

	public String getCrtBchId() {
		return crtBchId;
	}

	public void setCrtBchId(String crtBchId) {
		this.crtBchId = crtBchId;
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
	public/*protected*/ Object clone() throws CloneNotSupportedException { 

		// call父类的clone方法 

		Object result = super.clone(); 



		//TODO: 定制clone数据 

		return result; 

		} 

}
