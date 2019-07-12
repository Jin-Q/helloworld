package com.yucheng.cmis.biz01line.cus.cuscom.component;

import com.yucheng.cmis.pub.PUBConstant;
import com.yucheng.cmis.biz01line.cus.cuscom.agent.CusComRelInvestAgent;
import com.yucheng.cmis.biz01line.cus.cuscom.domain.*;
import com.yucheng.cmis.pub.CMISComponent;
import com.yucheng.cmis.pub.exception.ComponentException;
import com.ecc.emp.core.EMPException;
 


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
public class CusComRelInvestComponent extends CMISComponent {


	public String checkExist(CusComRelInvest cusComRelInvest) throws EMPException, ComponentException {
		// 返回信息
		String strReturnMessage;
		CusComRelInvest pFromDb=null;
		CusComRelInvestAgent cusComRelInvestAgent = (CusComRelInvestAgent) this.getAgentInstance(PUBConstant.CUSCOMRELINVEST);
		pFromDb = cusComRelInvestAgent.checkExist(cusComRelInvest);
		if(pFromDb.getCusId()==null){	
			strReturnMessage="no";
		}else{
			strReturnMessage="yes";
		}
		return strReturnMessage;
	};
	
	public CusComRelInvest getCusComRelInvest(String cusId,String cusIdRel) throws ComponentException{
		CusComRelInvestAgent cusComRelInvestAgent = (CusComRelInvestAgent) this.getAgentInstance(PUBConstant.CUSCOMRELINVEST);
		CusComRelInvest ccri = null;
		ccri = cusComRelInvestAgent.getCusComrelComRelInvest(cusId,cusIdRel);
		return ccri;
	}
}
