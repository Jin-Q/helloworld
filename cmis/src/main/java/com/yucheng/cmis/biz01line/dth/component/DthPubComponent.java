package com.yucheng.cmis.biz01line.dth.component;

import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.yucheng.cmis.biz01line.dth.agent.DthPubAgent;
import com.yucheng.cmis.pub.CMISComponent;
import com.yucheng.cmis.pub.exception.AgentException;
import com.yucheng.cmis.pub.exception.DaoException;

public class DthPubComponent extends CMISComponent {
	/**
	 * 行长驾驶舱专用无返回值的命名sql调用
	 * @param kcoll  传值kcoll
	 */
	public void delSqlNoReturn(KeyedCollection kcoll) throws AgentException {
		try {
			DthPubAgent cmisAgent = (DthPubAgent)this.getAgentInstance("DthPubAgent");
			cmisAgent.delSqlNoReturn(kcoll);
		} catch (Exception e) {
			throw new AgentException(e.getMessage());
		}
	}
	
	/**
	 * 行长驾驶舱专用带返回值的命名sql调用
	 * @param kcoll  传值kcoll
	 * @return result_kcoll 返回kcoll
	 */
	public KeyedCollection delSqlReturnKcoll(KeyedCollection kcoll) throws AgentException {
		try {
			DthPubAgent cmisAgent = (DthPubAgent)this.getAgentInstance("DthPubAgent");
			return cmisAgent.delSqlReturnKcoll(kcoll);
		} catch (Exception e) {
			throw new AgentException(e.getMessage());
		}
	}
	
	/**
	 * 行长驾驶舱专用带分页的命名sql调用
	 * @param kcoll  传值kcoll
	 * @return result_kcoll 返回icoll
	 * @throws DaoException 
	 */	
	public IndexedCollection delSqlReturnIcoll(KeyedCollection kcoll) throws AgentException {
		try {
			DthPubAgent cmisAgent = (DthPubAgent)this.getAgentInstance("DthPubAgent");
			return cmisAgent.delSqlReturnIcoll(kcoll);
		} catch (Exception e) {
			throw new AgentException(e.getMessage());
		}
	}

}