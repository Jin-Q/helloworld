package com.yucheng.cmis.biz01line.iqp.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.yucheng.cmis.pub.CMISDao;

public class IqpActrecBondDao extends CMISDao {

	public String getAllInvcAndBondAmt(String poNo, Connection conn)
			throws SQLException {

		String sReturn = "";
		Statement stmt = null;
		ResultSet rs = null;

		String sql = "select count(*)  as invcQuant  ,nvl(sum(INVC_AMT),0) as invcAmt ,nvl(sum(BOND_AMT) ,0) as bondAmt from iqp_actrecbond_detail WHERE PO_NO ='"
				+ poNo + "' and status='2'";
		stmt = conn.createStatement();
		rs = stmt.executeQuery(sql);
		while (rs.next()) {
			sReturn = rs.getString("invcQuant") + "@" + rs.getString("invcAmt")
					+ "@" + rs.getString("bondAmt");
		}
		return sReturn;
	}

	public int deleteByNo(String tableName, String conditionSql, Connection conn)
			throws SQLException {
		Statement stmt = null;
		int count = 0;
		stmt = conn.createStatement();
		String sql = "delete from " + tableName + " where  " + conditionSql;
		count = stmt.executeUpdate(sql);
		return count;

	}

}
