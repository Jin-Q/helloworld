package com.yucheng.cmis.biz01line.cus.interfaces.impl;


import java.util.List;

import com.ecc.emp.core.EMPException;
import com.yucheng.cmis.biz01line.cus.cusbase.agent.CusBlackListAgent;
import com.yucheng.cmis.biz01line.cus.cusbase.component.CusBaseComponent;
import com.yucheng.cmis.biz01line.cus.cusbase.domain.CusBase;
import com.yucheng.cmis.biz01line.cus.cuscom.component.CusComComponent;
import com.yucheng.cmis.biz01line.cus.cuscom.domain.CusCom;
import com.yucheng.cmis.biz01line.cus.cusindiv.component.CusIndivComponent;
import com.yucheng.cmis.biz01line.cus.cusindiv.domain.CusIndiv;
import com.yucheng.cmis.biz01line.cus.group.agent.CusGrpInfoAgent;
import com.yucheng.cmis.biz01line.cus.group.component.CusGrpInfoComponent;
import com.yucheng.cmis.biz01line.cus.group.domain.CusGrpInfo;
import com.yucheng.cmis.biz01line.cus.interfaces.CustomIface;
import com.yucheng.cmis.pub.CMISComponent;
import com.yucheng.cmis.pub.CMISDomain;
import com.yucheng.cmis.pub.PUBConstant;
import com.yucheng.cmis.pub.exception.ComponentException;

public class CustomIfaceImpl extends CMISComponent implements CustomIface {

	public CusCom getCusCom(String cusId) throws ComponentException {
		
		CusCom pCusCom = new CusCom();
		
		CusBaseComponent cusBaseComponent = (CusBaseComponent)this.getOtherComponentInstance(PUBConstant.CUSBASE);
		try {
			pCusCom = cusBaseComponent.getCusCom(cusId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return pCusCom;
	}

	public CusIndiv getCusIndiv(String cusId) throws EMPException {
		CusIndiv pcusIndiv = new CusIndiv();
		CusIndivComponent cusIndivComponent = (CusIndivComponent)this.getOtherComponentInstance(PUBConstant.CUSINDIV);
		pcusIndiv = cusIndivComponent.getCusIndiv(cusId);
		return pcusIndiv;
	}
	
	/**
	 * 根据提供的客户码查询客户信用等级信息
	 * @param cusId 	          客户码
	 * @return crdGrade           客户信用等级信息
	 * @throws ComponentException 
	 */
	public String getComCrdGrade(String cusId) throws ComponentException {
		String crdGrade = null;
		String cusType = null;
		CusBaseComponent cusBaseComponent = (CusBaseComponent)this.getOtherComponentInstance(PUBConstant.CUSBASE);
		CusBase cusBase = cusBaseComponent.getCusBase(cusId);
		cusType = cusBase.getCusType();
		if("A2".equals(cusType)){	//融资性担保公司
			crdGrade = cusBase.getGuarCrdGrade();
		}else{
			crdGrade = cusBase.getCusCrdGrade();
		}
		return crdGrade;
	}
	
	/**
	 * 根据客户码获得其所在的集团客户DOMAIN
	 * @throws ComponentException 
	 * */
	public CusGrpInfo getGrpNoByCusId(String cusId) throws Exception {
		CusGrpInfoComponent cgic = (CusGrpInfoComponent)this.getOtherComponentInstance(PUBConstant.CUSGRPINFO);
		try {
			List<CusGrpInfo> list = cgic.findCusGrpInfoByMemCusId(cusId);
			if(list==null || list.size()<=0){
				return null;
			}
			return list.get(0);
		} catch (EMPException e) {
			throw new ComponentException(e.getMessage());
		}
	}


	/**
	 * 通过客户码获取客户基表信息Domain
	 */
	public CusBase getCusBase(String cusId) throws ComponentException {
		CusBaseComponent cusBaseComponent = (CusBaseComponent)this.getOtherComponentInstance(PUBConstant.CUSBASE);
		CusBase cusBase = cusBaseComponent.getCusBase(cusId);
		return cusBase;
	}
	/**
	 * 修改客户信息根据CusIndiv对象更新数据库数据
	 * 
	 * @param cusIndiv
	 * @return String 返回String类型用于描述更新状态
	 * @throws EMPException
	 */
	public String modifyCusBase(CusBase cusBase) throws EMPException,
			ComponentException{
		CusBaseComponent cusBaseComponent = (CusBaseComponent)this.getOtherComponentInstance(PUBConstant.CUSBASE);
		return cusBaseComponent.modifyCusBase(cusBase);
	}
	
	/**
	 * 通过condition 查询不宜客户情况
	 * @param certType
	 * @param certCode
	 * @return
	 * @throws EMPException
	 */
	public List<CMISDomain> getCusBlkListByCondition(String condition)throws EMPException{
		CusBlackListAgent cusBlkListAgent = (CusBlackListAgent)this.getAgentInstance("CusBlkListAgent");
		return cusBlkListAgent.getCusBlkListByCondition(condition);
	}
	
	public CusGrpInfo getCusGrpInfoDomainByGrpNo(String grpNo) throws Exception
	{
		CusGrpInfoComponent cgic = (CusGrpInfoComponent)this.getComponent("CusGrpInfo");

		return cgic.getCusGrpInfoDomainByGrpNo(grpNo);
	}


	/**
	 * 根据CUS_ID获得其所在集团的DOMAIN
	 * */
	public CusGrpInfo getCusGrpInfoDomainByCusId(String cus_id)	throws ComponentException {
		CusGrpInfoAgent cgic = (CusGrpInfoAgent)this.getAgentInstance("CusGrpInfo");
		return cgic.getCusGrpInfoDomainByCusId(cus_id);
	}
	
	/**
	 * 根据CUS_ID,CUS_TYPE获取企业客户的高管信息中的法人代表或个人客户的社会信息中的配偶信息
	 * @author zwhu
	 * @throws ComponentException 
	 */
	public List<CMISDomain> queryRelCusListByCusId(String cusId,String cusType)
			throws ComponentException {
		if(cusType.equals("Z11")||cusType.equals("Z12")){
			CusIndivComponent cusIndivComponent = (CusIndivComponent)this.getOtherComponentInstance(PUBConstant.CUSINDIV);
			return cusIndivComponent.queryRelCusListByCusId(cusId, cusType);
		}
		else{
			CusComComponent cusComComponent = (CusComComponent)this.getComponent("CusCom");
			return cusComComponent.queryRelCusListByCusId(cusId,cusType);
		}	
	}
	/**
	 * 查找实际控制人信息
	 * @author zwhu
	 * @throws ComponentException 
	 */
	public List<CMISDomain> queryControlRelCusListByCusId(String cusId)
			throws ComponentException {
		CusComComponent cusComComponent = (CusComComponent)this.getComponent("CusCom");
		return cusComComponent.queryControlRelCusListByCusId(cusId);
	}

	public List<String> queryRelCusIdListByCusId(String cusId)
			throws ComponentException {
		// TODO Auto-generated method stub
		return null;
	}
	
}
