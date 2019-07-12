package com.yucheng.cmis.biz01line.cus.cusbase.agent;


import com.ecc.emp.core.EMPException;
import com.yucheng.cmis.biz01line.cus.cusbase.domain.CusBlkCheckinapp;
import com.yucheng.cmis.pub.CMISAgent;
import com.yucheng.cmis.pub.PUBConstant;
import com.yucheng.cmis.pub.exception.AgentException;

public class CusBlkCheckinappAgent extends CMISAgent {

   /**
    * 根据业务流水号获取  黑名单客户进入申请domian
    * @param serno   业务流水号
    * @return   pCusBlkLogoutapp  黑名单客户进入申请domian
    * @throws AgentException 
    */
	public CusBlkCheckinapp findCusCusBlkCheckinapp(String serno) throws EMPException {

		CusBlkCheckinapp pCusBlkCheckinapp = new CusBlkCheckinapp();
		pCusBlkCheckinapp = (CusBlkCheckinapp) this.findCMISDomainByKeyword(pCusBlkCheckinapp, PUBConstant.CUSBLKCHECKINAPP, serno);
		return pCusBlkCheckinapp;
	}
	
}
