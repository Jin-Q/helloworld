package com.yucheng.cmis.biz01line.cus.cuscom.component;

import com.ecc.emp.core.EMPException;
import com.yucheng.cmis.biz01line.cus.cuscom.agent.CusComManagerAgent;
import com.yucheng.cmis.biz01line.cus.cuscom.domain.CusComManager;
import com.yucheng.cmis.pub.CMISComponent;
import com.yucheng.cmis.pub.PUBConstant;
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
public class CusComManagerComponent extends CMISComponent {


	public String checkExist(CusComManager cusComManager) throws EMPException, ComponentException {
		// 返回信息
		String strReturnMessage;
		CusComManager pFromDb=null;
		CusComManagerAgent cusComManagerAgent = (CusComManagerAgent) this.getAgentInstance(PUBConstant.CUSCOMMANAGER);
		pFromDb = cusComManagerAgent.checkExist(cusComManager);
		if(pFromDb.getCusId()==null){	
			strReturnMessage="no";
		}else{
			strReturnMessage="yes";
		}
		return strReturnMessage;
	};
	/**
	 * @author ZhouJianFent
	 * @param cusComManager
	 * @return
	 * @throws EMPException
	 * @throws ComponentException
	 */
	public String checkExistByMrgType(CusComManager cusComManager) throws Exception {
		// 返回信息
		String strReturnMessage;
		CusComManagerAgent cusComManagerAgent = (CusComManagerAgent) this.getAgentInstance(PUBConstant.CUSCOMMANAGER);
		if(cusComManagerAgent.checkExistByMrgType(cusComManager)){	
			strReturnMessage="yes";
		}else{
			strReturnMessage="no";
		}
		return strReturnMessage;
	};

	public CusComManager getCusComManager(String cusId) throws Exception{
		CusComManagerAgent ccma = (CusComManagerAgent)this.getAgentInstance(PUBConstant.CUSCOMMANAGER);
		return ccma.getCusComManager(cusId);
	}
}
