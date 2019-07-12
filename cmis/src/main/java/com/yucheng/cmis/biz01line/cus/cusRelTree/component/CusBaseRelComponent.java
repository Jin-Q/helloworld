package com.yucheng.cmis.biz01line.cus.cusRelTree.component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.Map.Entry;

import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.yucheng.cmis.biz01line.cus.cusRelTree.agent.CusRelTreeAgent;
import com.yucheng.cmis.biz01line.cus.cusRelTree.domain.CusRelTree;
import com.yucheng.cmis.biz01line.cus.cusRelTree.domain.CusTree;
import com.yucheng.cmis.biz01line.cus.cusRelTree.util.TreeUtil;
import com.yucheng.cmis.biz01line.cus.cusbase.domain.CusBase;
import com.yucheng.cmis.biz01line.cus.interfaces.CustomIface;
import com.yucheng.cmis.pub.CMISComponent;
import com.yucheng.cmis.pub.exception.ComponentException;
import com.yucheng.cmis.pub.util.StringUtil;

public abstract class CusBaseRelComponent extends CMISComponent {

	/**
	 * cusTree取节点时的最大递归数
	 */
	public final static int MAX_COUNT = 10;

	/**
	 * cusTree展示的最大层数
	 */
	public final static int MAX_DISPLAY_LAYER = 3;

	/**
	 * 通过递归取CusTree的所有子节点时使用的临时变量，getCusRelTree方法使用
	 */
	private List<String> idList = new ArrayList<String>();

	/**
	 * 将cusTree转换为cusRelTree时使用的临时变量，转换后，此map为最后的返回值。getCusRelTree方法使用
	 */
	Map<String, CusRelTree> map = new LinkedHashMap<String, CusRelTree>();

	public Map<String, CusRelTree> get3DisplayLayerTree(CusRelTree cusRelTreeP)
			throws EMPException {
		return getAllTree(cusRelTreeP, MAX_DISPLAY_LAYER);
	}

	/**
	 * 判断该客户是否有对公关联客户
	 * 
	 * @param cusId
	 * @return 如果有关联客户，返回true
	 * @throws EMPException
	 */
	public boolean isInCusComRelTree(String cusId) throws ComponentException {
		try {
			CustomIface cusComInterface = (CustomIface) this
					.getComponentInterface("CustomIface");
			CusBase cusBase = cusComInterface.getCusBase(cusId);
			CusRelTree cusRelTree = new CusRelTree();
			cusRelTree.setNodeId(cusId);
			cusRelTree.setNodeLevel("");
			cusRelTree.setNodeName(cusBase.getCusName());
			cusRelTree.setNodeAttribute(TreeUtil.cusCom);
			cusRelTree.setNodeType("TreeSelf");
			cusRelTree.setPNodeId("-1");
			cusRelTree.setNodeInfo("");
			cusRelTree.setNodeCusType(cusBase.getCusType());
			cusRelTree.setNewParentID("-1");
			cusRelTree.setNewID(StringUtil.getRandomId());

			Map<String, CusRelTree> treeMap;

			treeMap = this.get3DisplayLayerTree(cusRelTree);
			Iterator<Entry<String, CusRelTree>>  it = treeMap.entrySet().iterator();
			Map.Entry<String, CusRelTree> entry = null;
			if (it != null) {
				while (it.hasNext()) {
					entry = (Map.Entry<String, CusRelTree>) it.next();
					CusRelTree subTree = (CusRelTree) entry.getValue();
					String cusType = subTree.getNodeAttribute();
					String subCusId = subTree.getNodeId();
					if (cusType.trim().equals("TreeCusCom")
							&& !subCusId.equals(cusId)) {
						return true;
					}
				}
			}
		} catch (EMPException e) {
			e.printStackTrace();
			throw new ComponentException(e);
		}

		return false;
	}
	
	/**
	 * 是否是集团客户判断(嘉兴)
	 * @param cusId 客户编号
	 * @return
	 * @throws ComponentException
	 */
	public boolean isGrpCus(String cusId) throws ComponentException{
		CusRelTreeAgent agent = (CusRelTreeAgent) this.getAgentInstance("CusRel");
		
		int count = agent.queryIsGrpcus(cusId);
		if(count>0){
			return true;
		}else{
			return false;
		}

	}
	
