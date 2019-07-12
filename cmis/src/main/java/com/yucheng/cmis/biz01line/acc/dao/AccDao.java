package com.yucheng.cmis.biz01line.acc.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

import com.ecc.emp.data.IndexedCollection;
import com.yucheng.cmis.pub.CMISDao;
import com.yucheng.cmis.pub.dao.SqlClient;
import com.yucheng.cmis.pub.exception.ComponentException;

public class AccDao extends CMISDao{
	/**
	 * 通过合同编号查询台账信息
	 * @param guar_cont_no_str 担保合同编号
	 * @throws ComponentException
	 * @throws SQLException 
	 */
	public IndexedCollection getAcc(String cont_no_str,Connection connection) throws Exception{
		IndexedCollection iColl = SqlClient.queryList4IColl("getAcc", cont_no_str, connection);
	    return iColl; 	 
	}
	/**
	 * 通过客户码查询台账信息
	 * @param cus_id 客户码
	 * @throws ComponentException
	 * @throws SQLException 
	 */
	public IndexedCollection getAccPop(String cus_id,String openDay,Connection connection) throws Exception{
		
		HashMap<String,String> hashMap = new HashMap<String,String>(); 
		hashMap.put("cus_id", cus_id);
		hashMap.put("openDay", openDay);
		IndexedCollection iColl = SqlClient.queryList4IColl("getAccPop", hashMap, connection);
		return iColl;
	}
	
}
