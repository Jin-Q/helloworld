package com.yucheng.cmis.biz01line.acc.agent;

import java.sql.SQLException;

import com.ecc.emp.data.IndexedCollection;
import com.yucheng.cmis.biz01line.acc.accPub.AccConstant;
import com.yucheng.cmis.biz01line.acc.dao.AccDao;
import com.yucheng.cmis.pub.CMISAgent;
import com.yucheng.cmis.pub.dao.SqlClient;
import com.yucheng.cmis.pub.exception.ComponentException;
import java.sql.Connection;

public class AccAgent extends CMISAgent{
	/**
	 * 通过合同编号查询台账信息
	 * @param guar_cont_no_str 担保合同编号
	 * @throws ComponentException
	 * @throws SQLException 
	 */
	public IndexedCollection getAcc(String cont_no_str,Connection connection) throws Exception{
		AccDao cmisDao = (AccDao)this.getDaoInstance(AccConstant.ACCDAO);
		IndexedCollection iColl = cmisDao.getAcc(cont_no_str,connection);
		return iColl; 
	}
	/**
	 * 通过客户码查询台账信息
	 * @param cus_id 客户码
	 * @throws ComponentException
	 * @throws SQLException 
	 */
	public IndexedCollection getAccPop(String cus_id,String openDay,Connection connection) throws Exception{
		AccDao cmisDao = (AccDao)this.getDaoInstance(AccConstant.ACCDAO);
		IndexedCollection iColl = cmisDao.getAccPop(cus_id,openDay, connection); 
		return iColl; 	 
	}
}
