package com.yucheng.cmis.biz01line.qry.component;

import org.apache.log4j.Logger;

import com.yucheng.cmis.pub.CMISComponent;
import com.yucheng.cmis.pub.CMISMessage;
import com.yucheng.cmis.pub.QryPubConstant;
import com.yucheng.cmis.pub.exception.AgentException;
import com.yucheng.cmis.pub.exception.ComponentException;
import com.yucheng.cmis.biz01line.qry.agent.QryAgent;

public class QryComponent extends CMISComponent {
	private static final Logger logger = Logger.getLogger(CMISComponent.class);
	/**
	 * 根据查询模板编号删除查询模板信息和其下配置的查询参数，返回值信息。
	 * @param tempNo
	 * @return
	 * @throws ComponentException
	 */
	public String deleteQryStaffByTempNo(String tempNo) throws ComponentException{
		//创建业务代理类
        QryAgent qryAgent;
		try {
			qryAgent = (QryAgent)
								this.getAgentInstance(QryPubConstant.QRYAGENT);
			qryAgent.deleteQryStaffByTempNo(tempNo);
		} catch (AgentException e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage(), e);
			throw new ComponentException(e);
		}
       return CMISMessage.SUCCESS;		
	}
}
