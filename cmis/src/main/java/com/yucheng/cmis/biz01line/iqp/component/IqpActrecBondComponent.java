package com.yucheng.cmis.biz01line.iqp.component;

import java.sql.Connection;
import java.sql.SQLException;

import com.ecc.emp.data.DuplicatedDataNameException;
import com.ecc.emp.data.InvalidArgumentException;
import com.ecc.emp.data.ObjectNotFoundException;
import com.yucheng.cmis.biz01line.iqp.agent.IqpActrecBondAgent;
import com.yucheng.cmis.pub.CMISComponent;
import com.yucheng.cmis.pub.exception.ComponentException;

public class IqpActrecBondComponent extends CMISComponent {

	public String getAllInvcAndBondAmt(String poNo, Connection conn)
			throws SQLException, ComponentException, InvalidArgumentException,
			DuplicatedDataNameException, ObjectNotFoundException {
		IqpActrecBondAgent cmisAgent = (IqpActrecBondAgent) this
				.getAgentInstance("IqpActrecBondAgent");
		return cmisAgent.getAllInvcAndBondAmt(poNo, conn);
	}

	public int deleteByNo(String tableName, String conditionSql, Connection conn)
			throws ComponentException, SQLException {
		IqpActrecBondAgent cmisAgent = (IqpActrecBondAgent) this
				.getAgentInstance("IqpActrecBondAgent");
		return cmisAgent.deleteByNo(tableName, conditionSql, conn);
	}

}
