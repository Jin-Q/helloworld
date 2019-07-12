package com.yucheng.cmis.biz01line.cus.cussubmitinfo.component;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.yucheng.cmis.biz01line.cus.cussubmitinfo.agent.CusSubmitInfoAgent;
import com.yucheng.cmis.pub.CMISAgentFactory;
import com.yucheng.cmis.pub.CMISComponent;
import com.yucheng.cmis.pub.exception.AgentException;
import com.yucheng.cmis.pub.exception.DaoException;

public class CusSubmitInfoComponent extends CMISComponent {
	
	private final String comId = "CusSubmitInfo";
	/**
	 * 得到任务最少的用户
	 * @param connection 
	 * @param context 
	 * @param curOrg  
	 * @return
	 * @throws AgentException 
	 */
	public String getLeastTaskUserId(Context context, Connection connection) throws Exception {
		String userId = null;
			
		CusSubmitInfoAgent crdAgent = (CusSubmitInfoAgent)CMISAgentFactory.getAgentFactoryInstance().getAgentInstance(comId);
		try {
			userId = crdAgent.getCurOrgLeastTaskUser(context,connection);
		} catch (DaoException e) {
			throw e;
		}
		return userId;
	}
	
}
