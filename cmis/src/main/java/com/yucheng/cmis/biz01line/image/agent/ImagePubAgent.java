package com.yucheng.cmis.biz01line.image.agent;

import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.yucheng.cmis.biz01line.image.dao.ImagePubDao;
import com.yucheng.cmis.pub.CMISAgent;
import com.yucheng.cmis.pub.exception.AgentException;
import com.yucheng.cmis.pub.exception.DaoException;

public class ImagePubAgent extends CMISAgent {
	/**
	 * 无返回值的命名sql调用
	 * @param kcoll  传值kcoll
	 */
	public void delSqlNoReturn(KeyedCollection kcoll) throws AgentException {
		try {
			ImagePubDao cmisDao = (ImagePubDao)this.getDaoInstance("ImagePubDao");
			cmisDao.delSqlNoReturn(kcoll);
		} catch (Exception e) {
			throw new AgentException(e.getMessage());
		}
	}
	
	/**
	 * 带返回值的命名sql调用
	 * @param kcoll  传值kcoll
	 * @return result_kcoll 返回kcoll
	 */
	public KeyedCollection delSqlReturnKcoll( KeyedCollection kcoll) throws AgentException {
		try {
			ImagePubDao cmisDao = (ImagePubDao)this.getDaoInstance("ImagePubDao");
			return cmisDao.delSqlReturnKcoll( kcoll);
		} catch (Exception e) {
			throw new AgentException(e.getMessage());
		}
	}
	
	/**
	 * 返回无分页icoll的命名sql调用
	 * @param kcoll  传值kcoll
	 * @return result_kcoll 返回icoll
	 * @throws DaoException 
	 */	
	public IndexedCollection delSqlReturnIcoll( KeyedCollection kcoll)throws AgentException {
		try {
			ImagePubDao cmisDao = (ImagePubDao)this.getDaoInstance("ImagePubDao");
			return cmisDao.delSqlReturnIcoll(kcoll);
		} catch (Exception e) {
			throw new AgentException(e.getMessage());
		}
	}
	
}