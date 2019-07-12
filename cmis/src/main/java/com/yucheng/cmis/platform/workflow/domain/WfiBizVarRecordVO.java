package com.yucheng.cmis.platform.workflow.domain;

/**
 * 审批变更业务要素修改记录值对象
 * @author liuhw
 *
 */
public class WfiBizVarRecordVO implements com.yucheng.cmis.pub.CMISDomain {

	/**
	 * 审批变更修改ID
	 */
	private String pk1;
	
	/**
	 * 流程实例号
	 */
	private String instanceid;
	
	/**
	 * 业务申请流水号
	 */
	private String pkValue;
	
	/**
	 * 表模型ID
	 */
	private String tableName;
	
	/**
	 * 节点ID
	 */
	private String nodeid;
	
	/**
	 * 节点名称
	 */
	private String nodename;
	
	/**
	 * 变量键名
	 */
	private String varKey;
	
	/**
	 * 变量名称
	 */
	private String varName;
	
	/**
	 * 变量值
	 */
	private String varValue;
	
	/**
	 * 变量显示值
	 */
	private String varDispvalue;
	
	/**
	 * 原变量值
	 */
	private String varOldValue;
	
	/**
	 * 原变量显示值
	 */
	private String varOldDispvalue;
	
	/**
	 * 变量数据类型
	 */
	private String varType;
	
	/**
	 * 操作时间
	 */
	private String opTime;
	
	/**
	 * 操作人
	 */
	private String inputId;
	
	/**
	 * 操作人名称(仅用于前台显示)
	 */
	private String inputName;
	
	/**
	 * 操作机构
	 */
	private String inputBrId;
	
	/**
	 * 操作机构名称(仅用于前台显示)
	 */
	private String inputBrName;
	
	/**
	 * 流程意见ID
	 */
	private String commentid;
	
	/**
	 * 备注
	 */
	private String remark;
	
	
	
	/**
	 * @return 审批变更修改ID
	 */
	public String getPk1() {
		return pk1;
	}



	/**
	 * @param 审批变更修改ID
	 */
	public void setPk1(String pk1) {
		this.pk1 = pk1;
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
	 * @return 节点名称
	 */
	public String getNodename() {
		return nodename;
	}



	/**
	 * @param 节点名称
	 */
	public void setNodename(String nodename) {
		this.nodename = nodename;
	}



	/**
	 * @return 变量键名
	 */
	public String getVarKey() {
		return varKey;
	}



	/**
	 * @param 变量键名
	 */
	public void setVarKey(String varKey) {
		this.varKey = varKey;
	}



	/**
	 * @return 变量名称
	 */
	public String getVarName() {
		return varName;
	}



	/**
	 * @param 变量名称
	 */
	public void setVarName(String varName) {
		this.varName = varName;
	}



	/**
	 * @return 变量值
	 */
	public String getVarValue() {
		return varValue;
	}



	/**
	 * @param 变量值
	 */
	public void setVarValue(String varValue) {
		this.varValue = varValue;
	}



	/**
	 * @return 变量显示值
	 */
	public String getVarDispvalue() {
		return varDispvalue;
	}



	/**
	 * @param 变量显示值
	 */
	public void setVarDispvalue(String varDispvalue) {
		this.varDispvalue = varDispvalue;
	}



	/**
	 * @return 原变量值
	 */
	public String getVarOldValue() {
		return varOldValue;
	}



	/**
	 * @param 原变量值
	 */
	public void setVarOldValue(String varOldValue) {
		this.varOldValue = varOldValue;
	}



	/**
	 * @return 原变量显示值
	 */
	public String getVarOldDispvalue() {
		return varOldDispvalue;
	}



	/**
	 * @param 原变量显示值
	 */
	public void setVarOldDispvalue(String varOldDispvalue) {
		this.varOldDispvalue = varOldDispvalue;
	}



	/**
	 * @return 变量数据类型
	 */
	public String getVarType() {
		return varType;
	}



	/**
	 * @param 变量数据类型
	 */
	public void setVarType(String varType) {
		this.varType = varType;
	}



	/**
	 * @return 操作时间
	 */
	public String getOpTime() {
		return opTime;
	}



	/**
	 * @param 操作时间
	 */
	public void setOpTime(String opTime) {
		this.opTime = opTime;
	}



	/**
	 * @return 操作人
	 */
	public String getInputId() {
		return inputId;
	}



	/**
	 * @param 操作人
	 */
	public void setInputId(String inputId) {
		this.inputId = inputId;
	}



	/**
	 * @return 操作机构
	 */
	public String getInputBrId() {
		return inputBrId;
	}



	/**
	 * @param 操作机构
	 */
	public void setInputBrId(String inputBrId) {
		this.inputBrId = inputBrId;
	}



	/**
	 * @return 流程意见ID
	 */
	public String getCommentid() {
		return commentid;
	}



	/**
	 * @param 流程意见ID
	 */
	public void setCommentid(String commentid) {
		this.commentid = commentid;
	}



	/**
	 * @return 备注
	 */
	public String getRemark() {
		return remark;
	}



	/**
	 * @param 备注
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}



	/**
	 * @return 操作人名称(仅用于前台显示)
	 */
	public String getInputName() {
		return inputName;
	}



	/**
	 * @param inputName 操作人名称(仅用于前台显示) to set
	 */
	public void setInputName(String inputName) {
		this.inputName = inputName;
	}



	/**
	 * @return 操作机构名称(仅用于前台显示)
	 */
	public String getInputBrName() {
		return inputBrName;
	}



	/**
	 * @param inputBrName 操作机构名称(仅用于前台显示) to set
	 */
	public void setInputBrName(String inputBrName) {
		this.inputBrName = inputBrName;
	}



	/**
	 * @return 业务申请流水号
	 */
	public String getPkValue() {
		return pkValue;
	}



	/**
	 * @param pkValue 业务申请流水号 to set
	 */
	public void setPkValue(String pkValue) {
		this.pkValue = pkValue;
	}



	/**
	 * @return 表模型ID
	 */
	public String getTableName() {
		return tableName;
	}



	/**
	 * @param tableName 表模型ID to set
	 */
	public void setTableName(String tableName) {
		this.tableName = tableName;
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
