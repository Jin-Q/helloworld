package com.yucheng.cmis.biz01line.cus.cuscom.component;

import com.yucheng.cmis.pub.PUBConstant;
import com.yucheng.cmis.biz01line.cus.cuscom.agent.CusComFinaStockAgent;
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
public class CusComFinaStockComponent extends CMISComponent {


/**
 * 
 * @param cusComFinaStock
 * @return
 * @throws EMPException
 * @throws ComponentException
 */
	public String checkExist(CusComFinaStock cusComFinaStock) throws EMPException, ComponentException {
		// 返回信息,默认为存在
		String strReturnMessage;
		CusComFinaStock pFromDb=null;
		CusComFinaStockAgent cusComFinaStockAgent = (CusComFinaStockAgent) this.getAgentInstance(PUBConstant.CUSCOMFINASTOCK);
		pFromDb = cusComFinaStockAgent.checkExist(cusComFinaStock);
		if(pFromDb.getCusId()==null){	
			//strReturnMessage =cusComAptitudeAgent.insert(pcusComAptitude);
			strReturnMessage="no";
		}else{
			strReturnMessage="yes";
		}
		return strReturnMessage;
	};
}
