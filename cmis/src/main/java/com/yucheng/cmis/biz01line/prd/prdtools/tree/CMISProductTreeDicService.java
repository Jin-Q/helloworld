package com.yucheng.cmis.biz01line.prd.prdtools.tree;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPConstance;
import com.ecc.emp.log.EMPLog;
import com.ecc.emp.service.EMPService;
import com.yucheng.cmis.biz01line.prd.domain.CMISProductTreeDicNode;

/**
 * @describe 产品树生成服务
 * @author Pansq
 */
public class CMISProductTreeDicService extends EMPService {

	private String catalogId = "catalogId";//目录编号
	private String catalogName = "catalogName";//目录名称
	private String supCatalogId = "supCatalogId";//上级目录
	private String catalogLevel = "catalogLevel";//目录级别
	private String Comments = "Comments";//备注
	private String displaySeparator = "->";
	
	private static final String prdKind = "productTree";
	
	//Map中每个对象都是一棵字典树
	private HashMap treeCache = new HashMap();

	//Map中的每个对象都是按照不同type进行区分的节点(用于快速的通过节点的编号找到相应的节点)
	private HashMap treeNodeCache = new HashMap();
	
	public void loadDicData(Context context, Connection connection) throws Exception{
		String sqlStr = null;
		Statement state = null;	
		ResultSet rs = null;
		//初始化时,将已有的树形字典数据清空
		treeCache.clear();
		treeNodeCache.clear();
		try{
			state = connection.createStatement();
			//查询产品目录表中所有产品目录
			sqlStr = "select "+this.getCatalogId()+","+this.getCatalogName()+","+this.getSupCatalogId()+","+this.getCatalogLevel()+","+this.getComments()+" from prd_catalog order by "+this.getCatalogId();
			rs = state.executeQuery(sqlStr);
			while(rs.next()){
				String catalogIdValue = rs.getString(1);
				String catalogNameValue = rs.getString(2);
				String supCatalogIdValue = rs.getString(3);
				String catalogLevelValue = rs.getString(4);
				String CommentsValue = rs.getString(5);
				
				//catalogIdValue为空，则缺省认为是非法数据
				if(catalogIdValue == null || "".equals(catalogIdValue)){
					continue;
				}
				//从节点缓存中找到相应的节点，如果不存在则新建一个节点，默认设置为一个
				HashMap nodeCache = (HashMap)treeNodeCache.get(prdKind);
				if(nodeCache == null){
					nodeCache = new HashMap();
					treeNodeCache.put(prdKind, nodeCache);
				}
				//从树中取当前节点
				CMISProductTreeDicNode node = (CMISProductTreeDicNode)nodeCache.get(catalogIdValue);
				if(node == null){
					node = new CMISProductTreeDicNode();
					node.catalogId = catalogIdValue;
					node.catalogName = catalogNameValue;
					node.supCatalogId = supCatalogIdValue;
					node.catalogLevel = catalogLevelValue;
					
					nodeCache.put(catalogIdValue, node);
				}else{//若已存在，则补全相应的信息(因为locate的原因，则提前加到treeNodeMap中)
					node.catalogId = catalogIdValue;
					node.catalogName = catalogNameValue;
					node.supCatalogId = supCatalogIdValue;
					node.catalogLevel = catalogLevelValue;
					continue;
				}
				//supCatalogIdValue为空或空格，则缺省认为是字典树的根节点
				if(supCatalogIdValue == null || "".equals(supCatalogIdValue) || " ".equals(supCatalogIdValue)){
					treeCache.put(prdKind, node);
					continue;
				}else{
					CMISProductTreeDicNode parent = (CMISProductTreeDicNode)nodeCache.get(supCatalogIdValue);
					if(parent != null){
						parent.addChild(node);
						continue;
					}
				}
				//若通过parent无法找到相应的父节点，则由locate构造出字典树
				String[] paths = catalogLevelValue.split(",");
				CMISProductTreeDicNode pNode = null;
				//supCatalogIdValue中第一个为根节点,最后一个是本节点
				for(int i=0;i<paths.length-1;i++){
					String path = paths[i];
					//不合法的部分
					if(path == null || "".equals(path))
						continue;
					//最开始pNode为空，从treeCache中取得根节点
					if(pNode == null){
						pNode = (CMISProductTreeDicNode)treeCache.get(prdKind);
						if(pNode == null){
							pNode = new CMISProductTreeDicNode();
							pNode.catalogId = path;
							nodeCache.put(path, pNode);
							treeCache.put(prdKind, pNode);
						}
					}else{
						CMISProductTreeDicNode child = pNode.getChild(path);
						if(child == null){
							child = new CMISProductTreeDicNode();
							child.catalogId = path;
							pNode.addChild(child);
							nodeCache.put(path, child);
						}
						pNode = child;
					}
				}
				if(pNode != null)
					pNode.addChild(node);
			}
			rs.close();
			rs = null;
			state.close();
			state = null;
			
		}catch(Exception e){
			EMPLog.log(EMPConstance.EMP_CORE, 0, EMPLog.ERROR, "The DataDicService occur an error:"+e.getMessage());
			throw e;
		}finally {
			if(rs != null){
				try {
					rs.close();
				} catch (SQLException e) {}
			}
			if (state != null) {
				try {
					state.close();
				} catch (SQLException e) {}
			}
		}
	}
	
	/**
	 * 获得产品目录树结构下的所有JSON字符串
	 */
	public String prdToJsonString(){
		CMISProductTreeDicNode node = (CMISProductTreeDicNode)treeCache.get("productTree");
		return this.toJsonString(node, true);
	}
	
