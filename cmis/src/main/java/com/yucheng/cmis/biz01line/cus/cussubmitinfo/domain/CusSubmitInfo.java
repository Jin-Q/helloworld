package com.yucheng.cmis.biz01line.cus.cussubmitinfo.domain;

import com.yucheng.cmis.pub.CMISDomain;

public class CusSubmitInfo implements CMISDomain{
	
	private String serno ;  //流水号
	private String cusId;	//客户编号
	private String memo;		//提示信息
	private String handoverId;	//移出人
	private String rcverId;	//接收人
	private String inputDate;	//录入日期
	private String endFlag;		//完成标志(0.完成 1.未完成)
	private String oprTime;		//具体时间
	private String oprType;		//操作类型(1.提交 2.打回 3.移交)
	
	public String getSerno() {
		return serno;
	}

	public void setSerno(String serno) {
		this.serno = serno;
	}

	public String getCusId() {
		return cusId;
	}

	public void setCusId(String cusId) {
		this.cusId = cusId;
	}

	public String getHandoverId() {
		return handoverId;
	}

	public void setHandoverId(String handoverId) {
		this.handoverId = handoverId;
	}

	public String getInputDate() {
		return inputDate;
	}

	public void setInputDate(String inputDate) {
		this.inputDate = inputDate;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getRcverId() {
		return rcverId;
	}

	public void setRcverId(String rcverId) {
		this.rcverId = rcverId;
	}

	public String getEndFlag() {
		return endFlag;
	}

	public void setEndFlag(String endFlag) {
		this.endFlag = endFlag;
	}

	public String getOprTime() {
		return oprTime;
	}

	public void setOprTime(String oprTime) {
		this.oprTime = oprTime;
	}

	public String getOprType() {
		return oprType;
	}

	public void setOprType(String oprType) {
		this.oprType = oprType;
	}

	public Object clone() throws CloneNotSupportedException {
		// call父类的clone方法
		Object result = super.clone();
		// TODO: 定制clone数据

		return result;

	}
}
