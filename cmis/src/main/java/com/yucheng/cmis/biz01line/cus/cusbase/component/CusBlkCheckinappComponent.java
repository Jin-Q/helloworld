package com.yucheng.cmis.biz01line.cus.cusbase.component;

import com.ecc.emp.core.EMPException;
import com.yucheng.cmis.biz01line.cus.cusbase.agent.CusBlkCheckinappAgent;
import com.yucheng.cmis.biz01line.cus.cusbase.domain.CusBlkCheckinapp;
import com.yucheng.cmis.pub.CMISComponent;
import com.yucheng.cmis.pub.PUBConstant;

public class CusBlkCheckinappComponent extends CMISComponent {
	
 
   /**
    * 根据业务流水号获取  黑名单客户进入申请domian
    * @param serno   业务流水号
    * @return   pCusBlkLogoutapp  黑名单客户进入申请domian
 * @throws EMPException 
    */
	public CusBlkCheckinapp findCusBlkCheckinapp(String serno) throws EMPException{
			
		CusBlkCheckinappAgent cusBlkCheckinappAgent = (CusBlkCheckinappAgent) this.getAgentInstance(PUBConstant.CUSBLKCHECKINAPP);
		CusBlkCheckinapp pCusBlkCheckinapp = new CusBlkCheckinapp();
		pCusBlkCheckinapp = cusBlkCheckinappAgent.findCusCusBlkCheckinapp(serno);
		return pCusBlkCheckinapp;	
	}
}
