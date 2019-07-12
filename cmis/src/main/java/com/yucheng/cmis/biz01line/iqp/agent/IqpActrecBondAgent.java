package com.yucheng.cmis.biz01line.iqp.agent;

import java.sql.Connection;
import java.sql.SQLException;

import com.yucheng.cmis.biz01line.iqp.dao.IqpActrecBondDao;
import com.yucheng.cmis.pub.CMISAgent;
import com.yucheng.cmis.pub.exception.ComponentException;

public class IqpActrecBondAgent extends CMISAgent {

	public String getAllInvcAndBondAmt(String poNo, Connection conn)
			throws SQLException, ComponentException {
		IqpActrecBondDao dao = (IqpActrecBondDao) this
				.getDaoInstance("IqpActrecBondDao");
		return dao.getAllInvcAndBondAmt(poNo, conn);
	}

	public int deleteByNo(String tableName, String conditionSql, Connection conn)
			throws ComponentException, SQLException {
		IqpActrecBondDao dao = (IqpActrecBondDao) this
				.getDaoInstance("IqpActrecBondDao");
		return dao.deleteByNo(tableName, conditionSql, conn);
	}
}
