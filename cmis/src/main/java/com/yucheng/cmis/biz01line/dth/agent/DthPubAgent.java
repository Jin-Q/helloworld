package com.yucheng.cmis.biz01line.dth.agent;

import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.yucheng.cmis.biz01line.dth.dao.DthPubDao;
import com.yucheng.cmis.pub.CMISAgent;
import com.yucheng.cmis.pub.exception.AgentException;
import com.yucheng.cmis.pub.exception.DaoException;

public class DthPubAgent extends CMISAgent {
	/**
	 * 行长驾驶舱专用无返回值的命名sql调用
	 * @param kcoll  传值kcoll
	 */
	public void delSqlNoReturn(KeyedCollection kcoll) throws AgentException {
		try {
			DthPubDao cmisDao = (DthPubDao)this.getDaoInstance("DthPubDao");
			cmisDao.delSqlNoReturn(kcoll);
		} catch (Exception e) {
			throw new AgentException(e.getMessage());
		}
	}
	
	/**
	 * 行长驾驶舱专用带返回值的命名sql调用
	 * @param kcoll  传值kcoll
	 * @return result_kcoll 返回kcoll
	 */
	public KeyedCollection delSqlReturnKcoll( KeyedCollection kcoll) throws AgentException {
		try {
			DthPubDao cmisDao = (DthPubDao)this.getDaoInstance("DthPubDao");
			return cmisDao.delSqlReturnKcoll( kcoll);
		} catch (Exception e) {
			throw new AgentException(e.getMessage());
		}
	}
	
	/**
	 * 行长驾驶舱专用返回无分页icoll的命名sql调用
	 * @param kcoll  传值kcoll
	 * @return result_kcoll 返回icoll
	 * @throws DaoException 
	 */	
	public IndexedCollection delSqlReturnIcoll( KeyedCollection kcoll)throws AgentException {
		try {
			DthPubDao cmisDao = (DthPubDao)this.getDaoInstance("DthPubDao");
			return cmisDao.delSqlReturnIcoll(kcoll);
		} catch (Exception e) {
			throw new AgentException(e.getMessage());
		}
	}
	
}