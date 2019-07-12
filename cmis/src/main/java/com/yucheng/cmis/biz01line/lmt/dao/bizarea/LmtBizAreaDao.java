package com.yucheng.cmis.biz01line.lmt.dao.bizarea;

import java.sql.Connection;

import com.ecc.emp.data.KeyedCollection;
import com.yucheng.cmis.pub.CMISDao;
import com.yucheng.cmis.pub.dao.SqlClient;

public class LmtBizAreaDao extends CMISDao{

	/**
	 * 根据客户码查询客户是否在圈商或联保小组内内
	 * @param cusId  客户码
	 * @throws Exception 
	 */
	public KeyedCollection queryJointAreaFlag(String cusId, Connection connection) throws Exception {
		
		KeyedCollection kColl = (KeyedCollection) SqlClient.queryFirst("QueryJointAreaFlag", cusId, null, connection);
		
		return kColl;
	}
	
	/**小组提交将数据插入台账表
	 * @param serno
	 */
	public void doSubmit(String serno ,Connection connection) {
		try {
			SqlClient.executeUpd("insertLmtNameList2Details", serno, null, null, connection);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
