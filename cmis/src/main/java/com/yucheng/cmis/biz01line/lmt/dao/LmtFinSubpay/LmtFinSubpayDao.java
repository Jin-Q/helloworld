package com.yucheng.cmis.biz01line.lmt.dao.LmtFinSubpay;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.ecc.emp.core.EMPException;
import com.yucheng.cmis.pub.CMISDao;

public class LmtFinSubpayDao extends CMISDao {

	/**
	 * 查询代偿总额
	 * 
	 * @param serno
	 * @param conn
	 * @return
	 * @throws EMPException
	 * @throws SQLException 
	 */
	public double getTotalSubpay(String serno, Connection conn)
			throws EMPException, SQLException {
		double amt = 0;
		Statement stmt = null;
		ResultSet rs = null;

		String sql = "";
		try {
			sql = "select sum(SUBPAY_CAP) as subpayCap , sum(SUBPAY_INT) as subpayInt from lmt_subpay_list where serno='"
					+ serno + "'";
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				amt = rs.getDouble("subpayCap") + rs.getDouble("subpayInt");
			}
		} catch (SQLException e) {
			throw new EMPException("查询代偿总额出错！");
		} finally {
				if (rs != null) {
					rs.close();
					rs = null;
				}
				if (stmt != null) {
					stmt.close();
					stmt = null;
				}
		}
		return amt;
	}

	/**
	 * 查询代偿笔数
	 * @param serno
	 * @param conn
	 * @return
	 * @throws EMPException
	 * @throws SQLException 
	 */
	public double getSubpayTimes(String serno, Connection conn)
			throws EMPException, SQLException {
		int SubpayTimes = 0;
		Statement stmt = null;
		ResultSet rs = null;

		String sql = "";
		try {
			sql = "select count(*) as SubpayTimes from lmt_subpay_list where serno='"
					+ serno + "'";
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				SubpayTimes = rs.getInt("SubpayTimes");
			}
		} catch (SQLException e) {
			throw new EMPException("查询代偿总额出错！");
		} finally {
				if (rs != null) {
					rs.close();
					rs = null;
				}
				if (stmt != null) {
					stmt.close();
					stmt = null;
				}
		}
		return SubpayTimes;
	}

}