	public String prdToJsonByNodeId(String nodeId){
		HashMap nodeCache = (HashMap)treeNodeCache.get(nodeId);
		if(nodeCache == null)
			return null;
		//nodeId = "PRD20120802035";
		CMISProductTreeDicNode node = (CMISProductTreeDicNode)nodeCache.get(nodeId);
		return this.toJsonString(node, true);
	}
	
	/**
	 * 获得某个节点以及下属所有节点的JSON字符串
	 * @param nodeId
	 * @return
	 */
	public String toJsonStringById(String type, String nodeId, boolean dynamic){
		HashMap nodeCache = (HashMap)treeNodeCache.get(type);
		if(nodeCache == null)
			return null;
		nodeId = "PRD20120802035";
		CMISProductTreeDicNode node = (CMISProductTreeDicNode)nodeCache.get(nodeId);
		return this.toJsonString(node, dynamic);
	}
	
	/**
	 * 获得某个节点以及下属所有节点的JSON字符串
	 * @param node
	 * @return
	 */
	public String toJsonString(CMISProductTreeDicNode node, boolean dynamic){
		if(node == null)
			return null;
		StringBuffer buf = new StringBuffer();
		buf.append("{id:\"").append(node.catalogId).append("\"");
		buf.append(",label:\"").append(node.catalogName).append("\"");
		buf.append(",dynamic:\"").append(dynamic).append("\"");
		if(node.catalogLevel != null && node.catalogLevel.length() > 0)
			buf.append(",locate:\"").append(node.catalogLevel).append("\"");
		
		if(node.childs != null && node.childs.size() > 0){
			buf.append(",leaf:\"").append(false).append("\"");
			buf.append(",children:[");
			for(int i=0;i<node.childs.size();i++){
				CMISProductTreeDicNode child = (CMISProductTreeDicNode)node.childs.get(i);
				
				getSubTreeJsonString(buf, child, dynamic);
				
				if(i <node.childs.size() -1)
					buf.append(",");
			}
			buf.append("]");
		}else{
			buf.append(",leaf:\"").append(true).append("\"");
		}
		
		buf.append("}");
		
		return buf.toString();
	}
	
	/**
	 * @describe 将上一级数生成JSON
	 * @param buf
	 * @param node
	 * @param dynamic
	 */
	private void getSubTreeJsonString(StringBuffer buf,CMISProductTreeDicNode node, boolean dynamic){
		buf.append("{id:\"").append(node.catalogId).append("\"");
		buf.append(",label:\"").append(node.catalogName).append("\"");
		buf.append(",dynamic:\"").append(dynamic).append("\"");
		if(node.catalogLevel != null && node.catalogLevel.length() > 0)
			buf.append(",locate:\"").append(node.catalogLevel).append("\"");
		
		if(node.childs != null && node.childs.size() > 0){
			buf.append(",leaf:\"").append(false).append("\"");
			if(dynamic == false){
				buf.append(",children:[");
				for(int i=0;i<node.childs.size();i++){
					CMISProductTreeDicNode child = (CMISProductTreeDicNode)node.childs.get(i);
					getSubTreeJsonString(buf,child, true);
					if(i <node.childs.size() -1)
						buf.append(",");
				}
				buf.append("]");
			}
		}else
			buf.append(",leaf:\"").append(true).append("\"");
		buf.append("}");
	}
	
	/**
	 * @describe 生成子节点的JSON
	 * @param type
	 * @param nodeId
	 * @return
	 */
	public String toChildrenJsonStringById(String nodeId){
		HashMap nodeCache = (HashMap)treeNodeCache.get(prdKind);
		if(nodeCache == null) {
			return null;
		}
		//nodeId = "PRD20120802035";
		CMISProductTreeDicNode node = (CMISProductTreeDicNode)nodeCache.get(nodeId);
		if(node == null || node.childs == null || node.childs.size() == 0)
			return null;
		
		StringBuffer buf = new StringBuffer();
		buf.append("[");
		for(int i=0;i<node.childs.size();i++){
			CMISProductTreeDicNode child = (CMISProductTreeDicNode)node.childs.get(i);
			
			buf.append("{id:\"").append(child.catalogId).append("\"");
			buf.append(",label:\"").append(child.catalogName).append("\"");
			buf.append(",dynamic:\"").append(true).append("\"");
			if(child.catalogLevel != null && child.catalogLevel.length() > 0)
				buf.append(",locate:\"").append(child.catalogLevel).append("\"");
			
			if(child.childs != null && child.childs.size() > 0)
				buf.append(",leaf:\"").append(false).append("\"");
			else
				buf.append(",leaf:\"").append(true).append("\"");
			
			buf.append("}");
			
			if(i <node.childs.size() -1)
				buf.append(",");
		}
		buf.append("]");
		
		return buf.toString();
	}
	
	
	
	public String getDisplaySeparator() {
		return displaySeparator;
	}
	public void setDisplaySeparator(String displaySeparator) {
		this.displaySeparator = displaySeparator;
	}
	public String getCatalogId() {
		return catalogId;
	}
	public void setCatalogId(String catalogId) {
		this.catalogId = catalogId;
	}
	public String getCatalogName() {
		return catalogName;
	}
	public void setCatalogName(String catalogName) {
		this.catalogName = catalogName;
	}
	public String getSupCatalogId() {
		return supCatalogId;
	}
	public void setSupCatalogId(String supCatalogId) {
		this.supCatalogId = supCatalogId;
	}
	public String getCatalogLevel() {
		return catalogLevel;
	}
	public void setCatalogLevel(String catalogLevel) {
		this.catalogLevel = catalogLevel;
	}
	public String getComments() {
		return Comments;
	}
	public void setComments(String comments) {
		Comments = comments;
	}

	
}
