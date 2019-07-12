package com.yucheng.cmis.biz01line.cus.cuscom.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.core.EMPException;
import com.yucheng.cmis.pub.exception.ComponentException;

/**
 *@Classname CusComManagerDao.java
 *@Version 1.0
 *@Since 1.0 2008-9-18 下午03:24:05
 *@Copyright yuchengtech
 *@Author yuch
 *@Description：
 *@Lastmodified
 *@Author
 */
public class CusComRelApitalDao {
	/**
	 * 根据客户ID去查询其实收注册资金
	 * 
	 * @param cusId
	 * @param conn
	 * @return
	 * @throws EMPException
	 */
	public Map getRegCapAmt(String cusId, Connection conn)
			throws EMPException {
//		double amt = 0;
		Statement stmt = null;
		ResultSet rs = null;
		Map map = new HashMap();

		String sql = "";
		try {
			sql = "select paid_cap_cur_type,paid_cap_amt from cus_com where cus_id='"
					+ cusId + "'";
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				double amt = rs.getDouble("paid_cap_amt");//金额
				String cur_type = rs.getString("paid_cap_cur_type");//币种
				map.put("paid_cap_cur_type", cur_type);
				map.put("paid_cap_amt", amt);
			}
		} catch (SQLException e) {
			throw new EMPException("查询客户实收注册资金出错！");
		} finally {
			try {
				if (rs != null) {
					rs.close();
					rs = null;
				}
				if (stmt != null) {
					stmt.close();
					stmt = null;
				}
			} catch (SQLException e) {
				throw new ComponentException();
			}
		}
		return map;
	}

	/**
	 * 根据客户ID查询其资本构成总和
	 * 
	 * @param cusId
	 * @param conn
	 * @return
	 * @throws EMPException
	 */
//	public double getTotalInvtAmt(String cusId, Connection conn)
//			throws EMPException {
//		double amt = 0;
//		Statement stmt = null;
//		ResultSet rs = null;
//
//		String sql = "";
//		try {
//			sql = "select sum(invt_amt) as invtAmt from cus_com_rel_apital where cus_id='"
//					+ cusId + "'";
//			stmt = conn.createStatement();
//			rs = stmt.executeQuery(sql);
//			while (rs.next()) {
//				amt = rs.getDouble("invtAmt");
//			}
//		} catch (SQLException e) {
//			throw new EMPException("查询客户资本构成总和出错！");
//		} finally {
//			try {
//				if (rs != null) {
//					rs.close();
//					rs = null;
//				}
//				if (stmt != null) {
//					stmt.close();
//					stmt = null;
//				}
//			} catch (SQLException e) {
//				throw new ComponentException();
//			}
//		}
//		return amt;
//	}

//	public double getUpdateTotalInvtAmt(String cusId,String cusIdRel, Connection conn)
//			throws EMPException {
//		double amt = 0;
//		Statement stmt = null;
//		ResultSet rs = null;
//
//		String sql = "";
//		try {
//			sql = "select invt_amt as invtAmt from cus_com_rel_apital where cus_id='"
//					+ cusId + "' and cus_id_rel='"+cusIdRel+"'";
//			stmt = conn.createStatement();
//			rs = stmt.executeQuery(sql);
//			while (rs.next()) {
//				amt = rs.getDouble("invtAmt");
//			}
//		} catch (SQLException e) {
//			throw new EMPException("查询客户原有出资金额出错！");
//		} finally {
//			try {
//				if (rs != null) {
//					rs.close();
//					rs = null;
//				}
//				if (stmt != null) {
//					stmt.close();
//					stmt = null;
//				}
//			} catch (SQLException e) {
//				throw new ComponentException();
//			}
//		}
//		return amt;
//	}

	/**
	 * 
	 * @param comMrgCertTyp
	 * @param comMrgCertCode
	 * @return String 如果返回为null表示没有查到，
	 */
	public double getTotelInvtPerc(String cus_id, Connection conn)
			throws EMPException {

		double invtPerc = 0;
		Statement stmt = null;
		ResultSet rs = null;

		try {
			stmt = conn.createStatement();
			String sql = "select sum(invt_perc) invt_perc from cus_com_rel_apital "
					+ "where cus_id='" + cus_id + "'";

			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				invtPerc = rs.getDouble("invt_perc");
			}

		} catch (SQLException e) {
			throw new EMPException("查询出资比例出错");
		} finally {
			try {

				if (rs != null) {
					rs.close();
					rs = null;
				}
				if (stmt != null) {
					stmt.close();
					stmt = null;
				}
				if (conn != null) {
					// conn.close();
					// conn = null;
				}
			} catch (SQLException e) {
				throw new ComponentException();
			}
		}

		return invtPerc;
	}

	public Map<String,Double> getUpdateTotelInvtPerc(String cus_id, String cus_id_rel, Connection conn) throws EMPException {
		Statement stmt = null;
		ResultSet rs = null;
		Map<String,Double> values = new HashMap<String,Double>();
		double totelInvtPerc = 0;
		double invtPerc = 0;
		try {
			stmt = conn.createStatement();
			String sql = "select a.amt_a totelInvtPerc,b.amt_b invt_perc "
					+ " from "
					+ "(select  cus_id,sum(invt_perc)  amt_a "
					+ " from CUS_COM_REL_APITAL where cus_id = '"
					+ cus_id
					+ "'"
					+ " group by cus_id)  a  ,"
					+ "("
					+ " select cus_id,invt_perc amt_b  from CUS_COM_REL_APITAL where cus_id = '"
					+ cus_id + "'and cus_id_rel='" + cus_id_rel
					+  "'" + ")  b  "
					+ " where a.cus_id = b.cus_id";
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				totelInvtPerc = rs.getDouble("totelInvtPerc");
				invtPerc = rs.getDouble("invt_perc");
				values.put("totelInvtPerc", totelInvtPerc);
				values.put("invt_perc", invtPerc);
			}

		} catch (SQLException e) {
			throw new EMPException("查询出资比例出错");
		} finally {
			try {
				if (rs != null) {
					rs.close();
					rs = null;
				}
				if (stmt != null) {
					stmt.close();
					stmt = null;
				}
				if (conn != null) {
				}
			} catch (SQLException e) {
				throw new ComponentException();
			}
		}

		return values;
	}
}
