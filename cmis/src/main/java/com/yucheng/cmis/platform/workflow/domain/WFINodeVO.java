package com.yucheng.cmis.platform.workflow.domain;

import java.util.List;

/**
 * 流程节点对象
 * <p>
 * 主要用在获取流程节点时，以该对象返回。
 * 
 * @author liuhw 2013/6/14
 *
 */
public class WFINodeVO {
	
	/**
	 * 流程节点ID
	 */
	private String nodeId;
	
	/**
	 * 流程节点名称
	 */
	private String nodeName;
	
	/**
	 * 节点类型，S开始；E结束；A人工；C自动；G全局；X异常；F补偿
	 */
	private String nodeType;
	
	/**
	 * 办理时是否需要人工选择用户。1人工选择；0系统指定
	 */
	private String ifSelectUser;
	
	/**
	 * 来源路由顺序号。流程配置时没有设置则默认1
	 */
	private Integer routeSeq;
	
	/**
	 * 来源路由名称。流程配置时没有设置则默认指向的节点名称
	 */
	private String routeName;
	
	/**
	 * 办理类型: 0.单人签收办理;1.单人竞争办理;2.多人顺序办理;3.多人并行办理;4.按转移条件;5.多人顺序可结束;6.多人并行可结束
	 */
	private String nodeTransactType;
	
	/**
	 * nodeRouterType:节点流向类型: “0”一般处理； “1”单选处理； “2”多选处理； 3.条件单选处理； 4.条件多选处理
	 */
	private String nodeRouterType;
	
	/**
	 * 节点办理人员list
	 */
	private List<WFIUserVO> userList;
	

	/**
	 * 流程节点ID
	 * @return the nodeId
	 */
	public String getNodeId() {
		return nodeId;
	}

	/**
	 * 流程节点ID
	 * @param nodeId the nodeId to set
	 */
	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	/**
	 * 流程节点名称
	 * @return the nodeName
	 */
	public String getNodeName() {
		return nodeName;
	}

	/**
	 * 流程节点名称
	 * @param nodeName the nodeName to set
	 */
	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	/**
	 * 节点办理人员list
	 * @return the userList
	 */
	public List<WFIUserVO> getUserList() {
		return userList;
	}

	/**
	 * 节点办理人员list
	 * @param userList the userList to set
	 */
	public void setUserList(List<WFIUserVO> userList) {
		this.userList = userList;
	}

	/**
	 * 节点类型，S开始；E结束；A人工；C自动；G全局；X异常；F补偿
	 * @return the nodeType
	 */
	public String getNodeType() {
		return nodeType;
	}

	/**
	 * 节点类型，S开始；E结束；A人工；C自动；G全局；X异常；F补偿
	 * @param nodeType the nodeType to set
	 */
	public void setNodeType(String nodeType) {
		this.nodeType = nodeType;
	}

	/**
	 * 办理时是否需要人工选择用户。1人工选择；0系统指定
	 * @return the ifselectuser
	 */
	public String getIfselectuser() {
		return ifSelectUser;
	}

	/**
	 * 办理时是否需要人工选择用户。1人工选择；0系统指定
	 * @param ifselectuser the ifselectuser to set
	 */
	public void setIfselectuser(String ifselectuser) {
		this.ifSelectUser = ifselectuser;
	}

	/**
	 * 来源路由顺序号。流程配置时没有设置则默认1
	 * @return the routeseq
	 */
	public Integer getRouteseq() {
		return routeSeq;
	}

	/**
	 * 来源路由顺序号。流程配置时没有设置则默认1
	 * @param routeseq the routeseq to set
	 */
	public void setRouteseq(Integer routeseq) {
		this.routeSeq = routeseq;
	}

	/**
	 * 来源路由名称。流程配置时没有设置则默认指向的节点名称
	 * @return the routename
	 */
	public String getRouteName() {
		return routeName;
	}

	/**
	 * 来源路由名称。流程配置时没有设置则默认指向的节点名称
	 * @param routename the routename to set
	 */
	public void setRouteName(String routeName) {
		this.routeName = routeName;
	}

	/**
	 * 办理类型: 0.单人签收办理;1.单人竞争办理;2.多人顺序办理;3.多人并行办理;4.按转移条件;5.多人顺序可结束;6.多人并行可结束
	 * @return the nodetransacttype
	 */
	public String getNodeTransactType() {
		return nodeTransactType;
	}

	/**
	 * 办理类型: 0.单人签收办理;1.单人竞争办理;2.多人顺序办理;3.多人并行办理;4.按转移条件;5.多人顺序可结束;6.多人并行可结束
	 * @param nodetransacttype the nodetransacttype to set
	 */
	public void setNodeTransactType(String nodeTransactType) {
		this.nodeTransactType = nodeTransactType;
	}

	/**
	 * 节点流向类型: “0”一般处理； “1”单选处理； “2”多选处理； 3.条件单选处理； 4.条件多选处理
	 * @return the nodeRouterType
	 */
	public String getNodeRouterType() {
		return nodeRouterType;
	}

	/**
	 * 节点流向类型: “0”一般处理； “1”单选处理； “2”多选处理； 3.条件单选处理； 4.条件多选处理
	 * @param nodeRouterType the nodeRouterType to set
	 */
	public void setNodeRouterType(String nodeRouterType) {
		this.nodeRouterType = nodeRouterType;
	}
	
	
}