	private Map<String, CusRelTree> getAllTree(CusRelTree cusRelTreeP, int count)
			throws EMPException {
		CusTree cusTree = null;
		cusTree = transCusRelTreeMapToCusTree(cusRelTreeP);
		//如果是根结点镇村经济组织，就不继续往下查了
		if(!"250".equals(cusTree.getNodeCusType())) {
			cusTree = (CusTree) getCusRelTree(cusTree, MAX_COUNT);
			adjustWholeCusTree(cusTree);
		}
		transCusTreeToCusRelTreeMap(cusTree, "-1", "-1", count);
		return map;
	}

	protected abstract List<CusTree> searchRelCusList(CusTree cusTree)
			throws EMPException;

	/**
	 * @author lisj
	 * @time 2014-11-27
	 * @description 需求编号：【XD141107075】
	 * 				用于对公客户管理一键查询导出功能（超类），查询企业关联关系（只展示第一层），并返回IndexedCollection数据
	 * @return relCusListIColl
	 */
	public abstract IndexedCollection searchRelCusList(String cusId, String CusType)
	throws EMPException;
	
	/**
	 * 通过搜索数据库取到树的所有子结点
	 * 
	 * @param cusTree
	 *            树
	 * @param count
	 *            剩下的递归次数
	 * @param cusIdList
	 *            临时的一个list，用于树中的cusId判断是否有重复，如果有重复，就不再递归它的子节点
	 * @return 树
	 * @throws EMPException
	 */
	private CusTree getCusRelTree(CusTree cusTree, int count)
			throws EMPException {
		// 如果cusId已经查出来过了，就不再递归
		if (idList.contains(cusTree.getNodeId())) {
			count = 0;
			return null;
		} else {
			idList.add(cusTree.getNodeId());
		}

		List<CusTree> cusTreeList = this.searchRelCusList(cusTree);
		if (cusTreeList == null || cusTreeList.size() == 0 || count == 0) {
			cusTree.setHasSubTree(false);
			cusTree.setChildTree(null);
			return cusTree;
		} else {
			cusTree.setHasSubTree(true);
			for (CusTree tmpCusTree : cusTreeList) {
				List<CusTree> childTree = cusTree.getChildTree();
				if (childTree != null && childTree.size() > 0) {
					childTree.add(tmpCusTree);
				} else {
					// 如果原来的子节点为null，需要新增一个list作为子节点
					List<CusTree> tmpList = new Vector<CusTree>();
					tmpList.add(tmpCusTree);
					cusTree.setChildTree(tmpList);
				}
				tmpCusTree = getCusRelTree(tmpCusTree, count--);
			}
		}
		return cusTree;
	}

	protected Comparator<CusTree> CusTreeListCompare = new Comparator<CusTree>() {
		public int compare(CusTree o1, CusTree o2) {
			return Integer.parseInt(o1.getNodeCusType().substring(0, 1))
					- Integer.parseInt(o2.getNodeCusType().substring(0, 1));
		}
	};

	/**
	 * 调整tree的位置，树的上下两层的节点，如果它们是相同的，就把下层节点的子节点全部移到上层
	 * 
	 * @param cusTree
	 * @return boolean 是否有做过调整，是为true，否为false
	 */
	private boolean adjustCusTree(CusTree cusTree) {
		boolean flag = false;
		// 存放所有已经出现过的树的id
		List<String> idList = new ArrayList<String>();

		String id = cusTree.getNodeId();

		// 存放循环的那层的树的节点
		List<CusTree> treeList = new Vector<CusTree>();
		treeList.add(cusTree);
		while (treeList != null && treeList.size() > 0) {
			// 临时存放当前层的树节点
			List<CusTree> list = new Vector<CusTree>();
			for (CusTree subList : treeList) {
				if (!id.equals(subList.getNodeId())) {
					idList.add(subList.getNodeId());
				}
				if (subList.hasSubTree()) {
					list.addAll(subList.getChildTree());
				}
			}
			for (CusTree sub : list) {
				// 节点的id与上层id相同，就让上层id拥有下层节点的子节点
				if (idList.contains(sub.getNodeId()) && sub.hasSubTree()) {
					CusTree tmpSubTree = cusTree.findFirstNode(sub.getNodeId());
					tmpSubTree.setChildTree(sub.getChildTree());
					tmpSubTree.setHasSubTree(true);
					sub.setChildTree(null);
					sub.setHasSubTree(false);
					flag = true;
				}
			}
			treeList = list;
		}
		return flag;
	}

	/**
	 * 调整整个树，直到不能再调整为止
	 * 
	 * @param tree
	 */
	private void adjustWholeCusTree(CusTree tree) {
		// 循环次数计数，最多循环10次
		int count = 0;
		// 上一次没有调整的话就不再循环
		while (adjustCusTree(tree) && count++ < MAX_COUNT) {
		}
	}

