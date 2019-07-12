package com.yucheng.cmis.platform.workflow.domain;

/**
 * <p>流程消息实体类</p>
 * 设置的属性有：<br>
 * <li>消息ID
 * <li>申请类型
 * <li>流程实例号
 * <li>流程标识
 * <li>节点ID
 * <li>审批场景
 * <li>表模型ID
 * <li>业务表主键名
 * <li>业务主键值（一般为业务流水号）
 * <li>审查审批人
 * <li>审查审批机构
 * <li>审查审批结论（流程审批意见标识）
 * <li>消息处理状态
 * <li>操作时间
 * @author liuhw 2013-7-8
 */

public class WfiMsgQueue  implements com.yucheng.cmis.pub.CMISDomain{
	
	/**
	 * 消息ID
	 */
	private String msgid;
	
	/**
	 * 申请类型
	 */
	private String applType;
	
	/**
	 * 流程实例号
	 */
	private String instanceid;
	
	/**
	 * 流程标识
	 */
	private String wfsign;
	
	/**
	 * 节点ID
	 */
	private String nodeid;
	
	/**
	 * 审批场景
	 */
	private String scene;
	
	/**
	 * 表模型ID
	 */
	private String tableName;
	
	/**
	 * 业务表主键名
	 */
	private String pkCol;
	
	/**
	 * 业务申请号
	 */
	private String pkValue;
	
	/**
	 * 审查审批人
	 */
	private String userId;
	
	/**
	 * 审查审批机构
	 */
	private String orgId;
	
	/**
	 * 审查审批结论（流程审批意见标识）<br>
	 * 同意-10
	 * 否决-20
	 * 打回-30
	 * 退回-40
	 * 跳转-50
	 * 拿回-60
	 * 撤办-70
	 * 挂起-80
	 * 唤醒-90
	 * 转办-11
	 * 协办-21
	 * 催办-31
	 */
	private String wfiResult;
	
	/**
	 * 流程审批状态（待发起000、111审批中、991拿回、992打回、997审批通过、998审批否决）
	 */
	private String wfiStatus;
	
	/**
	 * 消息处理状态(初始尚未处理-00,处理中-01,回滚取消-70,异常-90,处理完毕-99)
	 */
	private String opstatus;
	
	/**
	 * 操作时间
	 */
	private String optime;
	
	/**
	 * @return 消息ID
	 */
	public String getMsgid() {
		return msgid;
	}

	/**
	 * @param 消息ID
	 */
	public void setMsgid(String msgid) {
		this.msgid = msgid;
	}

	/**
	 * @return 申请类型
	 */
	public String getApplType() {
		return applType;
	}

	/**
	 * @param 申请类型
	 */
	public void setApplType(String applType) {
		this.applType = applType;
	}

	/**
	 * @return 流程实例号
	 */
	public String getInstanceid() {
		return instanceid;
	}

	/**
	 * @param 流程实例号
	 */
	public void setInstanceid(String instanceid) {
		this.instanceid = instanceid;
	}

	/**
	 * @return 流程标识
	 */
	public String getWfsign() {
		return wfsign;
	}

	/**
	 * @param 流程标识
	 */
	public void setWfsign(String wfsign) {
		this.wfsign = wfsign;
	}

	/**
	 * @return 节点ID
	 */
	public String getNodeid() {
		return nodeid;
	}

	/**
	 * @param 节点ID
	 */
	public void setNodeid(String nodeid) {
		this.nodeid = nodeid;
	}

	/**
	 * @return 审批场景
	 */
	public String getScene() {
		return scene;
	}

	/**
	 * @param 审批场景
	 */
	public void setScene(String scene) {
		this.scene = scene;
	}

	/**
	 * @return 表模型ID
	 */
	public String getTableName() {
		return tableName;
	}

	/**
	 * @param 表模型ID
	 */
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	/**
	 * @return 业务表主键名
	 */
	public String getPkCol() {
		return pkCol;
	}

	/**
	 * @param 业务表主键名
	 */
	public void setPkCol(String pkCol) {
		this.pkCol = pkCol;
	}

	/**
	 * @return 业务申请号
	 */
	public String getPkValue() {
		return pkValue;
	}

	/**
	 * @param 业务申请号
	 */
	public void setPkValue(String pkValue) {
		this.pkValue = pkValue;
	}

	/**
	 * @return 审查审批人
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * @param 审查审批人
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * @return 审查审批机构
	 */
	public String getOrgId() {
		return orgId;
	}

	/**
	 * @param 审查审批机构
	 */
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	/**
	 * @return 审查审批结论（流程意见标识）
	 */
	public String getWfiResult() {
		return wfiResult;
	}

	/**
	 * @param 审查审批结论（流程意见标识）
	 */
	public void setWfiResult(String wfiResult) {
		this.wfiResult = wfiResult;
	}

	/**
	 * @return 流程审批状态
	 */
	public String getWfiStatus() {
		return wfiStatus;
	}

	/**
	 * @param 流程审批状态
	 */
	public void setWfiStatus(String wfiStatus) {
		this.wfiStatus = wfiStatus;
	}

	/**
	 * @return 消息处理状态
	 */
	public String getOpstatus() {
		return opstatus;
	}

	/**
	 * @param 消息处理状态
	 */
	public void setOpstatus(String opstatus) {
		this.opstatus = opstatus;
	}

	/**
	 * @return 操作时间
	 */
	public String getOptime() {
		return optime;
	}

	/**
	 * @param 操作时间
	 */
	public void setOptime(String optime) {
		this.optime = optime;
	}

	/**
	 * @调用父类clone方法
	 * @return Object
	 * @throws CloneNotSupportedException
	 */
	public Object clone()throws CloneNotSupportedException{
		Object result = super.clone();
		//TODO: 定制clone数据
		return result;
	}
	
}