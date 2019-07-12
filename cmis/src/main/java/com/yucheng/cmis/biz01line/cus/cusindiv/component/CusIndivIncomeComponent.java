package com.yucheng.cmis.biz01line.cus.cusindiv.component;

import com.ecc.emp.core.EMPException;
import com.yucheng.cmis.biz01line.cus.cusindiv.agent.CusIndivIncomeAgent;
import com.yucheng.cmis.biz01line.cus.cusindiv.domain.CusIndivIncome;
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
public class CusIndivIncomeComponent extends CMISComponent {


	public String checkExist(CusIndivIncome cusIndivIncome) throws EMPException, ComponentException {
		// 返回信息
		String strReturnMessage;
		CusIndivIncome pFromDb=null;
		CusIndivIncomeAgent cusIndivIncomeAgent = (CusIndivIncomeAgent) this.getAgentInstance(PUBConstant.CUSINDIVINCOME);
		pFromDb = cusIndivIncomeAgent.checkExist(cusIndivIncome);
		if(pFromDb.getCusId()==null){	
			strReturnMessage="no";
		}else{
			strReturnMessage="yes";
		}
		return strReturnMessage;
	};
}
