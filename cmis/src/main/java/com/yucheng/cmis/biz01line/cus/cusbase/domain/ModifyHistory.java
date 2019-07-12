package com.yucheng.cmis.biz01line.cus.cusbase.domain;

import com.yucheng.cmis.pub.CMISDomain;

public class ModifyHistory implements CMISDomain {
	private String keyId; 
	private String tableName;
	private String cusId;
	private String modifyRecord; 
	private String modifyTime; 
	private String modifyUserId; 
	private String modifyUserBrId; 
	private String modifyUserIp; 
	private String modifyStatus; 
	
	/**
	 * @调用父类clone方法
	 * @return Object
	 * @throws CloneNotSupportedException
	 */
	public Object clone()throws CloneNotSupportedException{
		Object result = super.clone();
		return result;
	}

	public String getKeyId() {
		return keyId;
	}

	public void setKeyId(String keyId) {
		this.keyId = keyId;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getCusId() {
		return cusId;
	}

	public void setCusId(String cusId) {
		this.cusId = cusId;
	}

	public String getModifyRecord() {
		return modifyRecord;
	}

	public void setModifyRecord(String modifyRecord) {
		this.modifyRecord = modifyRecord;
	}

	public String getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}

	public String getModifyUserId() {
		return modifyUserId;
	}

	public void setModifyUserId(String modifyUserId) {
		this.modifyUserId = modifyUserId;
	}

	public String getModifyUserIp() {
		return modifyUserIp;
	}

	public void setModifyUserIp(String modifyUserIp) {
		this.modifyUserIp = modifyUserIp;
	}

	public String getModifyStatus() {
		return modifyStatus;
	}

	public void setModifyStatus(String modifyStatus) {
		this.modifyStatus = modifyStatus;
	}

	public String getModifyUserBrId() {
		return modifyUserBrId;
	}

	public void setModifyUserBrId(String modifyUserBrId) {
		this.modifyUserBrId = modifyUserBrId;
	}
}
