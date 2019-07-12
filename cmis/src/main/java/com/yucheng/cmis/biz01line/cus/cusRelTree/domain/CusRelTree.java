package com.yucheng.cmis.biz01line.cus.cusRelTree.domain;

/**
 * Title: CusRelation.java Description: DTree node机构
 * 
 * @author gaozh@yuchengtech.com
 * @version 1.0
 */
public class CusRelTree implements com.yucheng.cmis.pub.CMISDomain {
	private String treeName; // 树形名称
	private String nodeId; // 当前ID
	private String pNodeId; // 上层ID
	private String nodeUrl; // 当前ID链接
	private String nodeName; // 当前ID中文名
	private String nodeInfo; // 当前ID描述信息
	private String nodeType; // 当前ID类型
	private String nodeAttribute; // 当前ID备用属性
	private String nodeLevel;// 当前层次
	private String nodeCusType;// 当前节点客户类型
	private String newID;// 当前生成树的ID
	private String newParentID;//当前生成树的父ID
	public String getNewParentID() {
		return newParentID;
	}

	public void setNewParentID(String newParentID) {
		this.newParentID = newParentID;
	}

	public String getNewID() {
		return newID;
	}

	public void setNewID(String newID) {
		this.newID = newID;
	}

	public String getNodeCusType() {
		return nodeCusType;
	}

	public void setNodeCusType(String nodeCusType) {
		this.nodeCusType = nodeCusType;
	}

	public String getTreeName() {
		return treeName;
	}

	public void setTreeName(String treeName) {
		this.treeName = treeName;
	}

	public String getNodeId() {
		return nodeId;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	public String getPNodeId() {
		return pNodeId;
	}

	public void setPNodeId(String nodeId) {
		pNodeId = nodeId;
	}

	public String getNodeUrl(String emp_sid) {
		// nodeUrl="/cmis-main/getCusViewPage.do?EMP_SID="+emp_sid+"&cusId="+nodeId+"&info=tree";
		return nodeUrl;
	}

	public void setNodeUrl(String nodeUrl) {
		this.nodeUrl = nodeUrl;
	}

	public String getNodeName() {
		return nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	public String getNodeInfo() {
		return nodeInfo;
	}

	public void setNodeInfo(String nodeInfo) {
		this.nodeInfo = nodeInfo;
	}

	public String getNodeType() {
		return nodeType;
	}

	public void setNodeType(String nodeType) {
		this.nodeType = nodeType;
	}

	public String getNodeAttribute() {
		return nodeAttribute;
	}

	public void setNodeAttribute(String nodeAttribute) {
		this.nodeAttribute = nodeAttribute;
	}

	public String getNodeLevel() {
		if (nodeLevel == null)
			nodeLevel = "";
		return nodeLevel;
	}

	public void setNodeLevel(String nodeLevel) {
		this.nodeLevel = nodeLevel;
	}

	public String getNextNodeLevel() {
		String nextLevel = "One";
		if (!(this.nodeLevel != null || !this.nodeLevel.equals(""))) {
			if (nodeLevel.equals("One"))
				nextLevel = "Two";
			else if (nodeLevel.equals("Two"))
				nextLevel = "Three";
			else if (nodeLevel.equals("Three"))
				nextLevel = "Four";
			else if (nodeLevel.equals("Four"))
				nextLevel = "Five";
			else if (nodeLevel.equals("Five"))
				nextLevel = "Six";
			else if (nodeLevel.equals("Six"))
				nextLevel = "Seven";
			else if (nodeLevel.equals("Seven"))
				nextLevel = "Eight";
			else if (nodeLevel.equals("Eight"))
				nextLevel = "Nine";
			else if (nodeLevel.equals("Nine"))
				nextLevel = "Ten";
		}

		return nextLevel;
	}

	public/* protected */Object clone() throws CloneNotSupportedException {

		// call父类的clone方法

		Object result = super.clone();

		// TODO: 定制clone数据

		return result;

	}
}