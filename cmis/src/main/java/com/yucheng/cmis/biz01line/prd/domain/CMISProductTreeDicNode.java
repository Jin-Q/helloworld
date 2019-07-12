package com.yucheng.cmis.biz01line.prd.domain;

import java.util.ArrayList;
import java.util.List;


public class CMISProductTreeDicNode {

	public String catalogId = "catalogId";//目录编号
	public String catalogName = "catalogName";//目录名称
	public String supCatalogId = "supCatalogId";//上级目录
	public String catalogLevel = "catalogLevel";//目录级别
	public CMISProductTreeDicNode parent;
	public List childs;
	
	
	public CMISProductTreeDicNode getChild(String nodeId){
		if(this.childs == null)
			return null;
		for(int i=0;i<childs.size();i++){
			CMISProductTreeDicNode node = (CMISProductTreeDicNode)childs.get(i);
			if(nodeId.equals(node.catalogId))
				return node;
		}
		return null;
	}
	
	public void addChild(CMISProductTreeDicNode node){
		if(this.childs == null)
			this.childs = new ArrayList();
		this.childs.add(node);
		node.parent = this;
	}
}
