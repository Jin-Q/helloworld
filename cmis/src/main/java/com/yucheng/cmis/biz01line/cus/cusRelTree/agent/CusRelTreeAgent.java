package com.yucheng.cmis.biz01line.cus.cusRelTree.agent;

import java.util.HashMap;
import java.util.List;

import com.ecc.emp.core.EMPException;
import com.yucheng.cmis.biz01line.cus.cusRelTree.dao.CusRelTreeDao;
import com.yucheng.cmis.biz01line.cus.cusRelTree.domain.CusTree;
import com.yucheng.cmis.pub.CMISAgent;
import com.yucheng.cmis.pub.exception.AgentException;
 
 

public class CusRelTreeAgent extends CMISAgent {
	/**
	 * 取得企业关键人
	 * 
	 * @param cusTree
	 * @return
	 * @throws EMPException
	 */
	public List<CusTree> getKeyCus(String cusId) throws EMPException {
		CusRelTreeDao dao = new CusRelTreeDao();
		return dao.getKeyCus(cusId, this.getConnection());
	}

	/**
	 * 取得关键人配偶、父母、子女、兄弟姐妹
	 * 
	 * @param cusTree
	 * @return
	 */
	public List<CusTree> getRelCus(String cusId) throws EMPException {
		CusRelTreeDao dao = new CusRelTreeDao();
		return dao.getRelCus(cusId, this.getConnection());
	}

	/**
	 * 取得关键人所在的企业
	 * 
	 * @param cusTree
	 * @return
	 */
	public List<CusTree> getKeyCom(String cusId) throws EMPException {
		CusRelTreeDao dao = new CusRelTreeDao();
		return dao.getKeyCom(cusId, this.getConnection());
	}

	/**
	 * 从资本构成信息查询到的企业和法人
	 * 
	 * @param cusTree
	 * @return
	 */
	public List<CusTree> getComAndIndivFromApital(String cusId)
			throws EMPException {
		CusRelTreeDao dao = new CusRelTreeDao();
		return dao.getComAndIndivFrolistital(cusId, this.getConnection());
	}

	/**
	 * 从对外投资信息查询到的企业
	 * 
	 * @param cusTree
	 * @return
	 */
	public List<CusTree> getComAndIndivFromInvest(String cusId)
			throws EMPException {
		CusRelTreeDao dao = new CusRelTreeDao();
		return dao.getComAndIndivFromInvest(cusId, this.getConnection());
	}
	
	public int queryIsGrpcus(String cusId) throws AgentException{
		int count = 0;
		String sql  = " select count(*) ct from cus_grp_member where cus_id='"+cusId+"'";
		HashMap<String, String> hm  = this.queryDataOfMapByCondition(sql);
		
		String ct = hm.get("ct");
		
		if(ct==null||"".equals(ct.trim())){
		}else{
			count = Integer.parseInt(ct);
		}
		return count;
	}
	
	/**
	 * 从所有对公客户里查找同时被第三方控制的企业
	 * 
	 * @param cusTree
	 * @return
	 */
//	public List<CusTree> searchRelThirdCusList(String cusId)
//			throws EMPException {
//		CusRelTreeDao dao = new CusRelTreeDao();
//		return dao.searchRelThirdCusList(cusId, this.getConnection());
//	}
	/***
	 * 获取企业股东
	 * @param cusId
	 * @return
	 * @throws EMPException
	 */
	public List<String> getComRelCus(String cusId) throws EMPException{
		CusRelTreeDao dao = new CusRelTreeDao();
		return dao.getComRelCus(cusId, this.getConnection());
		
	}
}
