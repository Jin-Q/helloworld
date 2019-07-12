package com.yucheng.cmis.biz01line.cus.cusRelTree.component;

import java.util.List;
import java.util.Vector;

import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.yucheng.cmis.biz01line.cus.cusRelTree.agent.CusRelTreeAgent;
import com.yucheng.cmis.biz01line.cus.cusRelTree.domain.CusTree;

public class CusComRelComponent extends CusBaseRelComponent {
	/**
	 * 如果是企业，取得企业关键人 如果是企业，从资本构成信息查询到的企业和法人 如果是个人，取得个人配偶、父母、子女、兄弟姐妹
	 * 如果是个人，取得个人所在的企业 从对外投资信息查询到的企业
	 * 
	 * @param cusTree
	 * @return
	 */
	public List<CusTree> searchRelCusList(CusTree cusTree) throws EMPException {
		CusRelTreeAgent agent = (CusRelTreeAgent) this.getAgentInstance("CusRel");
		List<CusTree> list = new Vector<CusTree>();
		String cusType = cusTree.getNodeCusType();
		String cusId = cusTree.getNodeId();
		// 对公客户的客户类型以2开头
//		if (cusType != null && cusType.startsWith("2")) {
//			list.addAll(agent.getKeyCus(cusId));
//			list.addAll(agent.getComAndIndivFromApital(cusId));
//		} else {
//			list.addAll(agent.getRelCus(cusId));
//			list.addAll(agent.getKeyCom(cusId));
//		}
		// 个人客户的客户类型以Z开头
		if(cusType != null && cusType.startsWith("Z")){
			list.addAll(agent.getRelCus(cusId));
			list.addAll(agent.getKeyCom(cusId));
		}else {
			list.addAll(agent.getKeyCus(cusId));
			list.addAll(agent.getComAndIndivFromApital(cusId));
		}
		list.addAll(agent.getComAndIndivFromInvest(cusId));
		//第三方控股暂时注掉
//		list.addAll(agent.searchRelThirdCusList(cusId));
		// 如果没有子节点，就把子节点清空为null
		if (list.size() == 0) {
			return null;
		}
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
	public IndexedCollection searchRelCusList(String cusId, String cusType) throws EMPException {
		CusRelTreeAgent agent = (CusRelTreeAgent) this.getAgentInstance("CusRel");
		List<CusTree> list = new Vector<CusTree>();
		IndexedCollection relCusListIColl = new IndexedCollection();
		// 个人客户的客户类型以Z开头
		if(cusType != null && cusType.startsWith("Z")){
			list.addAll(agent.getRelCus(cusId));
			list.addAll(agent.getKeyCom(cusId));
		}else {
			list.addAll(agent.getKeyCus(cusId));
			list.addAll(agent.getComAndIndivFromApital(cusId));
		}
		list.addAll(agent.getComAndIndivFromInvest(cusId));
		if(list !=null && list.size()>0){
			for(int i=0;i<list.size();i++){
			String relCusName = list.get(i).getNodeName();
			String relCusInfo = list.get(i).getNodeInfo();
			KeyedCollection temp = new KeyedCollection();
			temp.addDataField("rel_cus_name", relCusName+relCusInfo);
			relCusListIColl.add(temp);
			}
		}	
		relCusListIColl.setName("CusRelInfoList");
		return relCusListIColl;
	}
}
