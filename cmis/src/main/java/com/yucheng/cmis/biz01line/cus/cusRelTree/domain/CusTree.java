package com.yucheng.cmis.biz01line.cus.cusRelTree.domain;

import java.util.List;
import java.util.Vector;

public class CusTree implements com.yucheng.cmis.pub.CMISDomain {

	private String treeName; // 树形名称
	private String nodeId; // 当前ID
	private String nodeUrl; // 当前ID链接
	private String nodeName; // 当前ID中文名
	private String nodeInfo; // 当前ID描述信息
	private String nodeType; // 当前ID类型
	private String nodeAttribute; // 当前ID备用属性
	private String nodeCusType;// 当前节点客户类型
	private List<CusTree> childTree; // 子节点
	private boolean hasSubTree; // 子节点
	
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

	public String getNodeUrl() {
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

	public String getNodeCusType() {
		return nodeCusType;
	}

	public void setNodeCusType(String nodeCusType) {
		this.nodeCusType = nodeCusType;
	}

	public List<CusTree> getChildTree() {
		return childTree;
	}

	public void setChildTree(List<CusTree> childTree) {
		this.childTree = childTree;
	}

	public Object clone() throws CloneNotSupportedException {
		Object result = super.clone();
		return result;
	}

	public boolean hasSubTree() {
		return hasSubTree;
	}
	
	public void setHasSubTree(boolean hasSubTree) {
		this.hasSubTree = hasSubTree;
	}
	
	public CusTree findSubTreeById(String nodeId)
	{
		if(!this.hasSubTree)
		{
			return null;
		}
		for(CusTree subTree : this.getChildTree())
		{
			if(nodeId.equals(subTree.getNodeId()))
			{
				return subTree;
			}
		}
		return null;
	}
	
	public void addSubTree(CusTree subTree)
	{
		if(this.hasSubTree)
		{
			this.setChildTree(new Vector<CusTree>());
		}
		this.getChildTree().add(subTree);
	}
	
	public void addSubTreeList(List<CusTree> subTreeList)
	{
		if(this.hasSubTree)
		{
			this.setChildTree(new Vector<CusTree>());
		}
		this.getChildTree().addAll(subTreeList);
	}
	
	/**
	 * 通过指定的node id取得树里最高一层的子节点
	 * @param id 指定node id
	 * @return
	 */
	public CusTree findFirstNode(String id) {
		if (this.nodeId.equals(id)) {
			return this;
		}
		List<CusTree> treeList = new Vector<CusTree>();
		treeList.add(this);
		while (treeList != null && treeList.size() > 0) {
			List<CusTree> list = new Vector<CusTree>();
			for (CusTree subList : treeList) {
				if (subList.hasSubTree) {
						list.addAll(subList.getChildTree());
				}
			}
			for (CusTree sub : list) {
				if (sub.nodeId.equals(id)) {
					return sub;
				}
			}
			treeList = list;
		}
		return null;
	}
	
	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("id: ").append(nodeId).append(" name: ").append(nodeName);
		return sb.toString();
	}
}
