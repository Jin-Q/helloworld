package com.yucheng.cmis.biz01line.cus.cusbase.component;

import com.ecc.emp.core.EMPException;
import com.yucheng.cmis.biz01line.cus.cusbase.agent.CusBlkLogoutappAgent;
import com.yucheng.cmis.biz01line.cus.cusbase.domain.CusBlkLogoutapp;
import com.yucheng.cmis.pub.CMISComponent;
import com.yucheng.cmis.pub.PUBConstant;

public class CusBlkLogoutappComponent extends CMISComponent {
	
 
   /**
    * 根据业务流水号获取  黑名单客户注销申请domian
    * @param serno   业务流水号
    * @return   pCusBlkLogoutapp  黑名单客户注销申请domian
 * @throws EMPException 
    */
	public CusBlkLogoutapp findCusBlkLogoutapp(String serno) throws EMPException{
			
		CusBlkLogoutappAgent cusBlkLogoutappAgent = (CusBlkLogoutappAgent) this.getAgentInstance(PUBConstant.CUSBLKLOGOUTAPP);
		CusBlkLogoutapp pCusBlkLogoutapp = new CusBlkLogoutapp();
		pCusBlkLogoutapp=cusBlkLogoutappAgent.findCusBlkLogoutapp(serno);
		return pCusBlkLogoutapp;	
	}
}
