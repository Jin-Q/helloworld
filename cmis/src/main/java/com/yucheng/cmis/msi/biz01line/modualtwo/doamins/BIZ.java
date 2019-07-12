package com.yucheng.cmis.msi.biz01line.modualtwo.doamins;

/**
 * <p>
 * 	业务基础信息Domain
 * 	<ul>描述：
 * 			modualtwo模块接口需要使用的domain
 * 	</ul>
 * </p>
 * @author yuhq
 * @version 3.0
 * @since 3.0
 *
 */
public class BIZ {

	private String id = null;
	private String bizName = null;
	private String bizCode = null;
	private String bizAmt = null;
	private String bizTerm = null;
	private String cusId = null;
	private String cusName = null;
	
	/**
	 * 获取业务流水号
	 * @return　业务流水号
	 */
	public String getId() {
		return id;
	}
	
	/**
	 * 设置业务流水号
	 * @param id 业务流水号
	 */
	public void setId(String id) {
		this.id = id;
	}
	
	/**
	 * 获取业务品种
	 * @return　业务品种
	 */
	public String getBizName() {
		return bizName;
	}
	
	/**
	 * 设置业务品种
	 * @param bizName 业务品种
	 */
	public void setBizName(String bizName) {
		this.bizName = bizName;
	}
	
	/**
	 * 设置业务类型
	 * @return　业务类型
	 */
	public String getBizCode() {
		return bizCode;
	}
	
	/**
	 * 业务类型
	 * @param bizCode 业务类型
	 */
	public void setBizCode(String bizCode) {
		this.bizCode = bizCode;
	}
	
	/**
	 * 获取请金额
	 * @return　申请金额
	 */
	public String getBizAmt() {
		return bizAmt;
	}
	
	/**
	 * 设置申请金额
	 * @param bizAmt 申请金额
	 */
	public void setBizAmt(String bizAmt) {
		this.bizAmt = bizAmt;
	}
	
	/**
	 * 获取申请期限
	 * @return 申请期限
	 */
	public String getBizTerm() {
		return bizTerm;
	}
	
	/**
	 * 设置申请期限
	 * @param bizTerm　申请期限
	 */
	public void setBizTerm(String bizTerm) {
		this.bizTerm = bizTerm;
	}
	
	/**
	 * 获取客户码
	 * @return 客户码
	 */
	public String getCusId() {
		return cusId;
	}
	
	/**
	 * 设置客户码
	 * @param cusId　客户码
	 */
	public void setCusId(String cusId) {
		this.cusId = cusId;
	}
	
	/**
	 * 获取客户名称
	 * @return 客户名称
	 */
	public String getCusName() {
		return cusName;
	}
	
	/**
	 * 设置客户名称
	 * @param cusName 客户名称
	 */
	public void setCusName(String cusName) {
		this.cusName = cusName;
	}
	
}
