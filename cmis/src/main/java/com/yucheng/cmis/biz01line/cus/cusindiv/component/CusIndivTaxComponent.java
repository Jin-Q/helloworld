package com.yucheng.cmis.biz01line.cus.cusindiv.component;

import com.ecc.emp.core.EMPException;
import com.yucheng.cmis.biz01line.cus.cusindiv.agent.CusIndivTaxAgent;
import com.yucheng.cmis.biz01line.cus.cusindiv.domain.CusIndivTax;
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
public class CusIndivTaxComponent extends CMISComponent {

	public String checkExist(CusIndivTax cusIndivTax) throws EMPException, ComponentException {
		// 返回信息
		String strReturnMessage;
		CusIndivTax pFromDb=null;
		CusIndivTaxAgent cusIndivTaxAgent = (CusIndivTaxAgent) this.getAgentInstance(PUBConstant.CUSINDIVTAX);
		pFromDb = cusIndivTaxAgent.checkExist(cusIndivTax);
		if(pFromDb.getCusId()==null){	
			strReturnMessage="no";
		}else{
			strReturnMessage="yes";
		}
		return strReturnMessage;
	};
}
