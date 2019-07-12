package com.yucheng.cmis.biz01line.cus.cusindiv.component;

import com.ecc.emp.core.EMPException;
import com.yucheng.cmis.biz01line.cus.cusindiv.agent.CusIndivSocRelAgent;
import com.yucheng.cmis.biz01line.cus.cusindiv.domain.CusIndivSocRel;
import com.yucheng.cmis.pub.CMISComponent;
import com.yucheng.cmis.pub.PUBConstant;
import com.yucheng.cmis.pub.exception.AgentException;
import com.yucheng.cmis.pub.exception.ComponentException;

/**
 * 
 * @Classname CusComComponent
 * @Version 0.1
 * @Since 2008-9-18
 * @Copyright yuchengtech
 * @Author wqgang
 * @Description：
 * @Lastmodified 2008-9-18
 * @Author wqgang
 */
public class CusIndivSocRelComponent extends CMISComponent {

	public String checkExist(CusIndivSocRel cusIndivSocRel)
			throws EMPException, ComponentException {
		// 返回信息
		String strReturnMessage;
		CusIndivSocRel pFromDb = new CusIndivSocRel();
		CusIndivSocRelAgent cusIndivSocRelAgent = (CusIndivSocRelAgent) this.getAgentInstance(PUBConstant.CUSINDIVSOCREL);
		pFromDb = cusIndivSocRelAgent.checkExist(cusIndivSocRel);
		if (pFromDb.getCusId() == null) {
			strReturnMessage = "no";
		} else {
			strReturnMessage = "yes";
		}
		return strReturnMessage;
	};

	/**
	 * 检查客户父母是否已经存在两个
	 * @param cusId
	 * @return
	 * @throws EMPException
	 * @throws ComponentException
	 */
	public String checkParents(String cusId)
			throws EMPException, Exception {
		// 返回信息
		String strReturnMessage;
		CusIndivSocRelAgent cusIndivSocRelAgent = (CusIndivSocRelAgent) this.getAgentInstance(PUBConstant.CUSINDIVSOCREL);
		strReturnMessage = cusIndivSocRelAgent.checkParents(cusId);
		return strReturnMessage;
	};
	
	/**
	 * 根据主客户ID检查该客户是否存在配偶信息
	 * 
	 * @param cusId
	 * @return
	 * @throws EMPException
	 */
	public String checkExist(String cusId) throws Exception {
		String strReturnMessage = null;
		CusIndivSocRelAgent cusIndivSocRelAgent = (CusIndivSocRelAgent) this.getAgentInstance(PUBConstant.CUSINDIVSOCREL);
		strReturnMessage = cusIndivSocRelAgent.checkExist(cusId);
		return strReturnMessage;
	}

