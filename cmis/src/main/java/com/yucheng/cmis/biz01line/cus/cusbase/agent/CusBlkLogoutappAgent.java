package com.yucheng.cmis.biz01line.cus.cusbase.agent;

import com.ecc.emp.core.EMPException;
import com.yucheng.cmis.biz01line.cus.cusbase.domain.CusBlkLogoutapp;
import com.yucheng.cmis.pub.CMISAgent;
import com.yucheng.cmis.pub.PUBConstant;
import com.yucheng.cmis.pub.exception.AgentException;

public class CusBlkLogoutappAgent extends CMISAgent {

	
	/**
	    * 根据业务流水号获取  黑名单客户注销申请domian
	    * @param serno   业务流水号
	    * @return   pCusBlkLogoutapp  黑名单客户注销申请domian
	    * @throws AgentException 
	    */
	public CusBlkLogoutapp findCusBlkLogoutapp(String serno) throws EMPException {

		CusBlkLogoutapp pCusBlkLogoutapp = new CusBlkLogoutapp();
		pCusBlkLogoutapp = (CusBlkLogoutapp) this.findCMISDomainByKeyword(pCusBlkLogoutapp, PUBConstant.CUSBLKLOGOUTAPP, serno);
		return pCusBlkLogoutapp;
	}
	
}
