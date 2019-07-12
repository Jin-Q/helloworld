package com.yucheng.cmis.biz01line.cus.cusRelTree.component;

import java.util.List;
import java.util.Vector;

import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.yucheng.cmis.biz01line.cus.cusRelTree.agent.CusRelTreeAgent;
import com.yucheng.cmis.biz01line.cus.cusRelTree.domain.CusTree;

public class CusIndivRelComponent extends CusBaseRelComponent {

	/**
	 * 个人关联客户搜索条件
	 * 1.个人做为企业的关键人
	 * 2.个人的配偶，父母，子女，兄弟姐妹的控股企业
	 * 3.以控股方式控制其它企事业。
	 * 
	 * @param cusTree
	 * @return
	 */
	public List<CusTree> searchRelCusList(CusTree cusTree) throws EMPException {
		CusRelTreeAgent agent = (CusRelTreeAgent) this.getAgentInstance("CusRel");
		List<CusTree> list = new Vector<CusTree>();
		String cusType = cusTree.getNodeCusType();
		String cusId = cusTree.getNodeId();
		// 对公客户的客户类型以2开头，如果是对公客户，就不需要再查下去了
//		if (cusType != null && cusType.startsWith("2")) {
//			return null;
//		}
		//个人客户以Z开头，如果是对公客户，就不需要再查下去了
		if (cusType != null && !cusType.startsWith("Z")) {
			return null;
		}
		list.addAll(agent.getRelCus(cusId));
		list.addAll(agent.getKeyCom(cusId));
		list.addAll(agent.getComAndIndivFromInvest(cusId));
		// 如果没有子节点，就把子节点清空为null
		if (list.size() == 0) {
			return null;
		}
		//排序注掉
//		Collections.sort(list, CusTreeListCompare);
		return list;
	}
	/**
	 * @author lisj
	 * @time 2014-11-27
	 * @description 需求编号：【XD141107075】
	 * 				用于对公客户管理一键查询导出功能，查询企业关联关系（只展示第一层），并返回IndexedCollection数据
	 * @return relCusListIColl
	 */
	@Override
	public IndexedCollection searchRelCusList(String cusId, String CusType)
			throws EMPException {
		// TODO Auto-generated method stub
		return null;
	}
}