	/**
	 * 从CusTree转换成CusRelTree
	 * 
	 * @param cusTree
	 * @param parentId
	 *            父节点id
	 * @param parentNodeId
	 *            父节点Node id
	 * @param count
	 *            取得的cusRelTree的层数
	 * @return
	 */
	private void transCusTreeToCusRelTreeMap(CusTree cusTree, String parentId,
			String parentNodeId, int count) {
		if (count-- >= 0) {
			CusRelTree cusRelTree = new CusRelTree();
			// 生成当前节点id
			String newId = StringUtil.getRandomId();
			// 当前节点Node id
			String newNodeId = cusTree.getNodeId();
			// 把cusTree里的值都转换成cusRelTree
			cusRelTree.setNodeId(newNodeId);
			cusRelTree.setPNodeId(parentNodeId);
			cusRelTree.setNodeName(cusTree.getNodeName());
			cusRelTree.setNodeInfo(cusTree.getNodeInfo());
			cusRelTree.setNodeType(cusTree.getNodeType());
			cusRelTree.setNodeAttribute(cusTree.getNodeAttribute());
			cusRelTree.setNodeCusType(cusTree.getNodeCusType());
			cusRelTree.setNewID(newId);
			cusRelTree.setNewParentID(parentId);
			map.put(cusRelTree.getNewID(), cusRelTree);
			List<CusTree> childTree = cusTree.getChildTree();
			// 递归子节点
			if (childTree != null && childTree.size() > 0) {
				for (CusTree tmpTree : childTree) {
					transCusTreeToCusRelTreeMap(tmpTree, newId, newNodeId,
							count);
				}
			}
		}
	}

	/**
	 * 从CusRelTree转换成CusTree 目前只需要实现只有一个根节点的CusRelTree转换
	 * 
	 * @param cusMap
	 * @return
	 */
	private CusTree transCusRelTreeMapToCusTree(CusRelTree cusRelTree) {
		CusTree cusTree = new CusTree();
		cusTree.setNodeId(cusRelTree.getNodeId());
		cusTree.setNodeName(cusRelTree.getNodeName());
		cusTree.setNodeInfo(cusRelTree.getNodeInfo());
		cusTree.setNodeType(cusRelTree.getNodeType());
		cusTree.setNodeAttribute(cusRelTree.getNodeAttribute());
		cusTree.setNodeCusType(cusRelTree.getNodeCusType());
		return cusTree;
	}
	
	public List<String> getCusComRelList(String cusId) throws ComponentException {
		List<String> list = new ArrayList<String>();
		try {
		CusRelTree cusRelTree = transRootCusRelTreeFromCusId(cusId);

		Map<String, CusRelTree> treeMap = this.get3DisplayLayerTree(cusRelTree);
		Iterator<Entry<String, CusRelTree>> it = treeMap.entrySet().iterator();
		Map.Entry<String, CusRelTree> entry = null;

		if (it != null) {
			while (it.hasNext()) {
				entry = (Map.Entry<String, CusRelTree>) it.next();
				CusRelTree subTree = (CusRelTree) entry.getValue();
				if(!(subTree.getNodeCusType().equals("110")||subTree.getNodeCusType().equals("120")||subTree.getNodeCusType().equals("130")))
					list.add(subTree.getNodeId());
			}
		}
		} catch (EMPException e) {
			e.printStackTrace();
			throw new ComponentException(e);
		}
		return list;
	}
	private CusRelTree transRootCusRelTreeFromCusId(String cusId) throws EMPException {
		CustomIface cusComInterface = (CustomIface) this.getComponentInterface("CustomIface");
		CusBase cusBase = cusComInterface.getCusBase(cusId);
		CusRelTree cusRelTree = new CusRelTree();
		cusRelTree.setNodeId(cusId);
		cusRelTree.setNodeLevel("");
		cusRelTree.setNodeName(cusBase.getCusName());
		cusRelTree.setNodeAttribute(TreeUtil.cusCom);
		cusRelTree.setNodeType("TreeSelf");
		cusRelTree.setPNodeId("-1");
		cusRelTree.setNodeInfo("");
		cusRelTree.setNodeCusType(cusBase.getCusType());
		cusRelTree.setNewParentID("-1");
		cusRelTree.setNewID(StringUtil.getRandomId());
		return cusRelTree;
	}
	public List<String> getComRelCus(String cusId) throws EMPException{
		CusRelTreeAgent agent = (CusRelTreeAgent) this.getAgentInstance("CusRel");
		return agent.getComRelCus(cusId);
	}

}
