package com.yucheng.cmis.biz01line.iqp.component;

import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.yucheng.cmis.biz01line.iqp.agent.IqpBatchAgent;
import com.yucheng.cmis.biz01line.iqp.agent.IqpCreditChangeAppAgent;
import com.yucheng.cmis.biz01line.iqp.appPub.AppConstant;
import com.yucheng.cmis.biz01line.iqp.dao.IqpCreditChangeAppDao;
import com.yucheng.cmis.pub.CMISComponent;

public class IqpCreditChangeAppComponent extends CMISComponent {
	
	/**
	 * 查询合同和出账表 
	 * @param date 日期
	 * @return returnKColl 集合
	 * @throws Exception
	 */
	public IndexedCollection getIqpCreditChangeApp() throws Exception {
		IqpCreditChangeAppAgent creditAgent = (IqpCreditChangeAppAgent)this.getAgentInstance(AppConstant.IQPCREDITCHANGEAPPAGENT);
		return creditAgent.getIqpCreditChangeApp();
	}
	
	/**
	 * 担保变更新增时查询合同信息及客户经理绩效信息
	 * @return res_value 集合
	 * @throws Exception
	 */
	public IndexedCollection getIqpGuarChangeList(PageInfo pageInfo,String condition) throws Exception{
		IqpCreditChangeAppAgent creditAgent = (IqpCreditChangeAppAgent)this.getAgentInstance(AppConstant.IQPCREDITCHANGEAPPAGENT);
	    IndexedCollection res_value = creditAgent.getIqpGuarChangeList(pageInfo,condition);  
		return res_value;      
	}
	
}