	public int giveValueToCusIndivSocRel(CusIndivSocRel cusIndivSocRel)
			throws Exception {

//		CusIndivAgent cusIndivAgent = (CusIndivAgent) this.getAgentInstance(PUBConstant.CUSINDIV);// 对私的agent
//		CusBaseAgent cusBaseAgent = (CusBaseAgent) this.getAgentInstance(PUBConstant.CUSBASE);

		CusIndivSocRelAgent cusIndivSocRelAgent = (CusIndivSocRelAgent) this.getAgentInstance(PUBConstant.CUSINDIVSOCREL);

		String cusId = cusIndivSocRel.getCusId();// 主客户码
		String cusIdRel = cusIndivSocRel.getCusIdRel();// 关联客户码
		String rel = cusIndivSocRel.getIndivCusRel();// 与客户关系
		String flag = "n";
//		CusBase mainCus = new CusBase();
//		CusBase relCus = new CusBase();
//		CusIndiv mainInd = new CusIndiv();
//		CusIndiv relInd = new CusIndiv();
		try {
//			mainCus = cusBaseAgent.findCusBaseById(cusId);// 主客户对私客户信息
//			mainInd = cusIndivAgent.findCusIndivByCusId(cusId);
//			relCus = cusBaseAgent.findCusBaseById(cusIdRel);// 获得关联客户的对私客户信息
//			relInd = cusIndivAgent.findCusIndivByCusId(cusIdRel);
//			relCus.setCustMgr(mainCus.getCustMgr());
//			relCus.setMainBrId(mainCus.getMainBrId());
			flag = cusIndivSocRelAgent.checkSocRelExist(cusId, rel,cusIdRel);// 检查主客户的社会关系
			if (flag.equals("y")) {
				cusIndivSocRelAgent.update(cusIndivSocRel);
			} else {
				cusIndivSocRelAgent.insert(cusIndivSocRel);
			}
//			if(rel.equals("1")){
//			}
//			if (rel.equals("2")) {
//				// 与父母的社会关系
//				flag = cusComMngFamilyAgent.checkSocRelExist(cusId, rel,cusIdRel);// 检查主客户的社会关系
//				if (flag.equals("y")) {
//					cusComMngFamilyAgent.update(cusIndivSocRel);
//				} else {
//					cusComMngFamilyAgent.insert(cusIndivSocRel);
//				}
//				flag = "n";
//				flag = cusComMngFamilyAgent.checkSocRelExist(cusIdRel, "3",cusId);// 检查父母的社会关系
//				if (flag.equals("y")) {
//					cusComMngFamilyAgent.update(cusIndivSocRel);
//				} else {
//					cusComMngFamilyAgent.insert(cusIndivSocRel);
//				}
//			}
//			if (rel.equals("3")) {
//				// 子女的客户关系
//				flag = cusComMngFamilyAgent.checkSocRelExist(cusIdRel, "3",cusId);// 检查与主客户的社会关系
//				if (flag.equals("y")) {
//					cusComMngFamilyAgent.update(cusIndivSocRel);
//				} else {
//					cusComMngFamilyAgent.insert(cusIndivSocRel);
//				}
//				flag = "n";
			/*	if (!mainCus.getCusIdRel().equals("")
						|| mainCus.getCusIdRel() != null) {*/
					// 检查客户配偶的社会关系
//					spsCus = cusIndivAgent.findCusIndivByCusId(spsCusId);
//					flag = cusComMngFamilyAgent.checkSocRelExist(cusIdRel, "2",spsCus.getCertCode());
//					if (flag.equals("y")) {
//						cusComMngFamilyAgent.update(spsCus, cusIdRel, "2");
//					} else {
//						cusComMngFamilyAgent.insert(spsCus, cusIdRel, "2");
//					}
//					flag = cusComMngFamilyAgent.checkSocRelExist(spsCusId, "3",relCertCode);
//					spsCus = cusIndivAgent.findCusIndivByCusId(spsCusId);
//					if (flag.equals("y")) {
//						cusComMngFamilyAgent.update(relCus, spsCusId, "3");
//					} else {
//						cusComMngFamilyAgent.insert(relCus, spsCusId, "3");
//					}
				//}
//			}
		} catch (EMPException e) {
			 throw e;
		}

		return 0;
	}

	public int deleteCusIndivSocRel(String cusId, String cusIdRel,String relType) throws Exception {
		int i = 0;
		CusIndivSocRelAgent cusIndivSocRelAgent = (CusIndivSocRelAgent) this
				.getAgentInstance(PUBConstant.CUSINDIVSOCREL);
		i = cusIndivSocRelAgent.deleteCusIndivSocRel(cusId, cusIdRel, relType);
		return i;
	}
	
	/**
	 * 根据主客户ID得到配偶的CusIndivSocRel的对象
	 * @param cusid 主客户ID关联 CusIndivSocRel.cus_id_rel 字段
	 * @return CusIndivSocRel
	 * @throws AgentException 
	 */
	public CusIndivSocRel getSpouseCusIndivSocRel(String cusid){
		CusIndivSocRel domain = new CusIndivSocRel();
		try{
			CusIndivSocRelAgent cusIndivSocRelAgent = (CusIndivSocRelAgent) this
				.getAgentInstance(PUBConstant.CUSINDIVSOCREL);
			domain = cusIndivSocRelAgent.getSpouseCusIndivSocRel(cusid);
			
		}catch(Exception e){}
		return domain;
		
	}
}
